package com.klaha.renthub.items.entity;

import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.categories.entity.Category;
import com.klaha.renthub.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @Column(name="item_name",nullable = false,length = 255)
    private String itemName;
    @Column(name="price",nullable = false)
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

