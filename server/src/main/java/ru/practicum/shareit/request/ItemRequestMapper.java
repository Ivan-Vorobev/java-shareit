package ru.practicum.shareit.request;


import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest toEntity(ItemRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return ItemRequest.builder()
                .id(dto.getId())
                .created(dto.getCreated())
                .requestor(dto.getRequestorId() != null
                        ? User.builder().id(dto.getRequestorId()).build()
                        : null
                )
                .description(dto.getDescription())
                .build();
    }

    public static ItemRequestDto toDto(ItemRequest model) {
        if (model == null) {
            return null;
        }

        return ItemRequestDto.builder()
                .id(model.getId())
                .created(model.getCreated())
                .requestorId(model.getRequestor() != null ? model.getRequestor().getId() : null)
                .description(model.getDescription())
                .items(ItemMapper.toDto(model.getItems()))
                .build();
    }

    public static List<ItemRequestDto> toDto(List<ItemRequest> models) {
        if (models == null) {
            return new ArrayList<>();
        }

        return models.stream()
                .map(ItemRequestMapper::toDto)
                .toList();
    }
}
