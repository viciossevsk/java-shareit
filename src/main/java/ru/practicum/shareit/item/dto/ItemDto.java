package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.ItemController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemDto {
    private Long id;
    @Size(max = 20)
    @NotBlank(groups = ItemController.class)
    private String name;
    @NotBlank(groups = ItemController.class)
    private String description;
    @NotNull(groups = ItemController.class)
    private Boolean available;
    private BookingShortDto nextBooking;
    private BookingShortDto lastBooking;
    @JsonManagedReference(value = "comment")
    private List<CommentDto> comments;
}
