package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto addUser(UserDto userDto) {
        if (userDto == null) {
            throw new ValidationException("User is null");
        }

        return UserMapper.toDto(userRepository.save(UserMapper.toEntity(userDto)));
    }

    public UserDto updateUser(UserDto userDto) {
        if (userDto == null) {
            throw new ValidationException("User is null");
        }

        User user = findById(userDto.getId());
        User updatedUser = UserMapper.toEntity(userDto);

        if (!Objects.isNull(user)) {
            if (!Objects.isNull(updatedUser.getName())) {
                user.setName(updatedUser.getName());
            }
            if (!Objects.isNull(updatedUser.getEmail())) {
                user.setEmail(updatedUser.getEmail());
            }
            userRepository.save(user);
        }

        return UserMapper.toDto(user);
    }

    public UserDto getById(Long userId) {
        return UserMapper.toDto(findById(userId));
    }

    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new ValidationException("User id is null");
        }

        userRepository.deleteById(userId);
    }

    public User findById(Long userId) {
        if (userId == null) {
            throw new ValidationException("User id is null");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id '" + userId + "' not found"));
    }
}
