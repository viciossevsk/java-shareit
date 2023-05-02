package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = User.class)
public interface ItemRequestMapper {
    @Mapping(target = "items", ignore = true)
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);
    ItemRequestDto toItemRequestWithItemsDto(List<ItemDto> items, ItemRequest itemRequest);
    @Mapping(target = "requestor", ignore = true)
   ItemRequest toItemRequest(ItemRequestDto itemRequestDto);
}