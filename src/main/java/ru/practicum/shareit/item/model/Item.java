package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@ToString
public class Item {
    private Integer id;
    @NotNull
    @NotBlank
    @Size(max = 20)
    private String name;
    private String description;
    @NotNull
    @NotBlank
    private Boolean available;

    private ItemRequest request;
    @NotNull
    @NotBlank
    private User owner;
}
