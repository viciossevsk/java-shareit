package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class BookingDto {
    @NotNull
    @NotBlank
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate start;
    @NotNull
    @NotBlank
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate end;
    @NotNull
    @NotBlank
    private Item item;
    @NotNull
    @NotBlank
    private User booker;
    @NotNull
    @NotBlank
    private BookingStatus status;
}
