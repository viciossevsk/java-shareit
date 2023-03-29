package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

//    @Autowired
//    public ItemController(ItemService itemService) {
//        this.itemService = itemService;
//    }

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(USER_ID_HEADER) Integer userId) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@Valid @RequestBody ItemDto itemDto, @PathVariable("id") Integer itemId,
                              @RequestHeader(USER_ID_HEADER) Integer userId) {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable("id") Integer itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUser(@RequestHeader(USER_ID_HEADER) Integer userId) {
        return itemService.getAllItemsByUser(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@RequestHeader(USER_ID_HEADER) Integer userId, @PathVariable("id") Integer itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestHeader(USER_ID_HEADER) Integer userId, @RequestParam String text) {
        return itemService.searchItemByText(userId, text);
    }

}
