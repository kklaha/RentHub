package com.klaha.renthub.orders.dto;


import com.klaha.renthub.items.entity.Item;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    @NotBlank(message = "Заказ не может быть пустым")
    private Item items;
    private Long buyerId;
}
