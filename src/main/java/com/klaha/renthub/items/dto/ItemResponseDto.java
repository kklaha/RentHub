package com.klaha.renthub.items.dto;


import com.klaha.renthub.enums.ItemStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {

    private Long id;
    private String itemName;
    private Long categoryId;
    private BigDecimal price;
    private String address;
    private String description;
    private String city;
    private ItemStatus status;
    private LocalDateTime createdAt;

}
