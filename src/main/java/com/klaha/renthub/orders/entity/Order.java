package com.klaha.renthub.orders.entity;


import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.enums.DeliveryType;
import com.klaha.renthub.enums.OrderStatus;
import com.klaha.renthub.items.entity.Item;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


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
    @JoinColumn(name="item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="buyer_id")
    private User buyer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;
    @Column(name="price",nullable = false)
    private BigDecimal price;
    @Column(name="status",nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name="delivery_type",length=32,nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DeliveryType deliveryType=DeliveryType.SELFPICKUP;
    @Column(name = "created_at",nullable = false)
    @Builder.Default
    private OffsetDateTime createdAt=OffsetDateTime.now();


}
