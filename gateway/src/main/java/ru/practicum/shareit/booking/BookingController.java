package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(USER_ID_HEADER) long userId,
                                           @RequestBody @Valid BookingDto bookingDto) {
        log.info("Creating booking {}, userId={}", bookingDto, userId);
        if (bookingDto.getStart() == null) {
            throw new IllegalArgumentException("booking start = null");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("booking start < today time");
        }
        if (bookingDto.getEnd() == null) {
            throw new IllegalArgumentException("booking end = null");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("booking end < today time");
        }
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null || bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new IllegalArgumentException("Booking start time after or equals end time");
        }
        return bookingClient.bookItem(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approvingBooking(@RequestHeader(USER_ID_HEADER) long ownerId,
                                                   @PathVariable long bookingId,
                                                   @RequestParam(name = "approved") boolean isApproved) {
        log.info("Approving booking, bookingId={}, ownerId={}, isApproved={}", bookingId, ownerId, isApproved);
        return bookingClient.approvingBooking(ownerId, bookingId, isApproved);

    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID_HEADER) long userId,
                                             @PathVariable long bookingId) {
        log.info("Get booking, bookingId={}, userId={}", bookingId, userId);
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookerStatistics(@RequestHeader(USER_ID_HEADER) long bookerId,
                                                      @RequestParam(value = "state", required = false,
                                                              defaultValue = "ALL") String state,
                                                      @RequestParam(value = "from", required = false, defaultValue =
                                                              "0") Integer from,
                                                      @RequestParam(value = "size", required = false, defaultValue =
                                                              "20") Integer size) {

        BookingState bookingState = BookingState.getNameByStringName(state).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state)
        );
        log.info("Get booker statistics, state={}, from={}, size={}", state, from, size);
        return bookingClient.getBookerStatistics(bookerId, bookingState.name(), from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerStatistics(@RequestHeader(USER_ID_HEADER) long ownerId,
                                                     @RequestParam(value = "state", required = false, defaultValue =
                                                             "ALL") String state,
                                                     @RequestParam(value = "from", required = false, defaultValue =
                                                             "0") Integer from,
                                                     @RequestParam(value = "size", required = false, defaultValue =
                                                             "20") Integer size) {

        BookingState bookingState =
                BookingState.getNameByStringName(state).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state)
        );
        log.info("Get owner statistics, state={}, from={}, size={}", state, from, size);
        return bookingClient.getOwnerStatistics(ownerId, bookingState.name(), from, size);
    }


}
