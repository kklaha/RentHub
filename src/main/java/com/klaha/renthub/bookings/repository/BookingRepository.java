package com.klaha.renthub.bookings.repository;

import com.klaha.renthub.bookings.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    @EntityGraph(attributePaths = {"item"})
    Page<Booking> findByProviderId(Long provideId,Pageable pageable);
    Optional<Booking> findByIdAndRenterId(Long bookingId,Long renterId);
    @EntityGraph(attributePaths = {"item"})
    Page<Booking> findByRenterId(Long renter, Pageable pageable);
    @Query("SELECT b from Booking b where b.id = :id and b.item.user.id= :sellerId")
    Optional<Booking> findByIdAndProviderId(@Param("id") Long bookingId,@Param("sellerId") Long sellerId);
    @Query("""
    select count(b) > 0 from Booking b
    where b.item.id = :itemId
      and b.status = com.klaha.renthub.enums.BookingStatus.CONFIRMED
      and b.startTime < :end
      and b.endTime > :start
""")
    boolean existsOverlapping(@Param("itemId") Long itemId,
                              @Param("start") OffsetDateTime start,
                              @Param("end") OffsetDateTime end);

}
