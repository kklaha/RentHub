package com.klaha.renthub.items.entity;

import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.enums.Category;
import com.klaha.renthub.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name="items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    @Column(name="category")
    @Enumerated(EnumType.STRING)
    private Category category;
    @CreationTimestamp
    @Column(name="created_at")
    private OffsetDateTime createdAt;
    @Column(name="item_name",nullable = false,length = 255)
    private String itemName;
    @Column(name="price",nullable = false,precision = 12,scale = 2)
    private BigDecimal price;
    @Column(name="address",nullable = false,length = 255)
    private String address;
    @Column(name = "description",columnDefinition = "text")
    private String description;
    @Column(name="city",nullable = false,length = 64)
    private String city;
    @Column(name="status",nullable = false,length = 32)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ItemStatus status=ItemStatus.ACTIVE;



}

