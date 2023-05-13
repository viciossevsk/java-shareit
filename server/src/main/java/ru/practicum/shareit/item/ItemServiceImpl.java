package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final CommentMapper commentMapper;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        validate(itemDto);
        User owner = getUserById(ownerId);
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public CommentDto createComment(Long itemId, Long bookerId, CommentDto commentDto) {
        User booker = getUserById(bookerId);

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                                                                        new EntityNotFoundException(String.format(MISTAKEN_ITEM_ID, itemId)));
        boolean canComment = booker.getBookings().stream()
                .anyMatch(booking ->
                                  booking.getItem().equals(item)
                                          && booking.getStatus().equals(BookingStatus.APPROVED)
                                          && booking.getStart().isBefore(LocalDateTime.now()));

        if (!canComment) {
            throw new ValidationException(String.format(MISTAKEN_USER_ID + " can't comment", bookerId));
        }
        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .authorName(booker.getName())
                .created(LocalDateTime.now())
                .item(item)
                .booker(booker)
                .build();
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(Long itemId, Long ownerId) {
        Item item = getItemByItemId(itemId);

        ItemDto itemDto = itemMapper.toItemDto(item);
        itemDto.setComments(commentRepository.findCommentDtosByItem(item));

        if (item.getOwner().getId().equals(ownerId)) {
            itemDto.setNextBooking(itemRepository.findNextBookingsOfItem(item).stream().findFirst().orElse(null));
            itemDto.setLastBooking(itemRepository.findLastBookingsOfItem(item).stream().findFirst().orElse(null));
        }
        return itemDto;
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        User owner = getUserById(ownerId);

        if (!owner.getId().equals(ownerId)) {
            throw new EntityNotFoundException(String.format(MISTAKEN_OWNER_ID, ownerId));
        }

        Item item = getItemByItemId(itemId);
        item.setOwner(owner);

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        itemDto = itemMapper.toItemDto(itemRepository.save(item));

        itemDto.setComments(commentRepository.findCommentDtosByItem(item));
        itemDto.setNextBooking(itemRepository.findNextBookingsOfItem(item).stream().findFirst().orElse(null));
        itemDto.setLastBooking(itemRepository.findLastBookingsOfItem(item).stream().findFirst().orElse(null));

        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItemsByOwner(Long ownerId, Integer start, Integer size) {
        PageRequest page = getPage(start, size);

        Map<Long, Item> items = itemRepository.findAllByOwnerId(ownerId, page).stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));

        Map<Long, List<CommentDto>> commentDtos = commentRepository.findCommentDtosByItems(items.values()).stream()
                .collect(Collectors.groupingBy(CommentDto::getItemId));

        Collection<BookingShortDto> nextBookings = itemRepository.findNextBookings(items.values());
        Collection<BookingShortDto> lastBookings = itemRepository.findLastBookings(items.values());

        return items.values().stream()
                .sorted(Comparator.comparing(Item::getId))
                .map(item -> {
                    ItemDto itemDto = itemMapper.toItemDto(item);
                    itemDto.setNextBooking(nextBookings.stream()
                                                   .filter(bookingShort -> bookingShort.getItemId().equals(itemDto.getId()))
                                                   .findFirst()
                                                   .orElse(null));
                    itemDto.setLastBooking(lastBookings.stream()
                                                   .filter(bookingShort -> bookingShort.getItemId().equals(itemDto.getId()))
                                                   .findFirst()
                                                   .orElse(null));
                    if (commentDtos.size() > 0) {
                        itemDto.setComments(commentDtos.get(itemDto.getId()).stream().collect(Collectors.toSet()));
                    }
                    return itemDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchItemByText(String text, Long userId, Integer start, Integer size) {
        if (isUserExistsById(userId)) {
            throw new EntityNotFoundException(String.format(MISTAKEN_USER_ID, userId));
        }

        if (text == null || text.isBlank()) {
            return List.of();
        }
        final String query = "%" + text + "%";

        PageRequest page = getPage(start, size);

        return itemRepository.findAllByNameIsLikeIgnoreCaseOrDescriptionIsLikeIgnoreCaseAndAvailableTrue(query, query, page)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void validate(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new ValidationException("Item name invalid");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Item available invalid");
        }
        if ((itemDto.getDescription() == null) || (itemDto.getDescription().isEmpty())) {
            throw new ValidationException("Item Description invalid");
        }
    }

    private Item getItemByItemId(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_ITEM_ID, itemId)));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_USER_ID, userId)));
    }

    private boolean isUserExistsById(Long userId) {
        return !userRepository.existsById(userId);
    }

}
