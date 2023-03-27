package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.stringToGreenColor;

@Component
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();
    private int generatorId = 0;

    @Override
    public Item createItem(Item item) {
        item.setId(++generatorId);
        items.put(generatorId, item);
        log.info(stringToGreenColor("create item..." + item));
        return item;
    }

    @Override
    public Item updateItem(Item item, Integer itemId) {
        items.replace(itemId, item);
        log.info(stringToGreenColor("update item..." + item));
        return item;
    }

    @Override
    public Item getItemById(Integer itemId) {
        if (itemId != null) {
            return Optional.of(items.get(itemId)).orElseThrow(() -> new UserNotFoundException(String.format("item with id=%s", itemId)));
        } else {
            throw new UserNotFoundException("item id not found");
        }
    }

    @Override
    public List<Item> getAllItems() {
        log.info(stringToGreenColor("get all items..."));
        return new ArrayList<>(items.values());
    }

    @Override
    public void deleteItem(Integer userId, Integer itemId) {
        log.info(stringToGreenColor("delete item..."));
        items.remove(itemId);
    }

    @Override
    public List<ItemDto> searchItemByName(Integer userId, String name) {
        return null;
    }
}
