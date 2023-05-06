package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.ItemController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemDto {
    private Long id;
    @Size(max = 20)
    @NotBlank(groups = ItemController.class)
    private String name;
    @NotBlank(groups = ItemController.class)
    private String description;
    @NotNull(groups = ItemController.class)
    private Boolean available;
    @Positive(message = "Value must be Long and positive")
    private Long requestId;
    private BookingShortDto nextBooking;
    private BookingShortDto lastBooking;
    private List<CommentDto> comments;
}
