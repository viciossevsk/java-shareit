package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Setter
@Getter
public class UserDto {
    private Integer id;
    @Size(max = 20)
    private String name;
    @Email
    private String email;
}
