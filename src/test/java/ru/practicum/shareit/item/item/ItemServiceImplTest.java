package ru.practicum.shareit.item.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository mockItemRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private CommentRepository mockCommentRepository;
    @Mock
    private CommentMapper mockCommentMapper;
    @Mock
    private ItemMapper mockItemMapper;
    @InjectMocks
    private ItemServiceImpl itemService;
    private User user;
    private ItemDto requestItemDto;
    private ItemDto responseItemDto;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        User owner = new User();
        owner.setId(2L);

        item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(owner);

        booking = new Booking();
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setItem(item);
        booking.setBooker(user);

        requestItemDto = ItemDto.builder()
                .name("name")
                .available(true)
                .description("Description")
                .build();

        responseItemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .available(true)
                .description("Description")
                .build();
    }

    @Test
    void createTest_whenItemNameInvalid_thenCreateItemAndReturnItemDto() {
        assertThrows(ValidationException.class, () -> itemService.createItem(new ItemDto(), 1L));
        verify(mockItemRepository, Mockito.never()).save(any(Item.class));
    }

    @Test
    void createTest_whenOwnerNotFound_thenEntityNotFoundExceptionThrown() {
        when(mockUserRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.createItem(requestItemDto, 1L));
        verify(mockItemRepository, never()).save(any(Item.class));
    }

    @Test
    void readTest_whenItemOfOwnerFound_thenReturnItemDtoForOwner() {
        long itemId = 1L;
        long ownerId = 2L;
        when(mockItemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(mockItemMapper.toItemDto(any(Item.class))).thenReturn(requestItemDto);

        ItemDto itemDto = itemService.getItemById(itemId, ownerId);

        assertEquals(requestItemDto, itemDto);
        verify(mockCommentRepository, times(1)).findCommentDtosByItem(any(Item.class));
    }

    @Test
    void readTest_whenItemNotFound_thenEntityNotFoundExceptionThrown() {
        long itemId = 1L;
        long userId = 1L;
        when(mockItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(itemId, userId));
        verify(mockItemMapper, never()).toItemDto(any(Item.class));
    }

    @Test
    void updateTest_whenItemFound_AndUserIsOwner_thenUpdateItemAndReturnItemDtoToOwner() {
        long itemId = 1L;
        long ownerId = 2L;
        when(mockItemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(mockItemRepository.save(any(Item.class))).thenReturn(item);
        when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mockItemMapper.toItemDto(any(Item.class))).thenReturn(responseItemDto);
        when(mockCommentRepository.findCommentDtosByItem(any(Item.class))).thenReturn(Set.of());

        ItemDto itemDtoActual = itemService.updateItem(requestItemDto, ownerId, itemId);

        assertEquals(responseItemDto, itemDtoActual);
    }

    @Test
    void updateTest_whenItemFound_ButUserIsNotOwner_thenEntityNotFoundExceptionThrown() {
        long itemId = 1L;
        long userId = 1L;

        assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(new ItemDto(), userId, itemId));
        verify(mockItemRepository, never()).save(any(Item.class));
    }


    @Test
    void deleteTest_whenInvoke_thenCheckInvocationOfIt() {
        long itemId = 1L;
        long ownerId = 1L;

        itemService.deleteItem(ownerId, itemId);

        verify(mockItemRepository).deleteByIdAndAndOwnerId(ownerId, itemId);
    }

    @Test
    void findAllItemsOfOwnerTest_whenInvoke_thenReturnSize0() {
        int from = 0;
        int size = 20;
        long ownerId = 2L;
        PageRequest page = PageRequest.of(from, size);

        when(mockItemRepository.findNextBookings(any())).thenReturn(List.of(new BookingShortDto()));
        when(mockItemRepository.findLastBookings(any())).thenReturn(List.of(new BookingShortDto()));

        List<ItemDto> itemDtos = itemService.getAllItemsByOwner(ownerId, from, size);

        assertEquals(0, itemDtos.size());
    }

    @Test
    void searchText_whenQueryEmpty_thenReturnEmptyCollection() {
        int from = 0;
        int size = 20;
        String query = "";
        PageRequest page = PageRequest.of(from, size);

        List<ItemDto> itemDtosActual = itemService.searchItemByText(query, from, size);

        assertEquals(List.of(), itemDtosActual);
        verify(mockItemRepository, never()).findAllByNameIsLikeIgnoreCaseOrDescriptionIsLikeIgnoreCaseAndAvailableTrue(query, query, page);
    }

    @Test
    void createCommentTest_whenUserCantComment_thenValidationExceptionThrown() {
        long itemId = 1L;
        long userId = 1L;
        user.setBookings(Set.of(booking));
        booking.setStatus(BookingStatus.REJECTED);
        when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mockItemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> itemService.createComment(itemId, userId, any(CommentDto
                                                                                                            .class)));
        verify(mockCommentRepository, never()).save(any(Comment.class));
    }

    @Test
    void createCommentTest_whenItemNotFound_thenEntityNotFoundExceptionThrown() {
        long itemId = 1L;
        long userId = 1L;
        when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mockItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.createComment(itemId, userId, any(CommentDto
                                                                                                                .class)));
        verify(mockCommentRepository, never()).save(any(Comment.class));
    }

    @Test
    void createCommentTest_whenUserNotFound_thenEntityNotFoundExceptionThrown() {
        long itemId = 1L;
        long userId = 1L;
        when(mockUserRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.createComment(itemId, userId, any(CommentDto
                                                                                                                .class)));
        verify(mockCommentRepository, never()).save(any(Comment.class));
    }


}