package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    public ItemRequestDto add(ItemRequestDto itemRequestDto) {
        userService.getById(itemRequestDto.getRequestorId());

        itemRequestDto.setId(null);
        itemRequestDto.setCreated(LocalDateTime.now());

        ItemRequest itemRequest = ItemRequestMapper.toEntity(itemRequestDto);
        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    public ItemRequestDto findById(Long itemRequestId) {
        if (Objects.isNull(itemRequestId)) {
            throw new BadRequestException("ItemRequestId is null");
        }

        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest not found by id " + itemRequestId));

        return ItemRequestMapper.toDto(itemRequest);
    }

    public List<ItemRequestDto> findAllUserRequests(Long requestorId) {
        User user = userService.findById(requestorId);

        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdNotInOrderByCreatedDesc(List.of(user));

        return ItemRequestMapper.toDto(itemRequests);
    }

    public List<ItemRequestDto> findAllRequestorItems(Long requestorId) {
        User user = userService.findById(requestorId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(user.getId());

        return ItemRequestMapper.toDto(itemRequests);
    }
}
