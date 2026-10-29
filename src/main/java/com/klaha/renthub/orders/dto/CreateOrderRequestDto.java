package com.klaha.renthub.orders.dto;


import com.klaha.renthub.enums.DeliveryType;
import com.klaha.renthub.items.entity.Item;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {

    @NotBlank(message = "Заказ не может быть пустым")
    private Item item;
    @NotBlank(message = "Выберите тип доставки")
    private DeliveryType deliveryType;
}
