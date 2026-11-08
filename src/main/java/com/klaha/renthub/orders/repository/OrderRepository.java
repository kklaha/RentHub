package com.klaha.renthub.orders.repository;

import com.klaha.renthub.orders.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @EntityGraph(attributePaths = {"item"})
    Page<Order> findByBuyerId(Long buyerId, Pageable pageable);
    @EntityGraph(attributePaths = {"item"})
    Page<Order> findBySellerId(Long sellerId,Pageable pageable);
    Optional<Order> findByIdAndBuyer_Id(Long id,Long buyerID);
    @Query("SELECT o FROM Order o WHERE o.id = :id AND o.item.user.id = :sellerId")
    Optional<Order> findByIdAndSellerId(@Param("id") Long id, @Param("sellerId") Long sellerId);
    @Modifying(flushAutomatically = true)
    @Query(
            """ 
                     UPDATE Order o 
                     set o.status=com.klaha.renthub.enums.OrderStatus.REJECTED 
                     where o.itemId= :itemId and o.id<> :confirmedId 
                     and o.status <> com.klaha.renthub.enums.OrderStatus.CREATED""")
    int rejectOtherResponse(@Param("itemId") Long itemId,@Param("conrimedId") Long confrimedId);
}
