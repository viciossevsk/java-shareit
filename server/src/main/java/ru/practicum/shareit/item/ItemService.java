package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(Long itemId, Long ownerId);

    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);

    List<ItemDto> getAllItemsByOwner(Long ownerId, Integer from, Integer size);

    void deleteItem(Long itemId);

    List<ItemDto> searchItemByText(String text, Long userId, Integer from, Integer size);

    CommentDto createComment(Long itemId, Long bookerId, CommentDto commentDto);
}