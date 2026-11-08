package com.klaha.renthub.bookings.entity;


import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.enums.BookingStatus;
import com.klaha.renthub.enums.Category;
import com.klaha.renthub.exception.BusinessException;
import com.klaha.renthub.items.entity.Item;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name="bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="renter_id")
    private User renter;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rentprovider_id")
    private User rentProvider;
    @Column(name="category")
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name="start_time")
    private OffsetDateTime startTime;
    @Column(name="end_time")
    private OffsetDateTime endTime;
    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @Column(name="total_price")
    private BigDecimal totalPrice;
    @Column(name="created_at")
    private OffsetDateTime createdAt=OffsetDateTime.now();


    public void validateDates(){
        if(!startTime.isBefore(endTime)){
            throw new BusinessException("Дата начала не может быть после конца");
        }
    }
}


