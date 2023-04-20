package ru.practicum.shareit.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Data
public class ItemRequest {
    private Integer id;
    private String name;
    private String description;
    private User requestor;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate created;
}
