package ru.practicum.shareit.item.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = "/testSchema.sql")
public class ItemServiceImplTest {
    private final ItemServiceImpl mositemServiceImpl;
    ItemDto requstItemDto2;
    ItemDto responseItemDto2;
    ItemDto responseItemDto3;
    CommentDto commentDto;
    Long itemId = 2L;
    Long ownerId = 1L;
    int start = 0;
    int size = 10;

    @BeforeEach
    void setUp() {
        requstItemDto2 = ItemDto.builder()
                .id(2L)
                .name("Item2 update")
                .description("Description")
                .available(true)
                .comments(Set.of())
                .build();
        responseItemDto2 = ItemDto.builder()
                .id(2L)
                .name("Item2")
                .description("Description")
                .available(true)
                .comments(Set.of())
                .build();

        responseItemDto3 = ItemDto.builder()
                .id(3L)
                .name("Item3")
                .description("Description")
                .available(true)
                .comments(Set.of())
                .build();

        commentDto = CommentDto.builder()
                .text("text_comment")
                .build();
    }

    @Test
    void getItemById_checkData() {

        ItemDto itemDto = mositemServiceImpl.getItemById(itemId, ownerId);

        assertEquals(responseItemDto2.toString(), itemDto.toString());

    }

    @Test
    void updateItem_checkData() {

        ItemDto itemDto = mositemServiceImpl.updateItem(requstItemDto2, itemId, ownerId);

        assertEquals(requstItemDto2.toString(), itemDto.toString());

    }

    @Test
    void getAllItemsByOwner_checkData() {

        List<ItemDto> itemDtos = mositemServiceImpl.getAllItemsByOwner(itemId, start, size);

        assertEquals(2, itemDtos.size());
    }

    @Test
    void deleteItem_checkData() {
        long itemId = 2L;
        int listSize;

        mositemServiceImpl.deleteItem(itemId, itemId);

        List<ItemDto> itemDtos = mositemServiceImpl.getAllItemsByOwner(itemId, start, size);

        listSize = itemDtos.size();

        assertEquals(1, listSize);

    }

    @Test
    void searchItemByText_checkData() {

        List<ItemDto> itemDtos = mositemServiceImpl.searchItemByText("Item2", start, size);

        assertEquals(1, itemDtos.size());

    }
}
