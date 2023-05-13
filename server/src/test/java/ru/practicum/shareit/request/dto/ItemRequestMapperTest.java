package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(value = "/testSchema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestMapperTest {
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private User booker;
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemRequest = itemRequestRepository.findById(1L).get();

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Item description");

        User owner = userRepository.findById(3L).get();

        booker = userRepository.findById(2L).get();

        item = itemRepository.findById(4L).get();
        item.setOwner(owner);
        item.setComments(Set.of());

        itemDto = new ItemDto();
        itemDto.setId(4L);
        itemDto.setName("Item4");
        itemDto.setDescription("Item4 Description search");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);
        itemDto.setComments(Set.of());
    }

    @Test
    void mapTest_mapsItemRequestToItemRequestDto() {
        ItemRequestWithItemsDto actual = itemRequestMapper.toItemRequestWithItemsDto(null, itemRequest);

        assertEquals(1, actual.getId());
        assertEquals("description", actual.getDescription());
        assertEquals(LocalDateTime.parse("2023-06-10T12:00:00", formatter), actual.getCreated());
        assertNull(actual.getItems());
    }

    @Test
    void mapTest_whenItemRequestIsNull_returnsItemRequestDtoNull() {
        ItemRequestDto actual = itemRequestMapper.toItemRequestDto(null);

        assertNull(actual);
    }

    @Test
    void testMerge_mergesItemRequestDtoAndUserToItemRequest() {
        ItemRequest actual = itemRequestMapper.toItemRequest(itemRequestDto);

        assertNull(actual.getId());
        assertEquals("Item description", actual.getDescription());
        assertNull(actual.getCreated());
        assertNull(actual.getRequestor());
    }

    @Test
    void mergeTest_whenItemRequestDtoAndUserAreNull_returnsItemRequestNull() {
        ItemRequest actual = itemRequestMapper.toItemRequest(null);

        assertNull(actual);
    }

    @Test
    void mergeTest_mergesItemRequestAndItemsToItemRequestDto() {
        ItemRequestWithItemsDto actual = itemRequestMapper.toItemRequestWithItemsDto(List.of(itemDto), itemRequest);

        assertEquals(1, actual.getId());
        assertEquals("description", actual.getDescription());
        assertEquals(LocalDateTime.parse("2023-06-10T12:00:00", formatter), actual.getCreated());
        assertEquals(List.of(itemDto), actual.getItems());
    }

    @Test
    void mergeTest_whenItemRequestAndListOfItemsAreNull_returnsItemRequestDtoNull() {
        ItemRequestWithItemsDto actual = itemRequestMapper.toItemRequestWithItemsDto(List.of(itemDto), null);

        assertNull(actual.getId());
        assertNull(actual.getDescription());
        assertNull(actual.getCreated());
        assertEquals(List.of(itemDto), actual.getItems());
    }


}