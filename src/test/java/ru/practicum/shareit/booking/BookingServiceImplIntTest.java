package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = "/testSchema.sql")
public class BookingServiceImplIntTest {
    private final BookingServiceImpl mockBookingServiceImpl;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    LocalDateTime created = LocalDateTime.parse("2023-06-10T12:00:00", formatter);
    UserDto userDto1;
    UserDto userDto2;
    CommentDto commentDto;
    ItemDto itemDto;
    BookingDto requestBookingDto1;
    BookingDto responseBookingDto1;
    BookingDto responseBookingDto2;
    BookingDto responseBookingDto3;
    BookingDto responseBookingDto4;
    BookingDto responseBookingDto5;
    long ownerId = 1L;
    long bookingId = 1L;
    boolean isApproved = true;
    long bookerId = 2;
    int start = 0;
    int size = 10;

    @BeforeEach
    void setUp() {
        userDto1 = UserDto.builder()
                .id(1L)
                .name("user_1")
                .email("user_1@mail.ru")
                .build();

        userDto2 = UserDto.builder()
                .id(2L)
                .name("user_2")
                .email("user_2@mail.ru")
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .text("text_comment")
                .authorName("user_2")
                .created(created)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Item1")
                .description("Description")
                .available(true)
                .comments(Set.of(commentDto))
                .build();

        requestBookingDto1 = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.parse("2023-06-01T12:00:00", formatter))
                .end(LocalDateTime.parse("2023-06-02T12:00:00", formatter))
                .build();

        responseBookingDto1 = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.parse("2023-06-01T12:00:00", formatter))
                .end(LocalDateTime.parse("2023-06-02T12:00:00", formatter))
                .item(itemDto)
                .booker(userDto2)
                .status(BookingStatus.WAITING)
                .build();

        responseBookingDto2 = BookingDto.builder()
                .id(2L)
                .start(LocalDateTime.parse("2023-06-01T12:00:00", formatter))
                .end(LocalDateTime.parse("2023-06-02T12:00:00", formatter))
                .status(BookingStatus.REJECTED)
                .booker(userDto2)
                .item(itemDto)
                .build();

        responseBookingDto3 = BookingDto.builder()
                .id(3L)
                .start(LocalDateTime.parse("2023-04-26T12:00:00", formatter))
                .end(LocalDateTime.parse("2023-04-27T12:00:00", formatter))
                .status(BookingStatus.APPROVED)
                .booker(userDto2)
                .item(itemDto)
                .build();

        responseBookingDto4 = BookingDto.builder()
                .id(4L)
                .start(LocalDateTime.parse("2023-05-10T12:00:00", formatter))
                .end(LocalDateTime.parse("2023-06-10T12:00:00", formatter))
                .status(BookingStatus.APPROVED)
                .booker(userDto2)
                .item(itemDto)
                .build();

        responseBookingDto5 = BookingDto.builder()
                .id(5L)
                .start(LocalDateTime.parse("2023-06-01T12:00:00", formatter))
                .end(LocalDateTime.parse("2023-06-02T12:00:00", formatter))
                .item(itemDto)
                .booker(userDto2)
                .status(BookingStatus.WAITING)
                .build();

    }

    @Test
    void createBooking_checkData() {

        BookingDto bookingDto = mockBookingServiceImpl.createBooking(bookerId, requestBookingDto1);

        assertEquals(responseBookingDto5, bookingDto);

    }

    @Test
    void approvingBooking_checkData() {
        requestBookingDto1.setStatus(BookingStatus.APPROVED);
        responseBookingDto1.setStatus(BookingStatus.APPROVED);

        BookingDto bookingDto = mockBookingServiceImpl.approvingBooking(ownerId, bookingId, isApproved);

        assertEquals(responseBookingDto1, bookingDto);
    }

    @Test
    void getBooking_checkData() {
        BookingDto bookingDto = mockBookingServiceImpl.getBooking(bookingId, ownerId);

        assertEquals(responseBookingDto1, bookingDto);
    }

    @Test
    void getBookerStatistics_checkData() {

        String state = "FUTURE";
        List<BookingDto> bookingDtos = mockBookingServiceImpl.getBookerStatistics(ownerId, state, start, size);
        assertEquals(List.of(), bookingDtos);

        state = "PAST";
        bookingDtos = mockBookingServiceImpl.getBookerStatistics(ownerId, state, start, size);
        assertEquals(List.of(), bookingDtos);

        state = "REJECTED";
        bookingDtos = mockBookingServiceImpl.getBookerStatistics(ownerId, state, start, size);
        assertEquals(List.of(), bookingDtos);

        state = "WAITING";
        bookingDtos = mockBookingServiceImpl.getBookerStatistics(ownerId, state, start, size);
        assertEquals(List.of(), bookingDtos);

        state = "ALL";
        bookingDtos = mockBookingServiceImpl.getBookerStatistics(ownerId, state, start, size);
        assertEquals(List.of(), bookingDtos);

        state = "CURRENT";
        bookingDtos = mockBookingServiceImpl.getBookerStatistics(ownerId, state, start, size);
        assertEquals(List.of(), bookingDtos);
    }

    @Test
    void getOwnerStatistics_checkData() {

        String state = "FUTURE";
        List<BookingDto> bookingDtos = mockBookingServiceImpl.getOwnerStatistics(ownerId, state, start, size);
        assertEquals(List.of(responseBookingDto2, responseBookingDto1, responseBookingDto4),
                     bookingDtos);

        state = "PAST";
        bookingDtos = mockBookingServiceImpl.getOwnerStatistics(ownerId, state, start, size);
        assertEquals(List.of(responseBookingDto3), bookingDtos);

        state = "REJECTED";
        bookingDtos = mockBookingServiceImpl.getOwnerStatistics(ownerId, state, start, size);
        assertEquals(List.of(responseBookingDto2), bookingDtos);

        state = "WAITING";
        bookingDtos = mockBookingServiceImpl.getOwnerStatistics(ownerId, state, start, size);
        assertEquals(List.of(responseBookingDto1), bookingDtos);

        state = "ALL";
        bookingDtos = mockBookingServiceImpl.getOwnerStatistics(ownerId, state, start, size);
        assertEquals(List.of(responseBookingDto2, responseBookingDto1, responseBookingDto4, responseBookingDto3),
                     bookingDtos);

        state = "CURRENT";
        bookingDtos = mockBookingServiceImpl.getOwnerStatistics(ownerId, state, start, size);
        assertEquals(List.of(), bookingDtos);
    }
}
