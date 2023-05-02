package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                    @Valid @RequestBody BookingDto bookingDto) {

        return bookingService.createBooking(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approvingBooking(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                       @PathVariable Long bookingId,
                                       @RequestParam(name = "approved") boolean isApproved) {

        return bookingService.approvingBooking(ownerId, bookingId, isApproved);

    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                 @PathVariable Long bookingId) {

        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookerStatistics(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                                @RequestParam(required = false, defaultValue = "ALL") String state,
                                                @RequestParam(required = false, defaultValue = "0") Integer start,
                                                @RequestParam(required = false, defaultValue = "20") Integer size) {

        return bookingService.getBookerStatistics(bookerId, state, start, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerStatistics(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                               @RequestParam(required = false, defaultValue = "ALL") String state,
                                               @RequestParam(required = false, defaultValue = "0") Integer start,
                                               @RequestParam(required = false, defaultValue = "20") Integer size) {

        return bookingService.getOwnerStatistics(ownerId, state, start, size);
    }


}
