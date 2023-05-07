package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserMapper mockUserMapper;
    @Mock
    private UserRepository mockUserRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private UserDto requestUserDto;
    private UserDto responseUserDto;
    private User user;

    @BeforeEach
    void setUp() {
        requestUserDto = new UserDto();
        requestUserDto.setName("John");
        requestUserDto.setEmail("john@mail.com");

        responseUserDto = new UserDto();
        responseUserDto.setId(1L);
        responseUserDto.setName("John");
        responseUserDto.setEmail("john@mail.com");

        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@mail.com");

    }

    @Test
    void createTest_whenInvoke_thenReturnUserDto() {
        when(mockUserMapper.toUser(requestUserDto)).thenReturn(user);
        when(mockUserRepository.save(user)).thenReturn(user);
        when(mockUserMapper.toUserDto(user)).thenReturn(responseUserDto);

        UserDto actualDto = userService.createUser(requestUserDto);

        assertEquals(responseUserDto, actualDto);
        verify(mockUserRepository).save(user);
    }

    @Test
    void readTest_whenUserFound_thenReturnUserDTO() {
        long userId = 1L;
        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockUserMapper.toUserDto(user)).thenReturn(responseUserDto);

        UserDto actualDto = userService.getUserById(userId);

        assertEquals(responseUserDto, actualDto);
    }

    @Test
    void readTest_whenUserNotFound_thenEntityNotFoundExceptionThrown() {
        long userId = 1L;
        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void updateTest_whenUserNotFound_thenEntityNotFoundExceptionThrown() {
        long userId = 2L;
        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(requestUserDto, userId));
        verify(mockUserRepository, never()).save(user);
    }

    @Test
    void deleteTest_whenInvoke_thenCheckInvocationOfIt() {
        long userId = 1L;

        userService.deleteUserById(userId);

        verify(mockUserRepository).deleteById(userId);
    }

    @Test
    void findAllTest_whenInvoke_thenReturnListOfUsers() {
        when(mockUserRepository.findAll()).thenReturn(List.of(user));
        when(mockUserMapper.toUserDto(user)).thenReturn(responseUserDto);

        Collection<UserDto> actualUsers = userService.getAllUsers();

        assertEquals(List.of(responseUserDto), actualUsers);
    }
}