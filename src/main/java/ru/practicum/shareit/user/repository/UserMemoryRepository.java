package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.DuplicateEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserMemoryRepository {
    private Long increment = 0L;
    private final Map<Long, User> data = new HashMap<>();

    public User addUser(User user) {
        checkUserExists(user);
        user.setId(generateId());
        data.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        checkUserExists(user);
        User updatedUser = getById(user.getId());
        if (!Objects.isNull(user.getName())) {
            updatedUser.setName(user.getName());
        }
        if (!Objects.isNull(user.getEmail())) {
            updatedUser.setEmail(user.getEmail());
        }
        return updatedUser;
    }

    public User getById(Long userId) {
        User user = data.get(userId);

        if (user == null) {
            throw new UserNotFoundException("User with id '" + userId + "' not found");
        }

        return user;
    }

    public void deleteUser(Long userId) {
        data.remove(userId);
    }

    private void checkUserExists(User user) {
        Optional<User> searchUser = data.values().stream()
                .filter(findUser -> !Objects.isNull(user.getEmail()) && findUser.getEmail().equals(user.getEmail()) && !findUser.getId().equals(user.getId()))
                .findFirst();

        if (searchUser.isPresent()) {
            throw new DuplicateEmailException("User with email already exists");
        }
    }

    private Long generateId() {
        return ++increment;
    }
}
