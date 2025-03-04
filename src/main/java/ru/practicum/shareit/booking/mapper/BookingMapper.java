package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking modelFromDto(BookingDto dto) {

        return Booking.builder()
                .id(dto.getId())
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(dto.getItem() != null
                        ? Item.builder().id(dto.getId()).build()
                        : null
                )
                .booker(dto.getBooker() != null
                        ? User.builder().id(dto.getId()).build()
                        : null
                )
                .status(dto.getStatus())
                .build();
    }

    public static BookingDto dtoFromModel(Booking model) {

        return BookingDto.builder()
                .id(model.getId())
                .start(model.getStart())
                .end(model.getEnd())
                .item(model.getItem() != null
                        ? model.getItem().getId()
                        : null
                )
                .booker(model.getBooker() != null
                        ? model.getBooker().getId()
                        : null
                )
                .status(model.getStatus())
                .build();
    }
}
