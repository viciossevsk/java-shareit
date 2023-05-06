package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;

    @NotNull(groups = BookingController.class)
    private Long itemId;
    @NotNull(groups = BookingController.class)
    @DateTimeFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    private LocalDateTime start;
    @NotNull(groups = BookingController.class)
    @DateTimeFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
