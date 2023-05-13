package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(value = "/testSchema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserMapperTest {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    User user;
    UserDto requestUserDto, responseUserDto;

    @BeforeEach
    void setUp() {
        user = userRepository.findById(1L).get();

        responseUserDto = new UserDto();
        responseUserDto.setId(1L);
        responseUserDto.setName("user_1");
        responseUserDto.setEmail("user_1@mail.ru");

        requestUserDto = new UserDto();
        requestUserDto.setName("user1");
    }

    @Test
    void mapTest_UserToUserDto() {
        UserDto userDto = userMapper.toUserDto(user);

        assertEquals(responseUserDto, userDto);
    }

    @Test
    void mapTest_whenUserIsNullReturnUserDtoNull() {
        UserDto actual = userMapper.toUserDto((User) null);

        assertNull(actual);
    }

    @Test
    void mapTest_UserDtoToUser() {
        User user1 = userMapper.toUser(responseUserDto);

        assertEquals(user, user1);
    }

    @Test
    void mapTest_whenUserDtoIsNullReturnUserNull() {
        User actual = userMapper.toUser((UserDto) null);

        assertNull(actual);
    }

}