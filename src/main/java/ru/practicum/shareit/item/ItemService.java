package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemMemoryRepository itemRepository;
    private final UserService userService;

    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        UserDto owner = userService.getById(ownerId);

        return ItemMapper.toDto(itemRepository.addItem(ItemMapper.toEntity(itemDto, owner)));
    }

    public ItemDto updateItem(ItemDto itemDto, Long ownerId) {
        UserDto owner = userService.getById(ownerId);
        getItem(itemDto.getId(), ownerId);

        return ItemMapper.toDto(itemRepository.updateItem(ItemMapper.toEntity(itemDto, owner)));
    }

    public ItemDto getItem(Long itemId, Long ownerId) {
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id '" + itemId + "' not found"));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ConflictException("Owner and item owner does not match");
        }

        return ItemMapper.toDto(item);
    }

    public List<ItemDto> getAllItem(Long ownerId) {
        return ItemMapper.toDto(itemRepository.getAll(ownerId));
    }

    public List<ItemDto> getItemByText(String text) {
        return ItemMapper.toDto(itemRepository.searchAll(text));
    }
}
