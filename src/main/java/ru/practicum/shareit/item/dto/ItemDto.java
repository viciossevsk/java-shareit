package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ItemDto {
    private Integer id;
    //    @NotNull
//    @NotBlank
    @Size(max = 20)
    private String name;

    private String description;
    private Boolean available;
}
