package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Integer userId);

    User updateUser(User user, Integer userId);

    void deleteUserById(Integer userId);

    void checkEmailIsDublicate(Integer userId, String email);
}
