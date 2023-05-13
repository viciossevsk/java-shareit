package ru.practicum.shareit.item.comment.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(value = "/testSchema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentMapperTest {
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private CommentDto commentDto;
    private Comment comment;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        comment = commentRepository.findById(1L).get();

        User owner = userRepository.findById(1L).get();

        booker = userRepository.findById(2L).get();

        item = itemRepository.findById(1L).get();
        item.setOwner(owner);

        commentDto = new CommentDto();
        commentDto.setText("Comment");
    }

    @Test
    void mapTest_mapCommentDtoToComment() {
        Comment actual = commentMapper.toComment(commentDto);

        assertNull(actual.getId());
        assertEquals("Comment", actual.getText());
        assertNull(actual.getAuthorName());
        assertNull(actual.getCreated());
        assertNull(actual.getItem());
        assertNull(actual.getBooker());
    }

    @Test
    void mapTest_whenCommentDtoIsNullReturnCommentNull() {
        Comment actual = commentMapper.toComment((CommentDto) null);

        assertNull(actual);
    }

    @Test
    void testMap_mapCommentToCommentDto() {
        CommentDto actual = commentMapper.toCommentDto(comment);

        assertEquals(1, actual.getId());
        assertEquals("text_comment", actual.getText());
        assertEquals("user_2", actual.getAuthorName());
        assertEquals(LocalDateTime.parse("2023-06-10T12:00:00", formatter), actual.getCreated());
    }

    @Test
    void mapTest_whenCommentIsNullReturnCommentDtoNull() {
        CommentDto actual = commentMapper.toCommentDto((Comment) null);

        assertNull(actual);
    }

    @Test
    void mergeTest_whenBookerAndItemAndCommentDtoAreNullReturnCommentNull() {
        Comment actual = commentMapper.toComment(null);

        assertNull(actual);
    }

}