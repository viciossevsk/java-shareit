package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Optional;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(
            "select b" +
                    " from Booking b" +
                    " join fetch b.item" +
                    " join fetch b.booker" +
                    " where b.id in ?1"
    )
    Optional<Booking> findBookingByIdAndFetchAllEntities(Long bookingId);

    @Query(
            "select b" +
                    " from Booking b" +
                    " join fetch b.item" +
                    " join fetch b.booker" +
                    " where b.id in ?1"
    )
    Set<Booking> findBookingsAndFetchAllEntities(Set<Long> bookingIds);
}
