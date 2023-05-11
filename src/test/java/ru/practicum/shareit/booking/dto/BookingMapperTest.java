package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(value = "/testSchema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingMapperTest {
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private BookingDto bookingDto;
    private Booking booking;
    private User user;
    private Item item;
    private ItemDto itemDto;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {
        User owner = userRepository.findById(1L).get();

        Comment comment = commentRepository.findById(1L).get();

        user = userRepository.findById(2L).get();

        item = itemRepository.findById(1L).get();
        item.setOwner(owner);
        item.setComments(Set.of(comment));

        booking = bookingRepository.findById(1L).get();
        booking.setBooker(user);
        booking.setItem(item);

        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setName("user2");
        userDto.setEmail("user2@mail.ru");

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Comment");
        commentDto.setAuthorName("user2");
        commentDto.setCreated(LocalDateTime.parse("2023-09-10T12:00:00", formatter));

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item1");
        itemDto.setDescription("Item1 Description");
        itemDto.setAvailable(true);
        itemDto.setComments(Set.of(commentDto));

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.parse("2023-06-01T12:00:00", formatter));
        bookingDto.setEnd(LocalDateTime.parse("2023-06-02T12:00:00", formatter));
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setBooker(userDto);
        bookingDto.setItem(itemDto);
    }

    @Test
    void mapTest_mapBookingDtoToBooking() {
        Booking actual = bookingMapper.toBooking(bookingDto);

        assertEquals(booking, actual);
    }

    @Test
    void mapTest_mapBookingDtoToBooking_whenItemDtoIsNull() {
        bookingDto.setItem(null);
        booking.setItem(null);
        Booking actual = bookingMapper.toBooking(bookingDto);

        assertEquals(booking, actual);
    }

    @Test
    void mapTest_mapBookingDtoToBooking_whenCommentsIsNull() {
        itemDto.setComments(null);
        item.setComments(null);
        Booking actual = bookingMapper.toBooking(bookingDto);

        assertEquals(booking, actual);
    }
}