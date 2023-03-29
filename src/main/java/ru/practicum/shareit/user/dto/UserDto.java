package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    private Integer id;
    //    @NotNull
//    @NotBlank
    @Size(max = 20)
    private String name;
    //    @NotNull
//    @NotBlank
    @Email
    private String email;
}
