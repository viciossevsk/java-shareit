package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(Long requestorId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getAllItemRequestByOwner(Long requestorId);

    List<ItemRequestDto> getItemRequestOtherRequestor(Long requestorId, Integer start, Integer size);

    ItemRequestDto getItemRequestById(Long itemRequestId);
}
