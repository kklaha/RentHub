package com.klaha.renthub.bookings.dto;


import com.klaha.renthub.items.entity.Item;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingRequestDto {
    @NotBlank(message = "Выберите объявление для брони")
    private Item item;
    @NotBlank(message = "Выберите дату начала")
    private OffsetDateTime startTime;
    @NotBlank(message = "Выберите дату окончания")
    private OffsetDateTime endTime;
}
