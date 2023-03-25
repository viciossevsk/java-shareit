package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@AllArgsConstructor
@Setter
@Getter

public class UserDto {
    @NotNull
    @NotBlank
    @Size(max = 20)
    private String name;
    @NotNull
    @NotBlank
    @Email
    private String email;
}
