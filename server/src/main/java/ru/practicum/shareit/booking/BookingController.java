package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enumiration.BookingStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getAllBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state") Optional<BookingStatus> state,
            @RequestParam(name = "from") Integer from,
            @RequestParam(name = "size") Integer size
    ) {
        return bookingService.getAllBooking(userId, state);
    }

    @PostMapping
    public BookingDto addBooking(
            @RequestBody BookingDto bookingDto,
            @RequestHeader("X-Sharer-User-Id") Long bookerId
    ) {
        return bookingService.addBooking(bookingDto, bookerId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam Optional<BookingStatus> state
    ) {
        return bookingService.getAllOwnerBooking(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved
    ) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }
}
