package com.klaha.renthub.bookings.controller;


import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.bookings.dto.BookingResponseDto;
import com.klaha.renthub.bookings.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProviderController {
    private final BookingService service;

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<BookingResponseDto> confirmOrder(@PathVariable Long id,
                                                           @AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.confirmBooking(id, user.getId()));
    }
    @PatchMapping("/{id}/reject")
    public ResponseEntity<BookingResponseDto> rejectOrder(@PathVariable Long id,
                                                          @AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.rejectBooking(id,user.getId()));
    }
    @GetMapping("{id}")
    public ResponseEntity<BookingResponseDto> getBookingAsProvider(@PathVariable Long id,
                                                                   @AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.getBookingAsProvider(id, user.getId()));
    }
    @GetMapping("/bookings")
    public ResponseEntity<Page<BookingResponseDto>> getBookingsAsProvider(@AuthenticationPrincipal User user,
                                                                          @PageableDefault(size=20)Pageable pageable){
        Page<BookingResponseDto> bookings=service.getBookingsAsProvider(user.getId(),pageable);
        return ResponseEntity.ok(bookings);
    }

 }
