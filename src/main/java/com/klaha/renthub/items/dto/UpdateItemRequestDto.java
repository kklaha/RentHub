package com.klaha.renthub.items.dto;


import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemRequestDto {
    private String itemName;
    private String description;
    private BigDecimal price;

}
