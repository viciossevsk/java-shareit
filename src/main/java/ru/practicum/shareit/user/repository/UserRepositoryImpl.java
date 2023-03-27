package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;

import java.util.*;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.stringToGreenColor;

@Component
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private int generatorId = 0;

    @Override
    public User createUser(User user) {
        user.setId(++generatorId);
        users.put(generatorId, user);
        log.info(stringToGreenColor("create user..." + user.toString()));
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info(stringToGreenColor("get all user..."));
        return new ArrayList<>(users.values());
    }

    @Override
    public Boolean checkUserExist(Integer userId) {
        return Optional.of(getUserById(userId)).isPresent();
    }

    @Override
    public void deleteUserById(Integer userId) {
        log.info(stringToGreenColor("delete user..."));
        users.remove(userId);
    }

    @Override
    public User getUserById(Integer userId) {
        if (userId != null) {
            return Optional.of(users.get(userId)).orElseThrow(() -> new UserNotFoundException("user id not found"));


//            for (User user : users.values()){
//                if (user.getId() == userId) {
//                    return user;
//                }
//            }
//            throw new UserNotFoundException("user id not found");


        } else {
            throw new UserNotFoundException("user id not found");
        }
    }

    @Override
    public User updateUser(User user, Integer userId) {
        users.replace(userId, user);
        log.info(stringToGreenColor("update user..." + user.toString()));
        return user;
    }

    @Override
    public void validate(User user) {
        if (user.getEmail() == null) {
            throw new ValidationException("user email invalid");
        }
        for (User userInMap : users.values()) {
            if ((userInMap.getEmail().equals(user.getEmail())) && (userInMap.getId() != user.getId())) {
                throw new ConflictException(String.format("email %s already belongs to user %s", user.getEmail(),
                                                          userInMap.getName()));
            }
        }
    }
}
