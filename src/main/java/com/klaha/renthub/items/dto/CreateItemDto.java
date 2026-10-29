package com.klaha.renthub.items.dto;


import com.klaha.renthub.enums.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateItemDto {
    @NotBlank(message = "Имя вещи не может быть пустым")
    private String itemName;
    @NotBlank(message = "Категория не может быть пустой")
    private Category category;
    private String description;
    @NotBlank(message = "У вещи должна быть цена ")
    private BigDecimal price;
    @NotBlank(message = "Указать адресс обязательно")
    private String address;
    @NotBlank(message = "Укажите город")
    private String city;
}
