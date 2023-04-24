package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BookingDto {
    private Long id;

    @NotNull(groups = BookingController.class)
    private Long itemId;
    @NotNull(groups = BookingController.class)
    @DateTimeFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    @FutureOrPresent(message = "Start time can't be the past")
    private LocalDateTime start;
    @NotNull(groups = BookingController.class)
    @DateTimeFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    private LocalDateTime end;
    @NotNull(groups = BookingController.class)
    @JsonBackReference(value = "item")
    private Item item;
    @NotNull(groups = BookingController.class)
    @JsonBackReference(value = "booker")
    private User booker;
    @NotNull(groups = BookingController.class)
    private BookingStatus status;
}
