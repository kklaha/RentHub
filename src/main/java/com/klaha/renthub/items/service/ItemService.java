package com.klaha.renthub.items.service;


import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.auth.repository.UserRepository;
import com.klaha.renthub.items.dto.CreateItemDto;
import com.klaha.renthub.items.dto.ItemResponseDto;
import com.klaha.renthub.items.dto.UpdateItemRequestDto;
import com.klaha.renthub.items.dto.UpdateStatusDto;
import com.klaha.renthub.items.entity.Item;
import com.klaha.renthub.items.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public ItemResponseDto createItem(CreateItemDto dto,Long userId){
        User user=userRepository.findById(userId).
                orElseThrow(()-> new EntityNotFoundException("Пользователь не найден"));

        Item item= Item.builder().
                user(user).
                category(dto.getCategory()).
                itemName(dto.getItemName()).
                price(dto.getPrice()).address(dto.getAddress()).
                description(dto.getDescription()).city(dto.getCity()).build();
        Item saved=itemRepository.save(item);
        return from(saved);
    }
    @Transactional
    public ItemResponseDto updateItem(Long itemId,UpdateItemRequestDto dto,Long userId){
        Item item=itemRepository.findByIdAndUserId(itemId,userId).
                orElseThrow(()->new EntityNotFoundException("Предмет не найден"));
        if(dto.getItemName()!=null){ item.setItemName(dto.getItemName());}
        if(dto.getDescription()!=null){ item.setDescription(dto.getDescription());}
        if(dto.getPrice()!=null){ item.setPrice(dto.getPrice());}

        return from(item);
    }
    @Transactional
    public ItemResponseDto updateStatus(Long itemId,UpdateStatusDto dto,Long userId){
        Item item=itemRepository.findByIdAndUserId(itemId,userId).
                orElseThrow(()->new EntityNotFoundException("Предмет не найден"));
        item.setStatus(dto.getStatus());
        return from(item);
    }
    @Transactional
    public void deleteItem(Long itemId,Long userId){
        Item item=itemRepository.findByIdAndUserId(itemId,userId).
                orElseThrow(()->new EntityNotFoundException("Предмет не найден"));
        itemRepository.delete(item);
    }
    @Transactional(readOnly = true)
    public Page<ItemResponseDto> getUserItems(Long userId, Pageable pageable){
        Page<Item> items=itemRepository.findItemsByUserId(userId,pageable);
        return items.map(ItemService::from);

    }
    @Transactional(readOnly = true)
    public ItemResponseDto getItem(Long itemId,Long userId){
        Item item=itemRepository.findByIdAndUserId(itemId,userId).
                orElseThrow(()->new EntityNotFoundException("Вещь не найдена"));
        return from(item);
    }
    public static ItemResponseDto from(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .userId(item.getUser().getId())
                .category(item.getCategory())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .address(item.getAddress())
                .description(item.getDescription())
                .city(item.getCity())
                .status(item.getStatus())
                .createdAt(item.getCreatedAt())
                .build();
    }
}

