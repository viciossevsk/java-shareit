package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.MISTAKEN_USER_ID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        validate(userDto, null);
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        return userMapper.toUserDto(userRepository.findById(userId)
                                            .orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_USER_ID, userId)))
        );
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.format(MISTAKEN_USER_ID, userId)));
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, Long userId) {
        User user = getUser(userId);

        userDto.setId(userId);
        userDto.setEmail(userDto.getEmail() == null ? user.getEmail() : userDto.getEmail());
        userDto.setName(userDto.getName() == null ? user.getName() : userDto.getName());

        user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    private void validate(UserDto userDto, Long userId) {
        if ((userDto.getName() == null) || (userDto.getName().isEmpty())) {
            throw new ValidationException("User name invalid");
        }
        if ((userDto.getEmail() == null) || (userDto.getEmail().isEmpty())) {
            throw new ValidationException("Item Email invalid");
        }
    }
}
