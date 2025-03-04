package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ConflictException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemMemoryRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemMemoryRepository itemRepository;
    private final UserService userService;

    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        UserDto owner = userService.getById(ownerId);

        return ItemMapper.dtoFromModel(itemRepository.addItem(ItemMapper.modelFromDto(itemDto, owner)));
    }

    public ItemDto updateItem(ItemDto itemDto, Long ownerId) {
        UserDto owner = userService.getById(ownerId);
        getItem(itemDto.getId(), ownerId);

        return ItemMapper.dtoFromModel(itemRepository.updateItem(ItemMapper.modelFromDto(itemDto, owner)));
    }

    public ItemDto getItem(Long itemId, Long ownerId) {
        Item item = itemRepository.getItemById(itemId);

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ConflictException("Owner and item owner does not match");
        }

        return ItemMapper.dtoFromModel(item);
    }

    public List<ItemDto> getAllItem(Long ownerId) {
        return ItemMapper.dtoFromModel(itemRepository.getAll(ownerId));
    }

    public List<ItemDto> getItemByText(String text) {
        return ItemMapper.dtoFromModel(itemRepository.searchAll(text));
    }
}
