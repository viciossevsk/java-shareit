package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        userRepository.validate(user);
        return userRepository.createUser(user);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(Integer userId) {
        return userRepository.getUserById(userId);
    }

    public User updateUser(User user, Integer userId) {
        User userInMap = userRepository.getUserById(userId);
        user.setId(userId);
        if (user.getEmail() != null) {
            userRepository.validate(user);
            userInMap.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userInMap.setName(user.getName());
        }
        return userRepository.updateUser(userInMap, userId);
    }
    public void deleteUserById(Integer userId) {
        userRepository.deleteUserById(userId);
    }
}
