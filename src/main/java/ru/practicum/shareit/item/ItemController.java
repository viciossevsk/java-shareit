package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(USER_ID_HEADER) Long ownerId) {
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@Valid @RequestBody ItemDto itemDto, @PathVariable("id") Long itemId,
                              @RequestHeader(USER_ID_HEADER) Long ownerId) {
        return itemService.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@RequestHeader(USER_ID_HEADER) Long ownerId,
                               @PathVariable("id") Long itemId) {
        return itemService.getItemById(itemId, ownerId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                            @RequestParam(required = false, defaultValue = "0") Integer start,
                                            @RequestParam(required = false, defaultValue = "20") Integer size) {
        return itemService.getAllItemsByOwner(ownerId, start, size);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@RequestHeader(USER_ID_HEADER) Long ownerId, @PathVariable("id") Long itemId) {
        itemService.deleteItem(ownerId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam String text,
                                          @RequestParam(required = false, defaultValue = "0") Integer start,
                                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        return itemService.searchItemByText(text, start, size);
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                    @PathVariable(value = "id") Long itemId,
                                    @Valid @RequestBody CommentDto commentDto
    ) {
        return itemService.createComment(itemId, bookerId, commentDto);
    }

}
