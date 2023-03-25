package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@Setter
@Getter
public class ItemDto {
    @NotNull
    @NotBlank
    @Size(max = 20)
    private String name;
    @NotNull
    @NotBlank
    private String description;
    private boolean available;
}
