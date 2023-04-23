package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingNotMatchException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UnknownStateException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto createBooking(Long bookerId, BookingDto bookingDto) {
        validate(bookerId, bookingDto);

        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                                                                                        new EntityNotFoundException(String.format(MISTAKEN_ITEM_ID, bookingDto.getItemId())));


        if (!item.getAvailable()) {
            throw new ValidationException("Booking not available");
        }

        if (item.getOwner().getId().equals(bookerId)) {
            throw new BookingNotMatchException("Mistaken to booking item by owner");
        }

        User booker = getUserById(bookerId);
        Booking booking = bookingMapper.toBooking(bookingDto);

        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);

        bookingRepository.save(booking);

        return bookingMapper.toBookingDto(bookingRepository.findById(booking.getId()).orElseThrow(() ->
                                                                                                          new EntityNotFoundException(String.format(MISTAKEN_BOOKING_ID, booking.getId()))));
    }

    @Override
    @Transactional
    public BookingDto approvingBooking(Long ownerId, Long bookingId, boolean isApproved) {
        Booking booking = getBookingById(bookingId);

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalArgumentException("Rejected for approving operation");
        }

        if (booking.getItem().getOwner().getId().equals(ownerId)) {
            if (isApproved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
        } else {
            throw new EntityNotFoundException(String.format(MISTAKEN_USER_ID, ownerId));
        }

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBooking(Long bookingId, Long userId) {
        Booking booking = getBookingById(bookingId);

        if (booking.getItem().getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)) {
            return bookingMapper.toBookingDto(booking);
        }

        throw new EntityNotFoundException(String.format(MISTAKEN_USER_ID, userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookerStatistics(Long bookerId, String state) {
        User booker = getUserById(bookerId);

        Set<Long> bookingIds = booker.getBookings().stream()
                .map(Booking::getId)
                .collect(Collectors.toSet());

        Set<Booking> bookings = bookingRepository.findBookingsAndFetchAllEntities(bookingIds);

        return getBookingStatistics(bookings, state);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getOwnerStatistics(Long ownerId, String state) {
        User owner = getUserById(ownerId);

        Set<Long> bookingIds = owner.getItems().stream()
                .flatMap(item -> item.getBookings().stream())
                .map(Booking::getId)
                .collect(Collectors.toSet());

        Set<Booking> bookings = bookingRepository.findBookingsAndFetchAllEntities(bookingIds);

        return getBookingStatistics(bookings, state);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_USER_ID, userId)));
    }

    private Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_BOOKING_ID, bookingId)));
    }

    private void validate(Long bookerId, BookingDto bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("Booking start time after end time");
        }
        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new EntityNotFoundException(String.format(MISTAKEN_ITEM_ID, bookingDto.getItemId()));
        }

        if (!userRepository.existsById(bookerId)) {
            throw new EntityNotFoundException(String.format(MISTAKEN_USER_ID, bookerId));
        }
    }

    private BookingState getState(String requestState) {
        try {
            return BookingState.valueOf(requestState);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException(String.format(UNKNOWN_STATE, requestState));
        }
    }

    private List<BookingDto> getBookingStatistics(Collection<Booking> bookings, String requestState) {
        LocalDateTime now = LocalDateTime.now();

        switch (getState(requestState)) {
            case PAST:
                return bookings.stream()
                        .filter(booking -> booking.getEnd().isBefore(now))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(bookingMapper::toBookingDto)
                        .collect(Collectors.toCollection(LinkedList::new));
            case CURRENT:
                return bookings.stream()
                        .filter(booking -> booking.getStart().isBefore(now)
                                && booking.getEnd().isAfter(now))
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .map(bookingMapper::toBookingDto)
                        .collect(Collectors.toCollection(LinkedList::new));
            case FUTURE:
                return bookings.stream()
                        .filter(booking -> booking.getEnd().isAfter(now))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(bookingMapper::toBookingDto)
                        .collect(Collectors.toCollection(LinkedList::new));
            case WAITING:
                return bookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.WAITING)
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(bookingMapper::toBookingDto)
                        .collect(Collectors.toCollection(LinkedList::new));
            case REJECTED:
                return bookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.REJECTED)
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(bookingMapper::toBookingDto)
                        .collect(Collectors.toCollection(LinkedList::new));
            default:
                return bookings.stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(bookingMapper::toBookingDto)
                        .collect(Collectors.toCollection(LinkedList::new));
        }
    }
}
