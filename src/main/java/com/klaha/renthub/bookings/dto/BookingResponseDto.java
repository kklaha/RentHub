package com.klaha.renthub.bookings.dto;


import com.klaha.renthub.bookings.entity.Booking;
import com.klaha.renthub.enums.BookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Long id;
    private Long renterId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private BookingStatus status;
    private BigDecimal price;

    public static BookingResponseDto from(Booking booking){
        return BookingResponseDto.builder().
                id(booking.getBookingId()).renterId(booking.getRenter().getId()).
                startTime(booking.getStartTime()).endTime(booking.getEndTime()).
                status(booking.getStatus()).price(booking.getTotalPrice()).build();
    }

}
