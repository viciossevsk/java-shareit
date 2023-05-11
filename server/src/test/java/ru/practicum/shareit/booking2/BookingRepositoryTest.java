package ru.practicum.shareit.booking2;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking2.model.Booking;
import ru.practicum.shareit.booking2.repository.BookingRepository;

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



    @Test
    void findBookingsAndFetchAllEntitiesOrderByBooker() {
        int start = 0;
        int size = 10;


        PageRequest page = getPage(start, size);


        Set<Booking> bookings = bookingRepository.findBookingsAndFetchAllEntitiesOrderByBooker(Set.of(1L, 2L), page);

        assertEquals(2, bookings.size());

    }




}
