package ru.practicum.shareit.request;


import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

public class RequestMapper {
    public static ItemRequest toEntity(ItemRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return ItemRequest.builder()
                .id(dto.getId())
                .created(dto.getCreated())
                .requestorId(dto.getRequestorId() != null
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
                .requestorId(model.getRequestorId() != null ? model.getRequestorId().getId() : null)
                .description(model.getDescription())
                .build();
    }
}
