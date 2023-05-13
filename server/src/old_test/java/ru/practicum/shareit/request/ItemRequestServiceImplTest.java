package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository mockItemRequestRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ItemRequestMapper mockItemRequestMapper;
    @Mock
    private ItemRepository mockItemRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    private ItemRequest itemRequest;
    private User user;
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@mail.com");

        item = new Item();
        item.setId(1L);
        item.setName("Item1");
        item.setDescription("Item1 Description");
        item.setAvailable(true);
        item.setRequestId(1L);

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);

        itemDto = ItemDto.builder()
                .id(1L)
                .build();
    }

    @Test
    void createTest_whenDataValid_thenReturnItemRequestDto() {
        long userId = 1L;
        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockItemRequestMapper.toItemRequest(any(ItemRequestDto.class))).thenReturn(new ItemRequest());
        when(mockItemRequestRepository.save(new ItemRequest())).thenReturn(new ItemRequest());
        when(mockItemRequestMapper.toItemRequestDto(new ItemRequest())).thenReturn(new ItemRequestDto());

        ItemRequestDto itemRequestDto = itemRequestService.createItemRequest(userId, new ItemRequestDto());

        assertEquals(new ItemRequestDto(), itemRequestDto);
        verify(mockItemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    void createTest_whenUserNotFound_thenUserNotFoundExceptionThrown() {
        long userId = 1L;

        assertThrows(UserNotFoundException.class, () -> itemRequestService.createItemRequest(userId,
                                                                                             new ItemRequestDto()));
        verify(mockItemRequestRepository, never()).save(any(ItemRequest.class));
    }

    @Test
    void readTest_whenUserNotFound_thenEntityNotFoundExceptionThrown() {
        long userId = 1L;
        long requestId = 1L;
        when(mockUserRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getItemRequestById(requestId, userId));
        verify(mockItemRequestMapper, never()).toItemRequestWithItemsDto(anyList(), any(ItemRequest.class));
    }

    @Test
    void readTest_whenItemRequestNotFound_thenEntityNotFoundExceptionThrown() {
        long userId = 1L;
        long requestId = 1L;
        when(mockUserRepository.existsById(anyLong())).thenReturn(true);
        when(mockItemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getItemRequestById(requestId, userId));
        verify(mockItemRequestMapper, never()).toItemRequestWithItemsDto(anyList(), any(ItemRequest.class));
    }

    @Test
    void findAllRequestsOfUserTest_whenUserNotFound_thenEntityNotFoundExceptionThrown() {
        long userId = 1L;
        when(mockUserRepository.existsById(userId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getAllItemRequestByOwner(userId));
        verify(mockItemRequestMapper, never()).toItemRequestWithItemsDto(anyList(), eq(itemRequest));
    }

    @Test
    void findAllRequestsOfOthersTest_whenUserNotFound_thenEntityNotFoundExceptionThrown() {
        int from = 0;
        int size = 20;
        Long userId = 1L;
        when(mockUserRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getItemRequestOtherRequestor(userId,
                                                                                                          from, size));
        verify(mockItemRequestMapper, never()).toItemRequestWithItemsDto(anyList(), any(ItemRequest.class));
    }
}