package ru.practicum.shareit.item;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static Item toEntity(ItemDto dto, UserDto owner) {
        if (dto == null) {
            return null;
        }

        return Item.builder()
                .id(dto.getId())
                .owner(UserMapper.toEntity(owner))
                .request(dto.getRequestId() != null
                        ? ItemRequest.builder().id(dto.getRequestId()).build()
                        : null
                )
                .description(dto.getDescription())
                .name(dto.getName())
                .available(dto.getAvailable())
                .build();
    }

    public static ItemDto toDto(Item model) {
        if (model == null) {
            return null;
        }

        return ItemDto.builder()
                .id(model.getId())
                .ownerId(model.getOwner() != null
                        ? model.getOwner().getId()
                        : null
                )
                .requestId(model.getRequest() != null
                        ? model.getRequest().getId()
                        : null
                )
                .description(model.getDescription())
                .name(model.getName())
                .available(model.getAvailable())
                .build();
    }

    public static List<ItemDto> toDto(List<Item> models) {
        if (models == null) {
            return new ArrayList<>();
        }

        return models.stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}
