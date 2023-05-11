package ru.practicum.shareit.item.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.ItemController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class CommentDto {
    private Long id;
    @NotBlank(message = "Comment text must be specified")
    private String text;
    @NotNull(groups = ItemController.class)
    private String authorName;
    @NotNull(groups = ItemController.class)
    @DateTimeFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    private LocalDateTime created;
    @NotNull(groups = ItemController.class)
    private Long itemId;

}
