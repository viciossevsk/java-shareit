package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.otherFunction.AddvansedFunctions.getPage;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@Sql(value = "/testSchema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {

    private final BookingRepository bookingRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    Booking booking1 = Booking.builder()
            .id(2L)
            .status(BookingStatus.REJECTED)
            .start(LocalDateTime.parse("2023-06-01T12:00:00", formatter))
            .end(LocalDateTime.parse("2023-06-02T12:00:00", formatter))
            .build();
Booking booking2 = Booking.builder()
        .id(1L)
        .status(BookingStatus.WAITING)
        .start(LocalDateTime.parse("2023-06-01T12:00:00", formatter))
        .end(LocalDateTime.parse("2023-06-02T12:00:00", formatter))
        .build();


    @Test
    void findBookingsAndFetchAllEntitiesOrderByBooker() {
        int start = 0;
        int size = 10;


        PageRequest page = getPage(start, size);


        Set<Booking> bookings = bookingRepository.findBookingsAndFetchAllEntitiesOrderByBooker(Set.of(1L, 2L), page);

        assertEquals(Set.of(booking1, booking2).toString(), bookings.toString());

    }




}
