package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findAllByOwnerId(Long ownerId);

    Collection<Item> findAllByNameIsLikeIgnoreCaseOrDescriptionIsLikeIgnoreCaseAndAvailableTrue(String queryForName,
                                                                                                String queryForDescription);

    void deleteByIdAndAndOwnerId(Long itemId, Long ownerId);

    @Query(
            "select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.booker.id, b.item.id)" +
                    " from Booking as b " +
                    " where item in ?1" +
                    " and CURRENT_TIMESTAMP < b.start" +
                    " and b.status = 'APPROVED'" +
                    " order by b.start"
    )
    Collection<BookingShortDto> findNextBookings(Collection<Item> items);

    @Query(
            "select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.booker.id, b.item.id)" +
                    " from Booking as b " +
                    " where item in ?1" +
                    " and CURRENT_TIMESTAMP > b.start" +
                    " and b.status = 'APPROVED'" +
                    " order by b.start desc"
    )
    Collection<BookingShortDto> findLastBookings(Collection<Item> items);

    @Query(
            "select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.booker.id, b.item.id)" +
                    " from Booking as b " +
                    " where item = ?1" +
                    " and CURRENT_TIMESTAMP < b.start" +
                    " and b.status = 'APPROVED'" +
                    " order by b.start"
    )
    Collection<BookingShortDto> findNextBookingsOfItem(Item item);

    @Query(
            "select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.booker.id, b.item.id)" +
                    " from Booking as b " +
                    " where item = ?1" +
                    " and CURRENT_TIMESTAMP > b.start" +
                    " and b.status = 'APPROVED'" +
                    " order by b.start desc"
    )
    Collection<BookingShortDto> findLastBookingsOfItem(Item item);

}
