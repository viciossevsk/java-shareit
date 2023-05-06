package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(
            "select b" +
                    " from Booking b" +
                    " join fetch b.item" +
                    " join fetch b.booker" +
                    " where b.id in :bookingIds"
    )
    Set<Booking> findBookingsAndFetchAllEntities(@Param("bookingIds") Set<Long> bookingIds, PageRequest page);

    @Query(
            "select b" +
                    " from Booking b" +
                    " join fetch b.item" +
                    " join fetch b.booker" +
                    " where b.id in :bookingIds" +
                    " order by b.id desc"
    )
    Set<Booking> findBookingsAndFetchAllEntitiesOrderByBooker(@Param("bookingIds") Set<Long> bookingIds,
                                                              PageRequest page);
}
