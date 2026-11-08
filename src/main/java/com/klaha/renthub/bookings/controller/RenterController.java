package com.klaha.renthub.bookings.controller;


import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.bookings.dto.BookingResponseDto;
import com.klaha.renthub.bookings.dto.CreateBookingRequestDto;
import com.klaha.renthub.bookings.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/renter")
public class RenterController {
    private final BookingService service;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createOrder(@Valid CreateBookingRequestDto dto,
                                                          @AuthenticationPrincipal User user){
        BookingResponseDto booking = service.createBooking(dto,user.getId());
        return ResponseEntity.ok(booking);
    }
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<BookingResponseDto> confirmOrder(@PathVariable Long id,
                                                           @AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.confirmBooking(id, user.getId()));
    }
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDto> cancelOrder(@PathVariable Long id,
                                                          @AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.cancelBooking(id,user.getId()));
    }
    @GetMapping("/bookings")
    public ResponseEntity<Page<BookingResponseDto>> getBookingsAsRenter(@AuthenticationPrincipal User user,
                                             @PageableDefault(size=20) Pageable pageable){
        Page<BookingResponseDto> bookings = service.getBookingsAsRenter(user.getId(),pageable);
        return ResponseEntity.ok(bookings);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingAsRenter(@PathVariable Long id,
                                                                 @AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.getBookingAsRenter(id,user.getId()));
    }
}
