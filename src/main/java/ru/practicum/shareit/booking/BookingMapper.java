package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    public static Booking toEntity(BookingDto dto) {
        if (dto == null) {
            return null;
        }

        return Booking.builder()
                .id(dto.getId())
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(dto.getItemId() != null
                        ? Item.builder().id(dto.getItemId()).build()
                        : null
                )
                .booker(dto.getBookerId() != null
                        ? User.builder().id(dto.getBookerId()).build()
                        : null
                )
                .status(dto.getStatus())
                .build();
    }

    public static BookingDto toDto(Booking model) {
        if (model == null) {
            return null;
        }

        return BookingDto.builder()
                .id(model.getId())
                .start(model.getStart())
                .end(model.getEnd())
                .itemId(model.getItem() != null
                        ? model.getItem().getId()
                        : null
                )
                .bookerId(model.getBooker() != null
                        ? model.getBooker().getId()
                        : null
                )
                .status(model.getStatus())
                .build();
    }

    public static List<BookingDto> toDto(List<Booking> models) {
        if (models == null) {
            return new ArrayList<>();
        }

        return models.stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    public static List<Booking> toEntity(List<BookingDto> dtoList) {
        if (dtoList == null) {
            return new ArrayList<>();
        }

        return dtoList.stream()
                .map(BookingMapper::toEntity)
                .toList();
    }
}
