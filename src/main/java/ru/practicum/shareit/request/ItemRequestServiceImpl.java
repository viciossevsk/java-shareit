package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestWithItemsDto> getAllItemRequestByOwner(Long requestorId) {
        if (isUserExistsById(requestorId)) {
            throw new EntityNotFoundException(String.format(MISTAKEN_USER_ID, requestorId));
        }
        Map<Long, ItemRequest> requests = itemRequestRepository.findByRequestorId(requestorId).stream()
                .collect(Collectors.toMap(ItemRequest::getId, Function.identity()));
        return getItemRequestsWithItemsDto(requests);
    }

    @Override
    public List<ItemRequestWithItemsDto> getItemRequestOtherRequestor(Long requestorId, Integer start, Integer size) {
        if (isUserExistsById(requestorId)) {
            throw new EntityNotFoundException(String.format(MISTAKEN_USER_ID, requestorId));
        }
        PageRequest page = getPage(start, size);

        Map<Long, ItemRequest> requests = itemRequestRepository.findAllExceptUserId(requestorId, page).stream()
                .collect(Collectors.toMap(ItemRequest::getId, Function.identity()));

        return getItemRequestsWithItemsDto(requests);
    }

    @Override
    public ItemRequestWithItemsDto getItemRequestById(Long itemRequestId, Long requestorId) {
        if (isUserExistsById(requestorId)) {
            throw new EntityNotFoundException(String.format(MISTAKEN_USER_ID, requestorId));
        }
        ItemRequest itemRequest = getItemRequestByItemRequestId(itemRequestId);
        List<Item> items = itemRepository.findAllItemsByRequestIds(Set.of(itemRequest.getId()));
        return itemRequestMapper.toItemRequestWithItemsDto(itemsToItemsDTO(items), itemRequest);
    }

    private ItemRequest getItemRequestByItemRequestId(Long itemRequestId) {
        return itemRequestRepository.findById(itemRequestId).orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_ITEM_REQUEST_ID, itemRequestId)));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(MISTAKEN_USER_ID, userId)));
    }

    private List<ItemRequestWithItemsDto> getItemRequestsWithItemsDto(Map<Long, ItemRequest> requests) {
        Map<Long, List<Item>> items = itemRepository.findAllItemsByRequestIds(requests.keySet()).stream()
                .collect(Collectors.groupingBy(Item::getRequestId));

        return requests.values().stream()
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .map(itemRequest -> itemRequestMapper.toItemRequestWithItemsDto(itemsToItemsDTO(items.getOrDefault(itemRequest.getId(), List.of())), itemRequest)).collect(Collectors.toList());
    }

    private List<ItemDto> itemsToItemsDTO(List<Item> items) {
        return items.stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    private boolean isUserExistsById(Long userId) {
        return (!userRepository.existsById(userId));
    }

}
