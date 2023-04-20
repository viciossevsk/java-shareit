package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(Item item);

    Item updateItem(Item item, Integer itemId);

    Item getItemById(Integer itemId);

    List<Item> getAllItems();

    void deleteItem(Integer userId, Integer itemId);

    List<ItemDto> searchItemByName(Integer userId, String name);
}
