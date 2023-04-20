package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(UserDto userDto) {
        userRepository.checkEmailIsDublicate(null, userDto.getEmail());
        validate(userDto);
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userRepository.createUser(user));
    }

    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto getUserById(Integer userId) {
        return userMapper.toUserDto(userRepository.getUserById(userId));
    }

    public UserDto updateUser(UserDto userDto, Integer userId) {
        User userInMap = userRepository.getUserById(userId);
        userDto.setId(userId);
        if (userDto.getEmail() != null) {
            userRepository.checkEmailIsDublicate(userId, userDto.getEmail());
            userInMap.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            userInMap.setName(userDto.getName());
        }
        return userMapper.toUserDto(userRepository.updateUser(userInMap, userId));
    }

    public void deleteUserById(Integer userId) {
        userRepository.deleteUserById(userId);
    }

    private void validate(UserDto userDto) {
        if ((userDto.getName() == null) || (userDto.getName().isEmpty())) {
            throw new ValidationException("User name invalid");
        }
        if ((userDto.getEmail() == null) || (userDto.getEmail().isEmpty())) {
            throw new ValidationException("Item Email invalid");
        }
    }
}
