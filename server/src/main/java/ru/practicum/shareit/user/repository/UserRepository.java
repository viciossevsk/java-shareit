package ru.practicum.shareit.user.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            "select b" +
                    " from User u" +
                    " join u.bookings b" +
                    " join fetch b.booker" +
                    " join fetch b.item ib" +
                    " left join fetch ib.comments" +
                    " where u = :booker" +
                    " order by b.id desc"
    )
    Set<Booking> findBookingsOfUserAndFetchAllEntities(@Param("booker") User booker, PageRequest page);

}
