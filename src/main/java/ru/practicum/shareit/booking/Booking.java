package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@AllArgsConstructor
@Setter
@Getter
public class Booking {

    private Integer id;
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
