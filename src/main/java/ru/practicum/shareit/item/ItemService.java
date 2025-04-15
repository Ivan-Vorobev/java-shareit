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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        UserDto owner = userService.getById(ownerId);

        return ItemMapper.toDto(itemRepository.save(ItemMapper.toEntity(itemDto, owner)));
    }

    public ItemDto updateItem(ItemDto itemDto, Long ownerId) {
        UserDto owner = userService.getById(ownerId);
        Item item = findItem(itemDto.getId(), ownerId);
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

    public ItemDto getItem(Long itemId, Long ownerId) {
        return ItemMapper.toDto(findItem(itemId, ownerId));
    }

    public List<ItemDto> getAllItem(Long ownerId) {
        return ItemMapper.toDto(itemRepository.findAllByOwnerIdAndAvailableIsTrue(ownerId));
    }

    public List<ItemDto> getItemByText(String text, Long ownerId) {
        UserDto owner = userService.getById(ownerId);

        if (Objects.isNull(text) || text.isEmpty()) {
            return List.of();
        }

        return ItemMapper.toDto(itemRepository.findAllByText(text, owner.getId()));
    }

    private Item findItem(Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id '" + itemId + "' not found"));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ConflictException("Owner and item owner does not match");
        }

        return item;
    }
}
