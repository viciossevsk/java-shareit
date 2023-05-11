package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(USER_ID_HEADER) Long requestorId,
                                            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createItemRequest(requestorId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestWithItemsDto> getAllItemRequestByRequestor(@RequestHeader(USER_ID_HEADER) Long requestorId) {
        return itemRequestService.getAllItemRequestByOwner(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestWithItemsDto> getItemRequestOtherRequestor(@RequestHeader(USER_ID_HEADER) Long requestorId,
                                                                      @RequestParam(value = "from", required = false,
                                                                              defaultValue =
                                                                                      "0") Integer start,
                                                                      @RequestParam(value = "size", required = false,
                                                                              defaultValue =
                                                                                      "20") Integer size
    ) {
        return itemRequestService.getItemRequestOtherRequestor(requestorId, start, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItemsDto getItemRequestById(@RequestHeader(USER_ID_HEADER) Long requestorId,
                                                      @PathVariable("requestId") Long itemRequestId) {
        return itemRequestService.getItemRequestById(itemRequestId, requestorId);
    }
}
