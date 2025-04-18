package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enumiration.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingService bookingService;
    private final CommentRepository commentRepository;

    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        UserDto owner = userService.getById(ownerId);

        return ItemMapper.toDto(itemRepository.save(ItemMapper.toEntity(itemDto, owner)));
    }

    public CommentDto addComment(CommentDto commentDto, Long itemId, Long authorId) {
        User user = userService.findById(authorId);
        LocalDateTime currentTime = LocalDateTime.now();
        bookingService.getAllBooking(authorId, Optional.of(BookingStatus.APPROVED))
                .stream()
                .filter(v -> v.getItemId().equals(itemId) && currentTime.isAfter(v.getEnd()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("You don't booking this itemId: " + itemId));

        commentDto.setId(null);
        commentDto.setItemId(itemId);
        commentDto.setAuthorId(user.getId());
        commentDto.setAuthorName(user.getName());
        commentDto.setCreated(LocalDateTime.now());

        return CommentMapper.toDto(commentRepository.save(CommentMapper.toEntity(commentDto)));
    }

    public ItemDto updateItem(ItemDto itemDto, Long ownerId) {
        UserDto owner = userService.getById(ownerId);
        Item item = findItem(itemDto.getId());
        Item updatedItem = ItemMapper.toEntity(itemDto, owner);

        if (updatedItem.getName() != null) {
            item.setName(updatedItem.getName());
        }

        if (updatedItem.getDescription() != null) {
            item.setDescription(updatedItem.getDescription());
        }

        if (updatedItem.getAvailable() != null) {
            item.setAvailable(updatedItem.getAvailable());
        }

        itemRepository.save(item);

        return ItemMapper.toDto(item);
    }

    public ItemDto getItem(Long itemId) {
        return ItemMapper.toDto(findItem(itemId));
    }

    public List<ItemDto> getAllItem(Long ownerId) {
        List<ItemDto> items = ItemMapper.toDto(itemRepository.findAllByOwnerId(ownerId));
        List<BookingDto> bookings = bookingService.getAllOwnerBooking(ownerId, Optional.of(BookingStatus.ALL));
        Map<Long, ItemDto> itemsHash = items.stream()
                .collect(Collectors.toMap(ItemDto::getId, i -> i));

        LocalDateTime currentTime = LocalDateTime.now();
        bookings.stream().peek(bookingDto -> {
            if (bookingDto.getStart().isAfter(currentTime)) {
                itemsHash.get(bookingDto.getItemId()).setNextBooking(bookingDto);
            }

            if (
                    Objects.isNull(itemsHash.get(bookingDto.getItemId()).getLastBooking())
                            && bookingDto.getStart().isBefore(currentTime)
            ) {
                itemsHash.get(bookingDto.getItemId()).setLastBooking(bookingDto);
            }
        });

        return items;
    }

    public List<ItemDto> getItemByText(String text) {
        if (Objects.isNull(text) || text.isEmpty()) {
            return List.of();
        }

        return ItemMapper.toDto(itemRepository.findAllByText(text));
    }

    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id '" + itemId + "' not found"));
    }
}
