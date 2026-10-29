package com.klaha.renthub.items.repository;

import com.klaha.renthub.items.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item,Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<Item> findItemsByUserId(Long userId, Pageable pageable);
    Optional<Item> findByIdAndUserId(Long itemId,Long userId);
}
