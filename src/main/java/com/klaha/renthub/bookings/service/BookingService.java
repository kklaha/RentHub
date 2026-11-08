package com.klaha.renthub.bookings.service;


import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.auth.repository.UserRepository;
import com.klaha.renthub.bookings.dto.BookingResponseDto;
import com.klaha.renthub.bookings.dto.CreateBookingRequestDto;
import com.klaha.renthub.bookings.entity.Booking;
import com.klaha.renthub.bookings.repository.BookingRepository;
import com.klaha.renthub.enums.BookingStatus;
import com.klaha.renthub.enums.ItemStatus;
import com.klaha.renthub.exception.BusinessException;
import com.klaha.renthub.exception.StatusException;
import com.klaha.renthub.items.entity.Item;
import com.klaha.renthub.items.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;


@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingResponseDto createBooking(CreateBookingRequestDto dto, Long renterId){
        Item item= itemRepository.findById(dto.getItem().getId()).
                orElseThrow(()->new EntityNotFoundException("Объявление не найдено"));
        if(item.getStatus()!=ItemStatus.ACTIVE){
            throw new StatusException("Предмет не может быть забронирован");
        }
        if(bookingRepository.existsOverlapping(item.getId(),dto.getStartTime(),dto.getEndTime())){
            throw new BusinessException("Эти даты уже заняты");
        }
        if(renterId.equals(item.getUser().getId())){
            throw new BusinessException("Нельзя забронировать свое же объявление");
        }
        User renter=userRepository.findById(renterId).
                orElseThrow(()->new EntityNotFoundException("Пользователь не найден"));
        Booking booking= Booking.builder()
                .item(item).renter(renter).
                rentProvider(item.getUser()).category(item.getCategory()).
                startTime(dto.getStartTime()).endTime(dto.getEndTime()).status(BookingStatus.CREATED).
                totalPrice(totalPrice(dto)).createdAt(OffsetDateTime.now()).build();
        Booking saved=bookingRepository.save(booking);
        return  BookingResponseDto.from(saved);
    }
    @Transactional
    public BookingResponseDto confirmBooking(Long bookingId,Long sellerId){
        Booking booking =bookingRepository.findByIdAndProviderId(bookingId,sellerId).
                orElseThrow(()-> new EntityNotFoundException("Бронирование не найдено"));
        if(!booking.getStatus().canMoveTo(BookingStatus.CONFIRMED)){
            throw new StatusException("Бронирование не может быть подтверждено");
        }
        if(booking.getItem().getStatus()!=ItemStatus.ACTIVE){
            throw new BusinessException("Объявление не активно");
        }
        if (bookingRepository.existsOverlapping(
                booking.getItem().getId(), booking.getStartTime(), booking.getEndTime())) {
            throw new BusinessException("Эти даты уже заняты");
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        try{
            bookingRepository.saveAndFlush(booking);
        }catch (DataIntegrityViolationException e){
            throw new BusinessException("Объявление уже забронировано");
        }
        return BookingResponseDto.from(booking);
    }
    @Transactional
    public BookingResponseDto cancelBooking(Long bookingId,Long renterId){
        Booking booking=bookingRepository.findByIdAndRenterId(bookingId,renterId).
                orElseThrow(()->new EntityNotFoundException("Бронирование не найдено"));
        if(!booking.getStatus().canMoveTo(BookingStatus.CANCELED)){
            throw new StatusException("Бронирование не может быть отменено");
        }
        booking.setStatus(BookingStatus.CANCELED);
        return BookingResponseDto.from(booking);
    }
    @Transactional
    public BookingResponseDto rejectBooking(Long bookingId,Long providerId){
        Booking booking=bookingRepository.findByIdAndProviderId(bookingId,providerId).
                orElseThrow(()->new EntityNotFoundException("Бронирование не найдено"));
        if(!booking.getStatus().canMoveTo(BookingStatus.REJECTED)){
            throw new StatusException("Бронирование не может быть отклонено");
        }
        booking.setStatus(BookingStatus.REJECTED);
        return BookingResponseDto.from(booking);
    }
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingAsRenter(Long bookingId,Long renterId){
        Booking booking=bookingRepository.findByIdAndRenterId(bookingId,renterId).
                orElseThrow(()->new EntityNotFoundException("Бронирование не найдено"));
        return BookingResponseDto.from(booking);
    }
    @Transactional(readOnly = true)
    public Page<BookingResponseDto> getBookingsAsRenter(Long renterId, Pageable pageable){
        Page<Booking> bookings = bookingRepository.findByRenterId(renterId,pageable);
        return bookings.map(BookingResponseDto::from);
    }
    @Transactional(readOnly = true)
    public Page<BookingResponseDto> getBookingsAsProvider(Long providerId,Pageable pageable){
        Page<Booking> bookings = bookingRepository.findByProviderId(providerId,pageable);
        return bookings.map(BookingResponseDto::from);
    }
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingAsProvider(Long bookingId,Long providerId){
        Booking booking =bookingRepository.findByIdAndProviderId(bookingId,providerId).
                orElseThrow(()-> new EntityNotFoundException("Бронирование не найдено"));
        return BookingResponseDto.from(booking);
    }
    private BigDecimal totalPrice(CreateBookingRequestDto dto) {
        Duration duration = Duration.between(dto.getStartTime(), dto.getEndTime());
        long billedHours = (long) Math.ceil(duration.toMinutes() / 60.0);
        BigDecimal totalInHours = BigDecimal.valueOf(billedHours);
        return dto.getItem().getPrice().multiply(totalInHours);
    }

}
