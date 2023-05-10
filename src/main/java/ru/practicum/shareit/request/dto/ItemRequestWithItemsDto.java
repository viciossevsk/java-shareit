package ru.practicum.shareit.request.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
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
