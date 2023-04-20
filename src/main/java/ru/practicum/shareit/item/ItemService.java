package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;


    public ItemDto getItemById(Integer itemId) {
        return itemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    public ItemDto createItem(ItemDto itemDto, Integer userId) {
        User user = userRepository.getUserById(userId);
        validate(itemDto);
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(user);
        return itemMapper.toItemDto(itemRepository.createItem(item));
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
        return itemMapper.toItemDto(itemRepository.updateItem(itemInMap, itemId));
    }

    public List<ItemDto> getAllItemsByUser(Integer userId) {
        return itemRepository.getAllItems().stream().filter(item -> item.getOwner().getId().equals(userId)).map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    public void deleteItem(Integer userId, Integer itemId) {
        itemRepository.deleteItem(userId, itemId);
    }

    public List<ItemDto> searchItemByText(Integer userId, String text) {
        User user = userRepository.getUserById(userId);
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.getAllItems().stream().filter(item -> isTextContained(text, item)).map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    private boolean isTextContained(String text, Item item) {
        boolean isNameContainsText = item.getName().toLowerCase().contains(text.strip().toLowerCase());
        boolean isDescriptionContainsText = item.getDescription().toLowerCase().contains(text.strip().toLowerCase());

        return item.getAvailable() && (isDescriptionContainsText || isNameContainsText);
    }

    private void validate(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
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
