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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seller")
public class SellerController {
    private final OrderService service;

    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersAsSeller(@AuthenticationPrincipal User seller,
                                                                    @PageableDefault(size=20) Pageable pageable){
        Page<OrderResponseDto> orders=service.getOrdersAsSeller(seller.getId(),pageable);
        return ResponseEntity.ok(orders);
    }
    @PatchMapping("/{orderId}/reject")
    public ResponseEntity<OrderResponseDto> rejectOrder(@PathVariable Long orderId,
                                                        @AuthenticationPrincipal User seller){
        OrderResponseDto rejected=service.rejectOrder(orderId,seller.getId());
        return ResponseEntity.ok(rejected);
    }

    @PatchMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponseDto> confirmOrder(@PathVariable Long orderId,
                                                         @AuthenticationPrincipal User seller){
        OrderResponseDto confirmed=service.confirmOrder(orderId,seller.getId());
        return ResponseEntity.ok(confirmed);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId,
                                                     @AuthenticationPrincipal User seller){
        OrderResponseDto order=service.getOrderAsSeller(orderId,seller.getId());
        return ResponseEntity.ok(order);
    }
}
