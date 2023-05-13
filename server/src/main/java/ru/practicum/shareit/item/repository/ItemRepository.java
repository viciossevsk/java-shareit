package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findAllByOwnerId(Long ownerId, PageRequest page);

    Collection<Item> findAllByNameIsLikeIgnoreCaseOrDescriptionIsLikeIgnoreCaseAndAvailableTrue(String queryForName,
                                                                                                String queryForDescription,
                                                                                                PageRequest page);

    void deleteById(Long itemId);

    @Query(
            "select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.booker.id, b.item.id)" +
                    " from Booking as b " +
                    " where item in :itemIds" +
                    " and CURRENT_TIMESTAMP < b.start" +
                    " and b.status = 'APPROVED'" +
                    " order by b.start"
    )
    Collection<BookingShortDto> findNextBookings(@Param("itemIds")Collection<Item> items);

    @Query(
            "select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.booker.id, b.item.id)" +
                    " from Booking as b " +
                    " where item in :itemIds" +
                    " and CURRENT_TIMESTAMP > b.start" +
                    " and b.status = 'APPROVED'" +
                    " order by b.start desc"
    )
    Collection<BookingShortDto> findLastBookings(@Param("itemIds") Collection<Item> items);

    @Query(
            "select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.booker.id, b.item.id)" +
                    " from Booking as b " +
                    " where item = :item" +
                    " and CURRENT_TIMESTAMP < b.start" +
                    " and b.status = 'APPROVED'" +
                    " order by b.start"
    )
    Collection<BookingShortDto> findNextBookingsOfItem(@Param("item") Item item);

    @Query(
            "select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.booker.id, b.item.id)" +
                    " from Booking as b " +
                    " where item = :item" +
                    " and CURRENT_TIMESTAMP > b.start" +
                    " and b.status = 'APPROVED'" +
                    " order by b.start desc"
    )
    Collection<BookingShortDto> findLastBookingsOfItem(@Param("item") Item item);

    @Query(
            "select i" +
                    " from Item i" +
                    " left join fetch i.comments" +
                    " where i.requestId in :requestIds"
    )
    List<Item> findAllItemsByRequestIds(@Param("requestIds") Set<Long> requestIds);

}
