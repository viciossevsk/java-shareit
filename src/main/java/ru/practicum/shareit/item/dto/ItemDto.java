package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.practicum.shareit.item.ItemController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    private Integer id;
    @Size(max = 20)
    @NotBlank(groups = ItemController.class)
    private String name;
    @NotBlank(groups = ItemController.class)
    private String description;
    @NotBlank(groups = ItemController.class)
    private Boolean available;
}
