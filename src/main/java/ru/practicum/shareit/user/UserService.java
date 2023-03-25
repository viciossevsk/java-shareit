package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user){
        return userRepository.createUser(user);
    }
    public List<User> getAllUser(){
        return userRepository.getAllUser();
    }
    public User getUserById( Integer userId) {
        return userRepository.getUserById(userId);
    }

    public User updateUser(User user, Integer userId){
            return userRepository.updateUser(user, userId);
        }
    public void deleteUserById(Integer userId) {
        userRepository.deleteUserById(userId);
    }
}
