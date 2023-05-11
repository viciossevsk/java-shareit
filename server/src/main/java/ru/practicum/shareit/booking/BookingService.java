package ru.practicum.shareit.booking2;

import ru.practicum.shareit.booking2.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long bookerId, BookingDto bookingDto);

    BookingDto approvingBooking(Long ownerId, Long bookingId, boolean isApproved);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getBookerStatistics(Long bookerId, String state, Integer start, Integer size);

    List<BookingDto> getOwnerStatistics(Long ownerId, String state, Integer start, Integer size);
}
