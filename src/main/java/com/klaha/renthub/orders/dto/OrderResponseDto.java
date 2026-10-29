package com.klaha.renthub.orders.dto;

import com.klaha.renthub.enums.DeliveryType;
import com.klaha.renthub.enums.OrderStatus;
import com.klaha.renthub.items.entity.Item;
import com.klaha.renthub.orders.entity.Order;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long id;
    private Long buyerId;
    private OrderStatus status;
    private Item item;
    private OffsetDateTime createdAt;
    private BigDecimal price;
    private DeliveryType deliveryType;

    public static OrderResponseDto from(Order order){
        return OrderResponseDto.builder().
                id(order.getId()).buyerId(order.getBuyer().getId()).status(order.getStatus()).
                item(order.getItem()).
                createdAt(order.getCreatedAt()).price(order.getPrice()).
                deliveryType(order.getDeliveryType()).build();
    }
}
