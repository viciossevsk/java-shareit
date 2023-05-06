package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestWithItemsDto {
    @NotNull
    private Long id;

    @NotBlank
    private String description;

    @DateTimeFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    @NotNull
    private LocalDateTime created;

    private List<ItemDto> items;
}
