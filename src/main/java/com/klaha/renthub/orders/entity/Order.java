package com.klaha.renthub.order.entity;


import com.klaha.renthub.enums.DeliveryType;
import com.klaha.renthub.enums.OrderStatus;
import com.klaha.renthub.items.entity.Item;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name="orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="buyer_id")
    private Long buyerId;
    @JoinColumn(name = "seller_id")
    private Long sellerId;
    @Column(name="price",nullable = false)
    private BigDecimal price;
    @Column(name="status",nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status=OrderStatus.CREATED;
    @Column(name="delivery_type",length=32,nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;
    @Column(name="start_time")
    private LocalDateTime startTime;
    @Column(name="end_time")
    private LocalDateTime endTime;
    @Column(name = "created_at",nullable = false)
    @Builder.Default
    private LocalDateTime createdAt=LocalDateTime.now();


}
