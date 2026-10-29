package com.klaha.renthub.items.dto;


import com.klaha.renthub.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateStatusDto {
    private ItemStatus status;
}
