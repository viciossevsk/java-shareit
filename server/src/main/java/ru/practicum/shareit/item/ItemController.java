package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader(USER_ID_HEADER) Long ownerId) {
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable("id") Long itemId,
                              @RequestHeader(USER_ID_HEADER) Long ownerId) {
        return itemService.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@RequestHeader(USER_ID_HEADER) Long ownerId, @PathVariable("id") Long itemId) {
        return itemService.getItemById(itemId, ownerId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@RequestHeader(USER_ID_HEADER) Long ownerId, @RequestParam(required =
            false, defaultValue = "0") Integer from,
                                            @RequestParam(required = false, defaultValue = "20") Integer size) {
        return itemService.getAllItemsByOwner(ownerId, from, size);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") Long itemId) {
        itemService.deleteItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @RequestParam String text, @RequestParam(required = false, defaultValue =
            "0") Integer from, @RequestParam(required = false, defaultValue = "20") Integer size) {
        return itemService.searchItemByText(text, userId, from, size);
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                    @PathVariable(value = "id") Long itemId,
                                    @RequestBody CommentDto commentDto) {
        return itemService.createComment(itemId, bookerId, commentDto);
    }

}
