package ru.practicum.shareit.user;

import lombok.*;

import javax.validation.constraints.Email;
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

    @Size(max = 20)
    private String name;
    @Email
    private String email;
}
