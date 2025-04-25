package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.enumiration.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerInOrderByStartDesc(List<User> booker);

    List<Booking> findAllByBookerInAndStatusOrderByStartDesc(List<User> booker, BookingStatus status);

    List<Booking> findAllByItemOwnerOrderByStartDesc(User itemOwner);

    List<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(User itemOwner, BookingStatus status);
}
