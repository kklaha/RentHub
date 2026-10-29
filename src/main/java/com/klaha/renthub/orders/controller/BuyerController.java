package com.klaha.renthub.orders.controller;


import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.orders.dto.CreateOrderRequestDto;
import com.klaha.renthub.orders.dto.OrderResponseDto;
import com.klaha.renthub.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buyer")
public class BuyerController {
    private final OrderService service;
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody CreateOrderRequestDto dto,
                                                        @AuthenticationPrincipal User buyer){
        OrderResponseDto order=service.createOrder(dto,buyer.getId());
        return ResponseEntity.created(URI.create("/api/v1/orders/"+order.getId())).body(order);
    }
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId,
                                                        @AuthenticationPrincipal User buyer){
        OrderResponseDto canceled=service.cancelOrder(orderId,buyer.getId());
        return ResponseEntity.ok(canceled);
    }
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersAsBuyer(@AuthenticationPrincipal User buyer,
                                                                   @PageableDefault(size=20) Pageable pageable){
        Page<OrderResponseDto> orders=service.getOrdersAsBuyer(buyer.getId(),pageable);
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId,
                                                     @AuthenticationPrincipal User buyer){
        OrderResponseDto order=service.getOrderAsBuyer(orderId,buyer.getId());
        return ResponseEntity.ok(order);
    }



}
