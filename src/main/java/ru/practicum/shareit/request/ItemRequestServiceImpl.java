package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Set;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    @Override
    @Transactional
    public ItemRequestDto createItemRequest(Long ownerId, ItemRequestDto itemRequestDto) {
        User owner = getUserById(ownerId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(owner);
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getAllItemRequestByOwner(Long ownerId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getItemRequestOtherOwner(Long ownerId, Integer indexElement, Integer sizeElements) {
        return null;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long itemRequestId) {
        ItemRequest itemRequest = getItemRequestByItemRequestId(itemRequestId);
        List<Item> items = itemRepository.findAllItemsByRequestIds(Set.of(itemRequest.getId()));
        return itemRequestMapper.toItemRequestWithItemsDto(items, itemRequest);
    }

    private ItemRequest getItemRequestByItemRequestId(Long itemRequestId) {
        return itemRequestRepository.findById(itemRequestId).orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_ITEM_REQUEST_ID, itemRequestId)));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_USER_ID, userId)));
    }

}
