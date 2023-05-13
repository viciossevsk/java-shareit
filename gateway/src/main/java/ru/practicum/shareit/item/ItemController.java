package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(USER_ID_HEADER) long ownerId) {
        log.info("createItem {}, ownerId={}", itemDto, ownerId);
        return itemClient.createItem(ownerId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@Valid @RequestBody ItemDto itemDto, @PathVariable("id") long itemId,
                                             @RequestHeader(USER_ID_HEADER) long ownerId) {
        log.info("updateItem, itemId={} ownerId={}", itemId, ownerId);
        return itemClient.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@RequestHeader(USER_ID_HEADER) long ownerId, @PathVariable("id") long itemId) {
        log.info("getItemById, itemId={} ownerId={}", itemId, ownerId);
        return itemClient.getItemById(itemId, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByOwner(@RequestHeader(USER_ID_HEADER) long ownerId,
                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                            @RequestParam(required = false, defaultValue = "20") Integer size) {
        log.info("getAllItemsByOwner, ownerId={}, from={}, size={}", ownerId, from, size);
        return itemClient.getAllItemsByOwner(ownerId, from, size);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable("id") long itemId) {
        log.info("deleteItem, itemId={}", itemId);
        itemClient.deleteItem(itemId);
        return ResponseEntity.ok("Item is deleted");
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestHeader(USER_ID_HEADER) Long userId,
                                                   @RequestParam String text,
                                                   @RequestParam(required = false, defaultValue = "0") Integer from,
                                                   @RequestParam(required = false, defaultValue = "20") Integer size) {
        log.info("searchItemByText, text={}, from={}, size={}", text, from, size);
        return itemClient.searchItemByText(text, userId, from, size);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(USER_ID_HEADER) long bookerId,
                                    @PathVariable(value = "id") long itemId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("createComment {}, bookerId={} itemId={}", commentDto, bookerId, itemId);
        return itemClient.createComment(itemId, bookerId, commentDto);
    }

}
