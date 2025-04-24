package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addRequest(
            @Validated @RequestBody ItemRequestDto itemRequestDto,
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        itemRequestDto.setRequestorId(ownerId);
        return itemRequestService.add(itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> findAllRequestorItems(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemRequestService.findAllRequestorItems(ownerId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllUserRequests(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemRequestService.findAllUserRequests(ownerId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(
            @Valid @NotNull @PathVariable Long requestId,
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemRequestService.findById(requestId);
    }
}
