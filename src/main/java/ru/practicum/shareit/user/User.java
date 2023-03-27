package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private Integer id;
    @NotNull
    @NotBlank
    @Size(max = 20)
    private String name;
    @NotNull
    @NotBlank
    @Email
    private String email;
}
