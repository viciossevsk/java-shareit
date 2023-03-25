package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@Setter
@Getter
public class Item {
    private Integer id;
    @NotNull
    @NotBlank
    @Size(max = 20)
    private String name;
    private String description;
    private boolean available;

    private ItemRequest request;
    @NotNull
    @NotBlank
    private User owner;
}
