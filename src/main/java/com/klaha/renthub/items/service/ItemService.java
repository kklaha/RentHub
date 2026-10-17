package com.klaha.renthub.items.service;


import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.auth.repository.UserRepository;
import com.klaha.renthub.categories.entity.Category;
import com.klaha.renthub.categories.repository.CategoryRepository;
import com.klaha.renthub.enums.ItemStatus;
import com.klaha.renthub.items.dto.CreateItemDto;
import com.klaha.renthub.items.dto.ItemResponseDto;
import com.klaha.renthub.items.entity.Item;
import com.klaha.renthub.items.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor

public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ItemResponseDto createItem(CreateItemDto dto,Long userId){
        User user=userRepository.findById(userId).
                orElseThrow(()-> new EntityNotFoundException("Пользователь не найден"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

        Item item= Item.builder().
                user(user).
                category(category).
                itemName(dto.getItemName()).
                price(dto.getPrice()).address(dto.getAddress()).
                description(dto.getDescription()).city(dto.getCity()).build();
        Item saved=itemRepository.save(item);
        return ItemResponseDto.builder().
                itemName(saved.getItemName()).
                id(saved.getId()).categoryId(saved.getCategory().getId()).price(saved.getPrice()).build();
    }

}

