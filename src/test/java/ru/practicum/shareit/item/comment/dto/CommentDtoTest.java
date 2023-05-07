package ru.practicum.shareit.item.comment.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentDtoTest {
    private final CommentDto expected = new CommentDto();
    private final CommentDto actual = new CommentDto();

    @Test
    void testEquals() {
        assertEquals(expected, actual);
    }

    @Test
    void testHashCode() {
        assertEquals(expected.hashCode(), actual.hashCode());
    }

}