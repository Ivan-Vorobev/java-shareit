package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.enumiration.BookingStatus;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    private Long id;
    private LocalDate start;
    private LocalDate end;
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;
}
