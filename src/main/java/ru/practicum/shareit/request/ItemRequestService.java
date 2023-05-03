package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(Long requestorId, ItemRequestDto itemRequestDto);

    List<ItemRequestWithItemsDto> getAllItemRequestByOwner(Long requestorId);

    List<ItemRequestWithItemsDto> getItemRequestOtherRequestor(Long requestorId, Integer start, Integer size);

    ItemRequestWithItemsDto getItemRequestById(Long itemRequestId, Long requestorId);
}
