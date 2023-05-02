package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;
    @Override
    @Transactional
    public ItemRequestDto createItemRequest(Long requestorId, ItemRequestDto itemRequestDto) {
        User owner = getUserById(requestorId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(owner);
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getAllItemRequestByOwner(Long requestorId) {
        User Requestor = getUserById(requestorId);

        Map<Long, ItemRequest> requests = itemRequestRepository.findByRequestorId(Requestor.getId()).stream()
                .collect(Collectors.toMap(ItemRequest::getId, Function.identity()));

    return getItemRequestsDto(requests);
    }

    private List<ItemRequestDto> getItemRequestsDto(Map<Long, ItemRequest> requests) {
        Map<Long, List<Item>> items = itemRepository.findAllItemsByRequestIds(requests.keySet()).stream()
                .collect(Collectors.groupingBy(Item::getRequestId));

        return requests.values().stream()
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .map(itemRequest -> itemRequestMapper.toItemRequestWithItemsDto((List<ItemDto>) itemMapper.toItemDto((Item) items.getOrDefault(itemRequest.getId(), List.of())), itemRequest))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getItemRequestOtherRequestor(Long requestorId, Integer start, Integer size) {
        if (start < 0 || size <= 0) {
            throw new ValidationException("Mistaken item request parameters");
        }
        validate(requestorId);
        PageRequest page = PageRequest.of(start > 0 ? start / size : 0, size);

        Map<Long, ItemRequest> requests = itemRequestRepository.findAllExceptUserId(requestorId, page).stream()
                .collect(Collectors.toMap(ItemRequest::getId, Function.identity()));

        return getItemRequestsDto(requests);
    }

    private void validate(Long RequestorId) {
        userRepository.findById(RequestorId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_USER_ID, RequestorId)));
        }

    @Override
    public ItemRequestDto getItemRequestById(Long itemRequestId) {
        ItemRequest itemRequest = getItemRequestByItemRequestId(itemRequestId);
        List<Item> items = itemRepository.findAllItemsByRequestIds(Set.of(itemRequest.getId()));
        return itemRequestMapper.toItemRequestWithItemsDto((List<ItemDto>) itemMapper.toItemDto((Item) items), itemRequest);
    }

    private ItemRequest getItemRequestByItemRequestId(Long itemRequestId) {
        return itemRequestRepository.findById(itemRequestId).orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_ITEM_REQUEST_ID, itemRequestId)));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_USER_ID, userId)));
    }

}
