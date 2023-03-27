package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto getItemById(Integer itemId) {
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    public ItemDto createItem(ItemDto itemDto, Integer userId) {
        User user = userRepository.getUserById(userId);
        validate(itemDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.createItem(item));
    }

    public ItemDto updateItem(ItemDto itemDto, Integer itemId, Integer userId) {
        User user = userRepository.getUserById(userId);
        Item itemInMap = itemRepository.getItemById(itemId);
        if (!user.equals(itemInMap.getOwner())) {
            throw new ItemNotFoundException(String.format("The item id %s has a different owner", itemId));
        }
        if (itemDto.getName() != null) {
            itemInMap.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemInMap.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemInMap.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.updateItem(itemInMap, itemId));
    }

    public ItemDto getItem(Integer itemId, Integer userId) {
        User user = userRepository.getUserById(userId);
        Item itemInMap = itemRepository.getItemById(itemId);
        if (!user.equals(itemInMap.getOwner())) {
            throw new ItemNotFoundException(String.format("The item id %s has a different owner", itemId));
        }
        return ItemMapper.toItemDto(itemInMap);
    }

    public List<ItemDto> getAllItems() {
        return itemRepository.getAllItems().stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public List<ItemDto> getAllItemsByUser(Integer userId) {
        return itemRepository.getAllItems().stream().filter(item -> item.getOwner().getId() == userId).map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public void deleteItem(Integer userId, Integer itemId) {
        itemRepository.deleteItem(userId, itemId);
    }

    public List<ItemDto> searchItemByName(Integer userId, String text) {
        userRepository.checkUserExist(userId);
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.getAllItems().stream().filter(item -> isTextContained(text, item)).map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private boolean isTextContained(String text, Item item) {
        boolean isNameContainsText = item.getName().toLowerCase().contains(text.strip().toLowerCase());
        boolean isDescriptionContainsText = item.getDescription().toLowerCase().contains(text.strip().toLowerCase());

        return item.getAvailable() && (isDescriptionContainsText || isNameContainsText);
    }

    private void validate(ItemDto itemDto) {
        if ((itemDto.getName() == null) || (itemDto.getName().isEmpty())) {
            throw new ValidationException("Item name invalid");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Item available invalid");
        }
        if ((itemDto.getDescription() == null) || (itemDto.getDescription().isEmpty())) {
            throw new ValidationException("Item Description invalid");
        }
    }

}
