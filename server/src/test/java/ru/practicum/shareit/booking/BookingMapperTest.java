package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    @Test
    void toEntity() {
        Booking booking = BookingMapper.toEntity(BookingDto.builder().id(1L).build());
        assertNull(BookingMapper.toEntity((BookingDto) null));
        assertNull(booking.getItem());
        assertNull(booking.getBooker());
    }

    @Test
    void toDto() {
        assertNull(BookingMapper.toDto((Booking) null));
    }

    @Test
    void testToDto() {
        assertEquals(BookingMapper.toDto((List<Booking>) null), List.of());
    }

    @Test
    void testToEntity() {
        assertEquals(BookingMapper.toEntity((List<BookingDto>) null), List.of());
        assertEquals(BookingMapper.toEntity(
                        List.of(BookingDto.builder().id(1L).build())),
                List.of(Booking.builder().id(1L).build())
        );
    }
}