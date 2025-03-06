package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMemoryRepository repository;

    public UserDto addUser(UserDto userDto) {
        if (userDto == null) {
            throw new ValidationException("User is null");
        }

        return UserMapper.toDto(repository.addUser(UserMapper.toEntity(userDto)));
    }

    public UserDto updateUser(UserDto userDto) {
        if (userDto == null) {
            throw new ValidationException("User is null");
        }

        getById(userDto.getId());

        return UserMapper.toDto(repository.updateUser(UserMapper.toEntity(userDto)));
    }

    public UserDto getById(Long userId) {
        if (userId == null) {
            throw new ValidationException("User id is null");
        }

        return UserMapper.toDto(
                repository.getById(userId)
                        .orElseThrow(() -> new NotFoundException("User with id '" + userId + "' not found"))
        );
    }

    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new ValidationException("User id is null");
        }

        repository.deleteUser(userId);
    }
}
