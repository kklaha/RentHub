package com.klaha.renthub.orders.service;


import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.auth.repository.UserRepository;
import com.klaha.renthub.enums.ItemStatus;
import com.klaha.renthub.enums.OrderStatus;
import com.klaha.renthub.exception.BussinesException;
import com.klaha.renthub.exception.ItemNotActiveException;
import com.klaha.renthub.exception.StatusException;
import com.klaha.renthub.items.entity.Item;
import com.klaha.renthub.items.repository.ItemRepository;
import com.klaha.renthub.orders.dto.CreateOrderRequestDto;
import com.klaha.renthub.orders.dto.OrderResponseDto;
import com.klaha.renthub.orders.entity.Order;
import com.klaha.renthub.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequestDto dto,Long buyerId){
        Item item=itemRepository.
                findById(dto.getItem().getId()).
                orElseThrow(()->new EntityNotFoundException("Вещь не найдена"));
        if(item.getStatus()!= ItemStatus.ACTIVE){
            throw new ItemNotActiveException("Предмет зарезервирован");
        }
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new EntityNotFoundException("Покупатель не найден"));
        Order order= Order.builder().item(item).buyer(buyer).
                seller(item.getUser()).price(item.getPrice()).
                status(OrderStatus.CREATED).
                deliveryType(dto.getDeliveryType()).createdAt(OffsetDateTime.now()).build();
        Order saved=orderRepository.save(order);
        return OrderResponseDto.from(saved);
    }
    @Transactional
    public OrderResponseDto confirmOrder(Long orderId,Long sellerId){
        Order order = orderRepository.findByIdAndSellerId(orderId,sellerId).
                orElseThrow(() -> new EntityNotFoundException("Заказ не найедн"));
        if (!order.getStatus().canMoveTo(OrderStatus.CONFIRMED)){
            throw new StatusException("Нельзя переместить заказ в этот статус");
        }
        if(order.getItem().getStatus()!=ItemStatus.ACTIVE){
            throw new StatusException("Объявление не активно");
        }
        order.setStatus(OrderStatus.CONFIRMED);
        order.getItem().setStatus(ItemStatus.PAID);
        try {
            orderRepository.saveAndFlush(order);
        } catch (DataIntegrityViolationException e) {
            throw new BussinesException("На эту вещь уже принят другой заказ");
        }
            return OrderResponseDto.from(order);

    }
    @Transactional
    public OrderResponseDto rejectOrder(Long orderId,Long sellerId){
        Order order= orderRepository.findByIdAndSellerId(orderId,sellerId).
                orElseThrow(()->new EntityNotFoundException("Заказ не найден"));
        if(!order.getStatus().canMoveTo(OrderStatus.REJECTED)){
            throw new StatusException("Статус в заказе не может быть перемещен в отказ");
        }
        order.setStatus(OrderStatus.REJECTED);
        order.getItem().setStatus(ItemStatus.ACTIVE);
        return OrderResponseDto.from(order);
    }
    @Transactional
    public OrderResponseDto cancelOrder(Long orderId,Long buyerId){
        Order order = orderRepository.findByIdAndBuyer_Id(orderId,buyerId).
                orElseThrow(()->new EntityNotFoundException("Заказ не найден"));
        if(!order.getStatus().canMoveTo(OrderStatus.CANCELED)){
            throw new StatusException("Заказ не может быть отменен");
        }
        order.setStatus(OrderStatus.CANCELED);
        order.getItem().setStatus(ItemStatus.ACTIVE);
        return OrderResponseDto.from(order);
    }
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrdersAsBuyer(Long buyerId, Pageable pageable) {
        Page<Order> orders=orderRepository.findByBuyer_Id(buyerId,pageable);
        return orders.map(OrderResponseDto::from);
    }
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrdersAsSeller(Long sellerId,Pageable pageable) {
       Page<Order> orders=orderRepository.findBySeller_Id(sellerId,pageable);
       return orders.map(OrderResponseDto::from);
    }
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderAsSeller(Long orderId,Long sellerId){
        Order order=orderRepository.findByIdAndSellerId(orderId,sellerId).
                orElseThrow(()->new EntityNotFoundException("Заказ не найден"));
        return OrderResponseDto.from(order);
    }
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderAsBuyer(Long orderId,Long buyerId){
        Order order=orderRepository.findByIdAndBuyer_Id(orderId,buyerId).
                orElseThrow(()->new EntityNotFoundException("Заказ не найден"));
        return OrderResponseDto.from(order);
    }
}
