package com.klaha.renthub.items.controller;


import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.items.dto.CreateItemDto;
import com.klaha.renthub.items.dto.ItemResponseDto;
import com.klaha.renthub.items.service.ItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/items")
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ResponseEntity<ItemResponseDto> createItme(@Valid@NotBlank CreateItemDto dto,
                                                      @AuthenticationPrincipal User user){
        ItemResponseDto created= service.createItem(dto,user.getId());
        return ResponseEntity.created(URI.create("/api/v1/items"+created.getId())).body(created);
    }
}
