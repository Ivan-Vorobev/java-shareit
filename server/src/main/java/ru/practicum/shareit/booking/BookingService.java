package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enumiration.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public BookingDto addBooking(BookingDto bookingDto, Long bookerId) {
        userService.getById(bookerId);
        Optional<Item> item = itemRepository.findById(bookingDto.getItemId());

        if (item.isEmpty()) {
            throw new NotFoundException("Item " + bookingDto.getItemId() + " not found");
        }

        if (!item.get().getAvailable()) {
            throw new BadRequestException("Item " + bookingDto.getItemId() + " unavailable");
        }

        bookingDto.setId(null);
        bookingDto.setBookerId(bookerId);
        bookingDto.setStatus(BookingStatus.WAITING);

        Booking booking = bookingRepository.save(BookingMapper.toEntity(bookingDto));
        booking.setItem(item.get());

        return BookingMapper.toDto(booking);
    }

    public BookingDto approveBooking(Long userId, Long bookingId, Boolean isApproved) {
        Booking booking = findBooking(bookingId);

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new BadRequestException("User " + userId + " is not owner");
        }

        booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = findBooking(bookingId);

        if (
                !booking.getItem().getOwner().getId().equals(userId)
                        && !booking.getBooker().getId().equals(userId)
        ) {
            throw new BadRequestException("User " + userId + " is not owner or booker");
        }

        return BookingMapper.toDto(booking);
    }

    public List<BookingDto> getAllBooking(Long userId, Optional<BookingStatus> state) {
        User user = userService.findById(userId);

        List<Booking> bookings;
        if (state.isEmpty() || state.get().equals(BookingStatus.ALL)) {
            bookings = bookingRepository.findAllByBookerInOrderByStartDesc(List.of(user));
        } else {
            bookings = bookingRepository.findAllByBookerInAndStatusOrderByStartDesc(List.of(user), state.get());
        }

        return BookingMapper.toDto(bookings);
    }

    public List<BookingDto> getAllOwnerBooking(Long userId, Optional<BookingStatus> state) {

        User user = userService.findById(userId);

        List<Booking> bookings;
        if (state.isEmpty() || state.get().equals(BookingStatus.ALL)) {
            bookings = bookingRepository.findAllByItemOwnerOrderByStartDesc(user);
        } else {
            bookings = bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(user, state.get());
        }

        return BookingMapper.toDto(bookings);
    }

    public Booking findBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id '" + bookingId + "' not found"));
    }
}
