package ru.practicum.shareit.item.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@Sql(value = "/testSchema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {
    private final ItemRepository mockItemRepository;
    private final ItemServiceImpl mockItemServiceImpl;
    long itemId = 1L;
    long ownerId = 1L;
    int start = 0;
    int size = 10;

    @Test
    void findNextBookings_Test() {

        mockItemRepository.deleteById(itemId);

            List<ItemDto> itemDtos = mockItemServiceImpl.getAllItemsByOwner(ownerId, start, size);
            assertEquals(0, itemDtos.size());
    }
}
