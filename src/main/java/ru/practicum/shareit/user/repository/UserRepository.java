package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User createUser(User user);
    List<User> getAllUser();
    User getUserById(Integer userId);
    User updateUser(User user, Integer userId);
    void deleteUserById(Integer userId);
}
