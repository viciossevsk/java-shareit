package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserService mockUserService;
    @Autowired
    private MockMvc mvc;
    private long userId = 1L;

    private UserDto userDtoRequest = UserDto.builder()
            .name("name")
            .email("aaaa@fdf.ru")
            .build();

    private UserDto userDtoResponse = UserDto.builder()
            .id(1L)
            .name("name")
            .email("aaaa@fdf.ru")
            .build();

    @Test
    void createUser_thenRequest_whenResponseOk() throws Exception {

        when(mockUserService.createUser(any(UserDto.class)))
                .thenReturn(userDtoResponse);

        mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDtoRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoResponse.getName().toString())))
                .andExpect(jsonPath("$.email", is(userDtoResponse.getEmail().toString())));

        verify(mockUserService).createUser(any(UserDto.class));
    }

    @Test
    void createUser_thenBigNameSize_when() throws Exception {
        userDtoRequest.setName("A very long sentence in English that takes up a lot of space");

        when(mockUserService.createUser(any(UserDto.class)))
                .thenReturn(userDtoResponse);

        mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDtoRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(mockUserService, never()).createUser(any(UserDto.class));
    }

    @Test
    void createUser_thenEmailFail_whenResponseOk() throws Exception {
        userDtoRequest.setEmail("7687678@777");

        when(mockUserService.createUser(any(UserDto.class)))
                .thenReturn(userDtoResponse);

        mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDtoRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(mockUserService, never()).createUser(any(UserDto.class));
    }

    @Test
    void getAllUsers_thenRequest_whenResponseOk() throws Exception {

        when(mockUserService.getAllUsers())
                .thenReturn(List.of(userDtoResponse));

        mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDtoRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockUserService, never()).getAllUsers();
    }

    @Test
    void getUserById_thenUserId_1_whenUserId_1() throws Exception {

        when(mockUserService.getUserById(anyLong()))
                .thenReturn(userDtoResponse);

        mvc.perform(get("/users/{id}", userId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) userId)))
                .andExpect(jsonPath("$.name", is(userDtoResponse.getName().toString())))
                .andExpect(jsonPath("$.email", is(userDtoResponse.getEmail().toString())));

        verify(mockUserService).getUserById(anyLong());
    }

    @Test
    void updateUser_thenRequest_whenResponseOk() throws Exception {

        when(mockUserService.updateUser(any(UserDto.class), anyLong()))
                .thenReturn(userDtoResponse);

        mvc.perform(patch("/users/{id}", userId)
                            .content(mapper.writeValueAsString(userDtoRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockUserService).updateUser(any(UserDto.class), anyLong());
    }
}
