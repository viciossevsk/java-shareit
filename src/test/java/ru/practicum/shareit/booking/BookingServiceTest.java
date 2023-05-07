package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UnknownStateException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
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
import static ru.practicum.shareit.otherFunction.AddvansedFunctions.getPage;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository mockBookingRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ItemRepository mockItemRepository;
    @Mock
    private BookingMapper mockBookingMapper;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user, owner;
    private Item item;
    private Booking booking;
    private BookingDto requestBookingDto;
    private BookingDto responseBookingDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@mail.com");

        owner = new User();
        owner.setId(2L);
        owner.setName("Owner");
        owner.setEmail("owner@mail.ru");

        item = new Item();
        item.setId(1L);
        item.setName("Item1");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(owner);

        booking = new Booking();
        booking.setId(1L);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setItem(item);
        booking.setBooker(user);

        requestBookingDto = new BookingDto();
        requestBookingDto.setItemId(1L);
        requestBookingDto.setStart(LocalDateTime.now().minusDays(2));
        requestBookingDto.setEnd(LocalDateTime.now().minusDays(1));

        responseBookingDto = new BookingDto();
        responseBookingDto.setItemId(1L);
        responseBookingDto.setStart(LocalDateTime.now().minusDays(2));
        responseBookingDto.setEnd(LocalDateTime.now().minusDays(1));
        responseBookingDto.setStatus(BookingStatus.APPROVED);
    }

    @Test
    void createBookingTest_whenDataValid_thenReturnBookingDto() {
        long bookerId = 1L;
        when(mockUserRepository.existsById(anyLong())).thenReturn(true);
        when(mockItemRepository.existsById(anyLong())).thenReturn(true);
        when(mockItemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mockBookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(mockBookingMapper.toBooking(requestBookingDto)).thenReturn(booking);
        when(mockBookingRepository.save(booking)).thenReturn(booking);
        when(mockBookingMapper.toBookingDto(booking)).thenReturn(new BookingDto());

        BookingDto bookingDto = bookingService.createBooking(bookerId, requestBookingDto);

        assertEquals(new BookingDto(), bookingDto);
        verify(mockBookingRepository).save(booking);
    }

    @Test
    void createBookingTest_whenItemNotAvailable_thenValidationExceptionThrown() {
        long bookerId = 1L;
        item.setAvailable(false);
        when(mockUserRepository.existsById(anyLong())).thenReturn(true);
        when(mockItemRepository.existsById(anyLong())).thenReturn(true);
        when(mockItemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> bookingService.createBooking(bookerId, requestBookingDto));
        verify(mockBookingRepository, never()).save(booking);
    }


    @Test
    void createBookingTest_whenTimeDataNotValid_thenValidationExceptionThrown() {
        long bookerId = 1L;
        requestBookingDto.setStart(requestBookingDto.getEnd().plusMinutes(1));

        assertThrows(ValidationException.class, () -> bookingService.createBooking(bookerId, requestBookingDto));
        verify(mockBookingRepository, never()).save(booking);
    }

    @Test
    void approvingBookingTest_whenInvoke_thenChangeAvailabilityAndReturnBookingDto() {
        long bookingId = 1L;
        long ownerId = 2L;
        booking.setStatus(BookingStatus.WAITING);
        when(mockBookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        when(mockBookingMapper.toBookingDto(booking)).thenReturn(responseBookingDto);

        BookingDto bookingDto = bookingService.approvingBooking(ownerId, bookingId, true);

        assertEquals(responseBookingDto, bookingDto);

        booking.setStatus(BookingStatus.WAITING);
        responseBookingDto.setStatus(BookingStatus.REJECTED);
        bookingDto = bookingService.approvingBooking(ownerId, bookingId, false);

        assertEquals(responseBookingDto, bookingDto);
    }

    @Test
    void approvingBookingTest_whenBookingNotFound_thenEntityNotFoundExceptionThrown() {
        long bookingId = 1L;
        long ownerId = 2L;
        booking.setStatus(BookingStatus.WAITING);
        when(mockBookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.approvingBooking(ownerId, bookingId, true));
    }

    @Test
    void approvingBookingTest_whenBookingStatusNotWaiting_thenIllegalArgumentExceptionThrown() {
        long bookingId = 1L;
        long ownerId = 2L;
        booking.setStatus(BookingStatus.APPROVED);
        when(mockBookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(IllegalArgumentException.class, () -> bookingService.approvingBooking(ownerId, bookingId, true));
    }

    @Test
    void approvingBookingTest_whenUserNotOwner_thenEntityNotFoundExceptionThrown() {
        long bookingId = 1L;
        long ownerId = 1L;
        booking.setStatus(BookingStatus.WAITING);
        when(mockBookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(EntityNotFoundException.class, () -> bookingService.approvingBooking(ownerId, bookingId, true));
    }

    @Test
    void getBookingTest_whenBookerOrOwnerGettingTheirBooking_thenReturnBookingDto() {
        long bookingId = 1L;
        long userId = 1L;
        when(mockBookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(mockBookingMapper.toBookingDto(booking)).thenReturn(responseBookingDto);

        BookingDto bookingDto = bookingService.getBooking(bookingId, userId);

        assertEquals(responseBookingDto, bookingDto);

        userId = 2L;
        bookingDto = bookingService.getBooking(bookingId, userId);
        assertEquals(responseBookingDto, bookingDto);
    }

    @Test
    void getBookingTest_whenBookingNotFound_thenEntityNotFoundExceptionThrown() {
        long bookingId = 1L;
        long userId = 1L;
        when(mockBookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(bookingId, userId));
    }

    @Test
    void getBookingTest_whenGettingBookingNotBookerOrOwner_thenEntityNotFoundExceptionThrown() {
        long bookingId = 1L;
        long userId = 3L;
        when(mockBookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(bookingId, userId));
    }

    @Test
    void getBookerStatisticsTest_whenInvoke_thenReturnListBookingDto() {
        int start = 0;
        int size = 10;
        long bookerId = 1L;
        user.setBookings(Set.of(booking));
        PageRequest page = getPage(start, size);

        when(mockUserRepository.findById(bookerId)).thenReturn(Optional.of(user));
        when(mockBookingMapper.toBookingDto(booking)).thenReturn(responseBookingDto);

        when(mockBookingRepository.findBookingsAndFetchAllEntitiesOrderByBooker(Set.of(1L), page)).thenReturn(Set.of(booking));

        String state = "PAST";
        List<BookingDto> bookingDtos = bookingService.getBookerStatistics(bookerId, state, start, size);
        assertEquals(List.of(responseBookingDto), bookingDtos);

        state = "FUTURE";
        bookingDtos = bookingService.getBookerStatistics(bookerId, state, start, size);
        assertEquals(List.of(), bookingDtos);

        state = "CURRENT";
        bookingDtos = bookingService.getBookerStatistics(bookerId, state, start, size);
        assertEquals(List.of(), bookingDtos);

        state = "WAITING";
        bookingDtos = bookingService.getBookerStatistics(bookerId, state, start, size);
        assertEquals(List.of(), bookingDtos);

        state = "REJECTED";
        bookingDtos = bookingService.getBookerStatistics(bookerId, state, start, size);
        assertEquals(List.of(), bookingDtos);

        state = "ALL";
        bookingDtos = bookingService.getBookerStatistics(bookerId, state, start, size);
        assertEquals(List.of(responseBookingDto), bookingDtos);
    }

    @Test
    void getBookerStatisticsTest_whenBookerNotFound_thenEntityNotFoundExceptionThrown() {
        int start = 0;
        int size = 10;
        long bookerId = 2L;
        String state = "PAST";
        when(mockUserRepository.findById(bookerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookerStatistics(bookerId, state, start,
                                                                                             size));
    }

    @Test
    void getBookerStatisticsTest_whenStateNotValid_thenUnknownStateExceptionThrown() {
        int start = 0;
        int size = 20;
        long bookerId = 2L;
        String state = "UNKNOWN";
        PageRequest page = PageRequest.of(start, size);
        user.setBookings(Set.of(booking));
        when(mockUserRepository.findById(bookerId)).thenReturn(Optional.of(user));
        when(mockBookingRepository.findBookingsAndFetchAllEntitiesOrderByBooker(Set.of(1L), page)).thenReturn(Set.of(booking));

        assertThrows(UnknownStateException.class, () -> bookingService.getBookerStatistics(bookerId, state, start,
                                                                                           size));
    }

    @Test
    void getOwnerStatisticsTest_whenInvoke_thenReturnListBookingDto() {
        int from = 0;
        int size = 20;
        long ownerId = 2L;
        responseBookingDto = new BookingDto();
        responseBookingDto.setItemId(1L);
        responseBookingDto.setStart(LocalDateTime.now().minusDays(2));
        responseBookingDto.setEnd(LocalDateTime.now().minusDays(1));
        responseBookingDto.setStatus(BookingStatus.APPROVED);


        owner.setItems(Set.of(item));
        item.setBookings(Set.of(booking));
        PageRequest page = PageRequest.of(from, size);
        when(mockUserRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(mockBookingRepository.findBookingsAndFetchAllEntitiesOrderByBooker(Set.of(1L), page)).thenReturn(Set.of(booking));
        when(mockBookingMapper.toBookingDto(booking)).thenReturn(new BookingDto());

        String state = "PAST";
        List<BookingDto> bookingDtosActual = bookingService.getOwnerStatistics(ownerId, state, from, size);
        assertEquals(List.of(new BookingDto()), bookingDtosActual);

        state = "FUTURE";
        bookingDtosActual = bookingService.getOwnerStatistics(ownerId, state, from, size);
        assertEquals(List.of(), bookingDtosActual);

        state = "CURRENT";
        bookingDtosActual = bookingService.getOwnerStatistics(ownerId, state, from, size);
        assertEquals(List.of(), bookingDtosActual);

        state = "WAITING";
        bookingDtosActual = bookingService.getOwnerStatistics(ownerId, state, from, size);
        assertEquals(List.of(), bookingDtosActual);

        state = "REJECTED";
        bookingDtosActual = bookingService.getOwnerStatistics(ownerId, state, from, size);
        assertEquals(List.of(), bookingDtosActual);

        state = "ALL";
        bookingDtosActual = bookingService.getOwnerStatistics(ownerId, state, from, size);
        assertEquals(List.of(new BookingDto()), bookingDtosActual);
    }

    @Test
    void getOwnerStatisticsTest_whenOwnerNotFound_thenEntityNotFoundExceptionThrown() {
        int from = 0;
        int size = 20;
        long ownerId = 2L;
        String state = "PAST";
        when(mockUserRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getOwnerStatistics(ownerId, state, from,
                                                                                            size));
        verify(mockUserRepository, Mockito.times(1)).findById(ownerId);
    }

    @Test
    void getOwnerStatisticsTest_whenRequestDataNotValid_thenEntityNotFoundExceptionThrown() {
        int from = -1;
        int size = 20;
        long ownerId = 2L;
        String state = "PAST";

        assertThrows(EntityNotFoundException.class, () -> bookingService.getOwnerStatistics(ownerId, state, from,
                                                                                            size));
        verify(mockUserRepository, Mockito.times(1)).findById(ownerId);
    }
}
