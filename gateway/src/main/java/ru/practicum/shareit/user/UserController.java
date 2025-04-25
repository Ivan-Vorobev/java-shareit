package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.RequestMethod;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(
            @Valid @NotNull @PathVariable Long userId
    ) {
        log.info("GET /users/{}", userId);
        return userClient.getById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Validated({RequestMethod.Create.class}) @RequestBody UserDto userDto) {
        log.info("POST /users: userDto {}", userDto);
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @Validated({RequestMethod.Update.class}) @RequestBody UserDto userDto,
            @PathVariable Long userId
    ) {
        log.info("PATCH /users/{}: userDto {}", userId, userDto);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("DELETE /users/{}", userId);
        userClient.deleteUser(userId);
    }
}
