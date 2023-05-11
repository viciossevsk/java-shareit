package ru.practicum.shareit.booking2;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking2.dto.BookingDto;
import ru.practicum.shareit.booking2.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface BookingMapper {
    BookingDto toBookingDto(Booking booking);

    Booking toBooking(BookingDto bookingDto);

}
