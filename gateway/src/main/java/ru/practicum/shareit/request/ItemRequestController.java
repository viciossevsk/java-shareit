package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(USER_ID_HEADER) long requestorId,
                                                    @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("createItemRequest {}, requestorId={}", itemRequestDto, requestorId);
        return itemRequestClient.createItemRequest(requestorId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestByRequestor(@RequestHeader(USER_ID_HEADER) long requestorId) {
        log.info("getAllItemRequestByRequestor, ownerId={}", requestorId);
        return itemRequestClient.getAllItemRequestByOwner(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestOtherRequestor(@RequestHeader(USER_ID_HEADER) long requestorId,
                                                                      @RequestParam(value = "from", required = false,
                                                                              defaultValue =
                                                                                      "0") Integer from,
                                                                      @RequestParam(value = "size", required = false,
                                                                              defaultValue =
                                                                                      "20") Integer size
    ) {
        log.info("getItemRequestOtherRequestor, requestorId={}, from={}, size={}", requestorId, from, size);
        return itemRequestClient.getItemRequestOtherRequestor(requestorId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(USER_ID_HEADER) long requestorId,
                                                      @PathVariable("requestId") long itemRequestId) {
        log.info("getItemRequestById {}, requestorId={}, itemRequestId={}", requestorId, itemRequestId);
        return itemRequestClient.getItemRequestById(itemRequestId, requestorId);
    }
}
