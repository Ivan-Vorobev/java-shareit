package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void addUser_shouldThrowException() {
        Assertions.assertThrows(RuntimeException.class, () -> userService.addUser(null));
    }

    @Test
    void addUser_shouldAddUser() {
        UserDto userDto = UserDto.builder().name("Ivan").email("ivan@test.com").build();
        User user = User.builder().name("Ivan").email("ivan@test.com").build();
        User createdUser = User.builder().id(10L).name("Ivan").email("ivan@test.com").build();

        Mockito.when(userRepository.save(user)).thenReturn(createdUser);

        UserDto createdUserDto = userService.addUser(userDto);

        Assertions.assertNotNull(createdUserDto);
        Assertions.assertEquals(createdUserDto.getId(), createdUser.getId());
        Assertions.assertEquals(createdUserDto.getName(), createdUser.getName());
        Assertions.assertEquals(createdUserDto.getEmail(), createdUser.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUser_shouldThrowError() {
        Long userId = 17L;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UserDto userDto = UserDto.builder().id(userId).build();

        Assertions.assertThrows(BadRequestException.class, () -> userService.updateUser(null));

        Assertions.assertThrows(BadRequestException.class, () -> userService.updateUser(UserDto.builder().build()));

        Assertions.assertThrows(NotFoundException.class, () -> userService.updateUser(userDto));
    }

    @Test
    void updateUser_shouldUpdateUser() {
        Long userId = 17L;
        User user = User.builder().id(userId).name("Name1").email("email1@test.com").build();
        User updatedUser = User.builder().id(userId).name("Name2").email("email2@test.com").build();
        UserDto userDto = UserDto.builder().id(userId).name("Name2").email("email2@test.com").build();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        UserDto updatedDataUserDto = userService.updateUser(userDto);
        Assertions.assertEquals(updatedDataUserDto.getId(), userDto.getId());
        Assertions.assertEquals(updatedDataUserDto.getName(), userDto.getName());
        Assertions.assertEquals(updatedDataUserDto.getEmail(), userDto.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getById_shouldGetById() {
        Long userId = 17L;
        User user = User.builder().id(userId).name("Name1").email("email1@test.com").build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto updatedDataUserDto = userService.getById(userId);
        Assertions.assertEquals(updatedDataUserDto.getId(), user.getId());
        Assertions.assertEquals(updatedDataUserDto.getName(), user.getName());
        Assertions.assertEquals(updatedDataUserDto.getEmail(), user.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUser_shouldThrowError() {
        Assertions.assertThrows(BadRequestException.class, () -> userService.updateUser(null));
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        Long userId = 17L;

        Assertions.assertDoesNotThrow(() -> userService.deleteUser(userId));

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}