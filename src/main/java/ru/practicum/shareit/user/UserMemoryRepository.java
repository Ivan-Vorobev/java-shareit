package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
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
        Optional<User> updatedUser = getById(user.getId());

        if (updatedUser.isPresent()) {
            if (!Objects.isNull(user.getName())) {
                updatedUser.get().setName(user.getName());
            }
            if (!Objects.isNull(user.getEmail())) {
                updatedUser.get().setEmail(user.getEmail());
            }
            return updatedUser.get();
        }

        return null;
    }

    public Optional<User> getById(Long userId) {
        User user = data.get(userId);

        return user == null ? Optional.empty() : Optional.of(user);
    }

    public void deleteUser(Long userId) {
        data.remove(userId);
    }

    private void checkUserExists(User user) {
        Optional<User> searchUser = data.values().stream()
                .filter(findUser -> !Objects.isNull(user.getEmail()) && findUser.getEmail().equals(user.getEmail()) && !findUser.getId().equals(user.getId()))
                .findFirst();

        if (searchUser.isPresent()) {
            throw new ConflictException("User with email already exists");
        }
    }

    private Long generateId() {
        return ++increment;
    }
}
