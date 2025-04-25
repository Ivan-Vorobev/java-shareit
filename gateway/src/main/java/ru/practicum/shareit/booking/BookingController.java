package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(
			@Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = checkState(stateParam);
		log.info("GET /bookings: state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(
			@Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId,
			@RequestBody @Valid BookItemRequestDto requestDto
	) {
		log.info("POST /bookings: requestDto {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(
			@Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId,
			@Valid @NotNull @PathVariable Long bookingId
	) {
		log.info("GET /bookings/{}: userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllOwnerBooking(
			@Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam
	) {
		BookingState state = checkState(stateParam);
		log.info("GET /bookings/owner?state={}: userId={}", stateParam, userId);
		return bookingClient.getAllOwnerBooking(userId, state);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(
			@Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId,
			@Valid @NotNull @PathVariable() long bookingId,
			@Valid @NotNull @RequestParam boolean approved
	) {
		log.info("PATCH /bookings/{}: approved={}, userId={}", bookingId, approved, userId);
		return bookingClient.approveBooking(userId, bookingId, approved);
	}

	private BookingState checkState(String stateParam) {
		return BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
	}
}
