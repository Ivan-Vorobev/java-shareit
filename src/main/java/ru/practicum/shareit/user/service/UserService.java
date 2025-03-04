package ru.practicum.shareit.user.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserMemoryRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMemoryRepository repository;

    public UserDto addUser(UserDto userDto) {
        if (userDto == null) {
            throw new ValidationException("User is null");
        }

        return UserMapper.dtoFromModel(repository.addUser(UserMapper.modelFromDto(userDto)));
    }

    public UserDto updateUser(UserDto userDto) {
        if (userDto == null) {
            throw new ValidationException("User is null");
        }

        return UserMapper.dtoFromModel(repository.updateUser(UserMapper.modelFromDto(userDto)));
    }

    public UserDto getById(Long userId) {
        if (userId == null) {
            throw new ValidationException("User id is null");
        }

        return UserMapper.dtoFromModel(repository.getById(userId));
    }

    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new ValidationException("User id is null");
        }

        repository.deleteUser(userId);
    }
}
