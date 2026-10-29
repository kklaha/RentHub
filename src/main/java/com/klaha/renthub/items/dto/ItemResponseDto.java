package com.klaha.renthub.items.dto;


import com.klaha.renthub.enums.Category;
import com.klaha.renthub.enums.ItemStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {

    private Long id;
    private Long userId;
    private String itemName;
    private Category category;
    private BigDecimal price;
    private String address;
    private String description;
    private String city;
    private ItemStatus status;
    private OffsetDateTime createdAt;

}
