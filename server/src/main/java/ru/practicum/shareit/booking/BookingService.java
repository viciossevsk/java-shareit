package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long bookerId, BookingDto bookingDto);

    BookingDto approvingBooking(Long ownerId, Long bookingId, boolean isApproved);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getBookerStatistics(Long bookerId, String state, Integer from, Integer size);

    List<BookingDto> getOwnerStatistics(Long ownerId, String state, Integer from, Integer size);
}
