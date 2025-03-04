package ru.practicum.shareit.request.mapper;


import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

public class RequestMapper {
    public static Request modelFromDto(ItemRequestDto dto) {

        return Request.builder()
                .id(dto.getId())
                .created(dto.getCreated())
                .requestor(dto.getRequestor() != null
                        ? User.builder().id(dto.getRequestor()).build()
                        : null
                )
                .description(dto.getDescription())
                .build();
    }

    public static ItemRequestDto dtoFromModel(Request model) {

        return ItemRequestDto.builder()
                .id(model.getId())
                .created(model.getCreated())
                .requestor(model.getRequestor() != null ? model.getRequestor().getId() : null)
                .description(model.getDescription())
                .build();
    }
}
