package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@Setter
@Getter
public class ItemDto {
    private Integer id;
    @Size(max = 20)
    private String name;
    private String description;
    private Boolean available;
}
