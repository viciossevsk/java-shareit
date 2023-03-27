package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepository {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Integer userId);

    User updateUser(User user, Integer userId);

    void deleteUserById(Integer userId);

    void validate(User user);

    Boolean checkUserExist(Integer userId);
}
