package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemService.getAllItem(ownerId);
    }

    @PostMapping
    public ItemDto addItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long itemId
    ) {
        return itemService.updateItem(itemId, itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long itemId
    ) {
        return itemService.getItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam String text
    ) {
        return itemService.getItemByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestBody CommentDto commentDto,
            @RequestHeader("X-Sharer-User-Id") Long authorId,
            @PathVariable Long itemId
    ) {
        return itemService.addComment(commentDto, itemId, authorId);
    }
}
