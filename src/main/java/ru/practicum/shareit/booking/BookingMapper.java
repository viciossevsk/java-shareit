package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Mapper(componentModel = "spring", uses = {User.class, Item.class, BookingStatus.class})
public interface BookingMapper {
    BookingDto toBookingDto(Booking booking);

    Booking toBooking(BookingDto bookingDto);
}
