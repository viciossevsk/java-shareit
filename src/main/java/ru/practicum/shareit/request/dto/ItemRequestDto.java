package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@AllArgsConstructor
@Setter
@Getter
public class ItemRequestDto {
    @NotNull
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @NotBlank
    private User requestor;
    @NotNull
    @NotBlank
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate created;
}
