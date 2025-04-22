package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.RequestMethod;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enumiration.BookingStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(
            @Validated({RequestMethod.Create.class}) @RequestBody BookingDto bookingDto,
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long bookerId
    ) {
        return bookingService.addBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @NotNull @PathVariable Long bookingId,
            @Valid @NotNull @RequestParam Boolean approved
    ) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @NotNull @PathVariable Long bookingId
    ) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllBooking(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestParam Optional<BookingStatus> state
    ) {
        return bookingService.getAllBooking(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBooking(
            @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestParam Optional<BookingStatus> state
    ) {
        return bookingService.getAllOwnerBooking(userId, state);
    }
}
