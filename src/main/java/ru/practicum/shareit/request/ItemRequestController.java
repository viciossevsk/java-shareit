package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    ItemRequestService itemRequestService;
    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createItemRequest(ownerId, itemRequestDto);
    }
    @GetMapping
    public List<ItemRequestDto> getAllItemRequestByOwner(@RequestHeader(USER_ID_HEADER) Long ownerId) {
        return itemRequestService.getAllItemRequestByOwner(ownerId);
    }
    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequestOtherOwner(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                                      @RequestParam(value = "from", required = false, defaultValue =
                                                              "0") Integer indexElement,
                                                      @RequestParam(value = "size", required = false, defaultValue =
                                                              "20") Integer sizeElements
    ) {
        return itemRequestService.getItemRequestOtherOwner(ownerId, indexElement, sizeElements);
    }
    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable("requestId") Long itemRequestId) {
        return itemRequestService.getItemRequestById(itemRequestId);
    }
}
