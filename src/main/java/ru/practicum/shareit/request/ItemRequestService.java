package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(Long ownerId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getAllItemRequestByOwner(Long ownerId);

    List<ItemRequestDto> getItemRequestOtherOwner(Long ownerId, Integer indexElement, Integer sizeElements);

    ItemRequestDto getItemRequestById(Long itemRequestId);
}
