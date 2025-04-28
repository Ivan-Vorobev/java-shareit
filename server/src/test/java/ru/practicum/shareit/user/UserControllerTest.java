package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = {UserController.class})
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void createUser_shouldCreateUserAndResponseHTTP200() throws Exception {
        UserDto userDto = UserDto.builder().name("Ivan").email("ivan@test.com").build();
        UserDto createdUserDto = UserDto.builder().id(10L).name("Ivan").email("ivan@test.com").build();

        Mockito.when(userService.addUser(userDto)).thenReturn(createdUserDto);

        mockMvc
                .perform(
                        post("/users")
                                .content(mapper.writeValueAsString(userDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUserDto.getId()))
                .andExpect(jsonPath("$.name").value(createdUserDto.getName()))
                .andExpect(jsonPath("$.email").value(createdUserDto.getEmail()));
    }

    @Test
    void getUser_shouldGetUserAndResponseHTTP200() throws Exception {
        UserDto userDto = UserDto.builder().id(10L).name("Ivan").email("ivan@test.com").build();

        Mockito.when(userService.getById(userDto.getId())).thenReturn(userDto);

        mockMvc
                .perform(
                        get("/users/" + userDto.getId())
                                .content(mapper.writeValueAsString(userDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void updateUser_shouldUpdateUserAndResponseHTTP200() throws Exception {
        UserDto userDto = UserDto.builder().id(10L).name("New Ivan").build();
        UserDto updatedUserDto = UserDto.builder().id(10L).name("New Ivan").email("ivan@test.com").build();

        Mockito.when(userService.updateUser(userDto)).thenReturn(updatedUserDto);

        mockMvc
                .perform(
                        patch("/users/" + updatedUserDto.getId())
                                .content(mapper.writeValueAsString(userDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUserDto.getId()))
                .andExpect(jsonPath("$.name").value(updatedUserDto.getName()))
                .andExpect(jsonPath("$.email").value(updatedUserDto.getEmail()));
    }

    @Test
    void deleteUser_shouldDeleteUserAndResponseHTTP200() throws Exception {
        mockMvc
                .perform(
                        delete("/users/10")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
