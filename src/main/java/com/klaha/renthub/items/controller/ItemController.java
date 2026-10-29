package com.klaha.renthub.items.controller;


import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.items.dto.CreateItemDto;
import com.klaha.renthub.items.dto.ItemResponseDto;
import com.klaha.renthub.items.dto.UpdateItemRequestDto;
import com.klaha.renthub.items.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@Valid@RequestBody CreateItemDto dto,
                                                      @AuthenticationPrincipal User user){
        ItemResponseDto created= service.createItem(dto,user.getId());
        return ResponseEntity.created(URI.create("/api/v1/items/"+created.getId())).body(created);
    }
    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> updateItem(@Valid@RequestBody UpdateItemRequestDto dto,
                                                      @AuthenticationPrincipal User user, @PathVariable Long itemId){
        ItemResponseDto updated=service.updateItem(itemId,dto,user.getId());
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId,@AuthenticationPrincipal User user){
        service.deleteItem(itemId,user.getId());
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<Page<ItemResponseDto>> getItems(@AuthenticationPrincipal User user,
                                                          Pageable pageable){
        Page<ItemResponseDto> items=service.getUserItems(user.getId(), pageable);
        return ResponseEntity.ok(items);
    }
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable Long itemId,
                                                   @AuthenticationPrincipal User user){
            ItemResponseDto item=service.getItem(itemId,user.getId());
            return ResponseEntity.ok(item);
    }

}
