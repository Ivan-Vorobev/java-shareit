package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.RequestMethod;
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
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemService.getAllItem(ownerId);
    }

    @PostMapping
    public ItemDto addItem(@Validated({RequestMethod.Create.class}) @RequestBody ItemDto itemDto, @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @Validated @RequestBody ItemDto itemDto,
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Valid @NotNull @PathVariable Long itemId
    ) {
        itemDto.setId(itemId);
        return itemService.updateItem(itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Valid @NotNull @PathVariable Long itemId
    ) {
        return itemService.getItem(itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItem(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Valid @NotNull @RequestParam String text
    ) {
        return itemService.getItemByText(text, ownerId);
    }
}
