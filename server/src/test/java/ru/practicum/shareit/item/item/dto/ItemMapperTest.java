package ru.practicum.shareit.item.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(value = "/testSchema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemMapperTest {
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private ItemDto itemDto;
    private Item item;
    private User user;
    private CommentDto commentDto;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {
        user = userRepository.findById(1L).get();

        Comment comment = commentRepository.findById(1L).get();

        item = itemRepository.findById(1L).get();
        item.setOwner(user);
        item.setComments(Set.of(comment));

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Comment");
        commentDto.setAuthorName("user2");
        commentDto.setCreated(LocalDateTime.parse("2023-09-10T12:00:00", formatter));

        itemDto = new ItemDto();
        itemDto.setName("Item1");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
    }

    @Test
    void mergeTest_ItemDtoAndUserToItem() {
        item.setId(null);
        Item actual = itemMapper.toItem(itemDto);

        assertEquals(item, actual);
    }

    @Test
    void mergeTest_whenOwnerAndItemDtoAreNullReturnItemNull() {
        Item actual = itemMapper.toItem(null);

        assertNull(actual);
    }

    @Test
    void testMerge_mergeItemDtoToItemFromDb() {
        itemDto.setId(1L);
        Item actual = itemMapper.toItem(itemDto);

        assertEquals(item, actual);
    }

    @Test
    void mergeTest_whenItemDtoIsNullReturnItemFromDb() {
        Item actual = itemMapper.toItem(null);

        assertEquals(null, actual);
    }

    @Test
    void mergeTest_whenItemDtoIsEmptyReturnItemFromDb() {
        Item actual = itemMapper.toItem(new ItemDto());

        assertNotEquals(new ItemDto(), actual);
    }

    @Test
    void mapOneForOwnerTest_whenItemIsNull_thenReturnNull() {
        ItemDto actual = itemMapper.toItemDto(null);

        assertNull(actual);
    }

    @Test
    void mapForUserTest_mapItemToItemDtoForUser() {
        item.setComments(null);
        ItemDto actual = itemMapper.toItemDto(item);

        assertEquals(1, actual.getId());
        assertEquals("Item1", actual.getName());
        assertEquals("Description", actual.getDescription());
        assertTrue(actual.getAvailable());
        assertNull(actual.getRequestId());
        assertNull(actual.getNextBooking());
        assertNull(actual.getLastBooking());
        assertNull(actual.getComments());
    }

    @Test
    void mapForUserTest_whenItemIsNull_thenReturnNull() {
        ItemDto actual = itemMapper.toItemDto(null);

        assertNull(actual);
    }
}