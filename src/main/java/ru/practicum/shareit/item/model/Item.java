package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Item {

    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
    private User owner;
}
