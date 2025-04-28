package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.RequestMethod;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItem(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.info("Get /items: userId={}", userId);
        return itemClient.getAllItem(userId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(
            @Validated({RequestMethod.Create.class}) @RequestBody ItemDto itemDto,
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.info("POST /items: itemDto {}, userId={}", itemDto, userId);
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @Validated @RequestBody ItemDto itemDto,
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId
    ) {
        log.info("PATCH /items/{}: itemDto {}, userId={}", itemId, itemDto, userId);
        return itemClient.updateItem(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable Long itemId
    ) {
        log.info("GET /items/{}: userId={}", itemId, userId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItem(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam String text
    ) {
        log.info("GET /search: text={}. userId={}", text, userId);
        return itemClient.getItemByText(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @Validated @RequestBody CommentDto commentDto,
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId
    ) {
        log.info("POST /{}/comment: commentDto {}. userId={}", itemId, commentDto, userId);
        return itemClient.addComment(itemId, userId, commentDto);
    }
}
