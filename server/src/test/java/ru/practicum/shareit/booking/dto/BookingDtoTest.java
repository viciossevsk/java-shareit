package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingDtoTest {
    private final BookingDto expected = new BookingDto();
    private final BookingDto actual = new BookingDto();

    @Test
    void testEquals() {
        assertEquals(expected, actual);
    }

}