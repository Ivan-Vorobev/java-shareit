package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
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
            @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        itemRequestDto.setRequestorId(ownerId);
        return itemRequestService.add(itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> findAllRequestorItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemRequestService.findAllRequestorItems(ownerId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllUserRequests(
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemRequestService.findAllUserRequests(ownerId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(
            @PathVariable Long requestId,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemRequestService.findById(requestId);
    }
}
