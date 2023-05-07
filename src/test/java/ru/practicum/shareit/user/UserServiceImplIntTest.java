package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = "/testSchema.sql")
public class UserServiceImplIntTest {
    UserDto requestUserDto;
    UserDto responseUserDto;
    UserDto responseUserDto1;
    long userId = 1L;
    private final UserServiceImpl mockUserServiceImpl;


    @BeforeEach
    void setUp() {
        requestUserDto = UserDto.builder().name("vasya").email("vasya@mail.ru").build();

        responseUserDto = UserDto.builder().id(4L).name("vasya").email("vasya@mail.ru").build();

        responseUserDto1 = UserDto.builder().id(1L).name("user_1").email("user_1@mail.ru").build();
    }

    @Test
    void createUser_checkData() {

        UserDto userDto = mockUserServiceImpl.createUser(requestUserDto);
        assertEquals(responseUserDto, userDto);

    }

    @Test
    void getAllUsers_checkData() {

        List<UserDto> userDtos = mockUserServiceImpl.getAllUsers();
        assertEquals(3, userDtos.size());
    }

    @Test
    void getUserById_checkData() {

        UserDto userDto = mockUserServiceImpl.getUserById(userId);
        assertEquals(responseUserDto1, userDto);
    }

    @Test
    void updateUser_checkData() {
        requestUserDto.setName("Update name");

        UserDto userDto = mockUserServiceImpl.updateUser(requestUserDto, userId);
        assertEquals(requestUserDto, userDto);
    }

    @Test
    void deleteUserById_checkData() {
        mockUserServiceImpl.deleteUserById(userId);

        List<UserDto> userDtos = mockUserServiceImpl.getAllUsers();
        assertEquals(2, userDtos.size());
    }
}
