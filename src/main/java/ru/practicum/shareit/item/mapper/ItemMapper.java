package ru.practicum.shareit.item.mapper;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static Item modelFromDto(ItemDto dto, UserDto owner) {

        return Item.builder()
                .id(dto.getId())
                .owner(UserMapper.modelFromDto(owner))
                .request(dto.getRequest() != null
                        ? Request.builder().id(dto.getRequest()).build()
                        : null
                )
                .description(dto.getDescription())
                .name(dto.getName())
                .available(dto.getAvailable())
                .build();
    }

    public static ItemDto dtoFromModel(Item model) {

        return ItemDto.builder()
                .id(model.getId())
                .owner(model.getOwner() != null
                        ? model.getOwner().getId()
                        : null
                )
                .request(model.getRequest() != null
                        ? model.getRequest().getId()
                        : null
                )
                .description(model.getDescription())
                .name(model.getName())
                .available(model.getAvailable())
                .build();
    }

    public static List<ItemDto> dtoFromModel(List<Item> models) {
        if (models == null) {
            return new ArrayList<>();
        }

        return models.stream()
                .map(ItemMapper::dtoFromModel)
                .toList();
    }
}
