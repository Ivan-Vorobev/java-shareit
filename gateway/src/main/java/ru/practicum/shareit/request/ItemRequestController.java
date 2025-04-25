package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(
            @Validated @RequestBody ItemRequestDto itemRequestDto,
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.info("POST /requests: itemRequestDto {}. userId={}", itemRequestDto, userId);
        return itemRequestClient.add(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllRequestorItems(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.info("GET /requests: userId={}", userId);
        return itemRequestClient.findAllRequestorItems(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllUserRequests(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.info("GET /requests/all: userId={}", userId);
        return itemRequestClient.findAllUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(
            @PathVariable long requestId,
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.info("GET /requests/{}: userId={}", requestId, userId);
        return itemRequestClient.findById(userId, requestId);
    }
}
