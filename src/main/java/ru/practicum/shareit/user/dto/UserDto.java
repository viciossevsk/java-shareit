package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.practicum.shareit.user.UserController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    @Size(max = 20)
    @NotBlank(groups = UserController.class)
    private String name;
    @Email(regexp = "^[a-z0-9-_.%]+@[a-z0-9-_]+.[a-z]+$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "email is " +
            "not valid")
    @NotBlank(groups = UserController.class)
    private String email;
}
