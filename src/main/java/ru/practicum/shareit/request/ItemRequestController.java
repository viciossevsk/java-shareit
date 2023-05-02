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
    public ItemRequestDto createItemRequest(@RequestHeader(USER_ID_HEADER) Long requestorId,
                                            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createItemRequest(requestorId, itemRequestDto);
    }
    @GetMapping
    public List<ItemRequestDto> getAllItemRequestByRequestor(@RequestHeader(USER_ID_HEADER) Long requestorId) {
        return itemRequestService.getAllItemRequestByOwner(requestorId);
    }
    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequestOtherRequestor(@RequestHeader(USER_ID_HEADER) Long requestorId,
                                                      @RequestParam(value = "from", required = false, defaultValue =
                                                              "0") Integer start,
                                                      @RequestParam(value = "size", required = false, defaultValue =
                                                              "20") Integer size
    ) {
        return itemRequestService.getItemRequestOtherRequestor(requestorId, start, size);
    }
    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable("requestId") Long itemRequestId) {
        return itemRequestService.getItemRequestById(itemRequestId);
    }
}
