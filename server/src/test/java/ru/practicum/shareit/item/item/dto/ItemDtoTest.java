package ru.practicum.shareit.item.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemDtoTest {
    private final ItemDto expected = new ItemDto();
    private final ItemDto actual = new ItemDto();

    @Test
    void testEquals() {
        assertEquals(expected, actual);
    }

    @Test
    void testHashCode() {
        assertEquals(expected.hashCode(), actual.hashCode());
    }
}