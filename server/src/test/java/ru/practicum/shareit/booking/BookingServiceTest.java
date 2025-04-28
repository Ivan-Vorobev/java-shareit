package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enumiration.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private BookingService bookingService;

    @Test
    void addBooking_shouldThrowError() {
        Long userId = 1L;
        UserDto user = UserDto.builder().id(userId).build();

        Mockito.when(userService.getById(any())).thenReturn(user);
        Mockito.when(itemRepository.findById(10L)).thenReturn(Optional.empty());
        Mockito.when(itemRepository.findById(15L)).thenReturn(Optional.of(Item.builder().available(false).build()));

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.addBooking(BookingDto.builder().itemId(10L).build(), userId));

        Assertions.assertThrows(BadRequestException.class, () -> bookingService.addBooking(BookingDto.builder().itemId(15L).build(), userId));
    }

    @Test
    void addBooking_shouldAddBooking() {
        Long userId = 1L;
        Long id = 5L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        UserDto user = UserDto.builder().id(userId).build();
        BookingDto bookingDto = BookingDto
                .builder()
                .itemId(15L)
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.APPROVED)
                .build();
        Booking booking = Booking
                .builder()
                .booker(User.builder().id(userId).build())
                .item(Item.builder().id(15L).build())
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.WAITING)
                .build();
        Booking createdBooking = Booking
                .builder()
                .id(id)
                .booker(User.builder().id(userId).build())
                .item(Item.builder().id(15L).build())
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.WAITING)
                .build();

        Mockito.when(userService.getById(any())).thenReturn(user);

        Mockito.when(itemRepository.findById(15L)).thenReturn(Optional.of(Item.builder().available(true).build()));
        Mockito.when(bookingRepository.save(booking)).thenReturn(createdBooking);

        BookingDto createdBookingDto = bookingService.addBooking(bookingDto, userId);

        Assertions.assertNotNull(createdBookingDto);
        Assertions.assertEquals(createdBookingDto.getId(), createdBooking.getId());
        Assertions.assertEquals(createdBookingDto.getBookerId(), createdBooking.getBooker().getId());
        Assertions.assertEquals(createdBookingDto.getItemId(), createdBooking.getItem().getId());
        Assertions.assertEquals(createdBookingDto.getStart(), createdBooking.getStart());
        Assertions.assertEquals(createdBookingDto.getEnd(), createdBooking.getEnd());
        Assertions.assertEquals(createdBookingDto.getStatus(), createdBooking.getStatus());

        Mockito.verify(bookingRepository, Mockito.times(1)).save(booking);
        Mockito.verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void findBooking_shouldThrowError() {
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.findBooking(1L));
    }

    @Test
    void findBooking_shouldFindBooking() {
        Long userId = 1L;
        Long id = 5L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        Booking booking = Booking
                .builder()
                .id(id)
                .booker(User.builder().id(userId).build())
                .item(Item.builder().id(15L).build())
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.WAITING)
                .build();
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertDoesNotThrow(() -> {
            bookingService.findBooking(id);
        });
    }

    @Test
    void approveBooking_shouldThrowError() {
        Long userId = 1L;
        Long id = 5L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        Booking booking = Booking
                .builder()
                .id(id)
                .booker(User.builder().id(userId).build())
                .item(Item.builder().id(15L).owner(User.builder().id(700L).build()).build())
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.WAITING)
                .build();
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(BadRequestException.class, () -> bookingService.approveBooking(userId, 2L, false));
    }

    @Test
    void approveBooking_shouldApproveBooking() {
        Long userId = 1L;
        Long id = 5L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        Booking booking = Booking
                .builder()
                .id(id)
                .booker(User.builder().id(userId).build())
                .item(Item.builder().id(15L).owner(User.builder().id(userId).build()).build())
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.WAITING)
                .build();

        Booking updatedBooking = Booking
                .builder()
                .id(id)
                .booker(User.builder().id(userId).build())
                .item(Item.builder().id(15L).owner(User.builder().id(userId).build()).build())
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.APPROVED)
                .build();
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(any())).thenReturn(updatedBooking);

        BookingDto bookingDto = bookingService.approveBooking(userId, 2L, true);

        Assertions.assertNotNull(bookingDto);
        Assertions.assertEquals(bookingDto.getId(), updatedBooking.getId());
        Assertions.assertEquals(bookingDto.getBookerId(), updatedBooking.getBooker().getId());
        Assertions.assertEquals(bookingDto.getItemId(), updatedBooking.getItem().getId());
        Assertions.assertEquals(bookingDto.getStart(), updatedBooking.getStart());
        Assertions.assertEquals(bookingDto.getEnd(), updatedBooking.getEnd());
        Assertions.assertEquals(bookingDto.getStatus(), updatedBooking.getStatus());

        Mockito.verify(bookingRepository, Mockito.times(1)).save(booking);
        Mockito.verify(bookingRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getBooking_shouldThrowError() {
        Long userId = 1L;
        Long id = 5L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        Booking booking = Booking
                .builder()
                .id(id)
                .booker(User.builder().id(600L).build())
                .item(Item.builder().id(15L).owner(User.builder().id(700L).build()).build())
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.WAITING)
                .build();
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(BadRequestException.class, () -> bookingService.getBooking(userId, 2L));
    }

    @Test
    void getBooking_shouldGetBooking() {
        Long userId = 1L;
        Long bookingId = 5L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        Booking booking = Booking
                .builder()
                .id(bookingId)
                .booker(User.builder().id(userId).build())
                .item(Item.builder().id(15L).owner(User.builder().id(userId).build()).build())
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.WAITING)
                .build();
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertDoesNotThrow(() -> {
            bookingService.getBooking(userId, bookingId);
        });
    }

    @Test
    void getAllBooking_shouldGetBooking() {
        Long userId = 1L;
        Long bookingId = 5L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        User user = User.builder().id(userId).build();
        Booking booking = Booking
                .builder()
                .id(bookingId)
                .booker(user)
                .item(Item.builder().id(15L).owner(User.builder().id(userId).build()).build())
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.WAITING)
                .build();


        Mockito.when(userService.findById(anyLong())).thenReturn(user);
        Mockito.when(bookingRepository.findAllByBookerInOrderByStartDesc(any())).thenReturn(List.of(booking));
        Mockito.when(bookingRepository.findAllByBookerInAndStatusOrderByStartDesc(any(), any())).thenReturn(List.of(booking));

        List<BookingDto> bookingDtoListForStatusAll = bookingService.getAllBooking(userId, Optional.empty());
        List<BookingDto> bookingDtoListForStatusWaiting = bookingService.getAllBooking(userId, Optional.of(BookingStatus.WAITING));

        assertNotNull(bookingDtoListForStatusAll);
        assertEquals(bookingDtoListForStatusAll.size(), 1);
        assertEquals(bookingDtoListForStatusAll.get(0).getId(), booking.getId());
        assertEquals(bookingDtoListForStatusAll.get(0).getBookerId(), booking.getBooker().getId());
        assertEquals(bookingDtoListForStatusAll.get(0).getItemId(), booking.getItem().getId());
        assertEquals(bookingDtoListForStatusAll.get(0).getStart(), booking.getStart());
        assertEquals(bookingDtoListForStatusAll.get(0).getEnd(), booking.getEnd());
        assertEquals(bookingDtoListForStatusAll.get(0).getStatus(), booking.getStatus());

        assertNotNull(bookingDtoListForStatusWaiting);
        assertEquals(bookingDtoListForStatusWaiting.size(), 1);
        assertEquals(bookingDtoListForStatusWaiting.get(0).getId(), booking.getId());
        assertEquals(bookingDtoListForStatusWaiting.get(0).getBookerId(), booking.getBooker().getId());
        assertEquals(bookingDtoListForStatusWaiting.get(0).getItemId(), booking.getItem().getId());
        assertEquals(bookingDtoListForStatusWaiting.get(0).getStart(), booking.getStart());
        assertEquals(bookingDtoListForStatusWaiting.get(0).getEnd(), booking.getEnd());
        assertEquals(bookingDtoListForStatusWaiting.get(0).getStatus(), booking.getStatus());

        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByBookerInOrderByStartDesc(any());
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByBookerInAndStatusOrderByStartDesc(any(), any());
        Mockito.verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getAllOwnerBooking_shouldGetAllOwnerBooking() {
        Long userId = 1L;
        Long bookingId = 5L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        User user = User.builder().id(userId).build();
        Booking booking = Booking
                .builder()
                .id(bookingId)
                .booker(user)
                .item(Item.builder().id(15L).owner(User.builder().id(userId).build()).build())
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.WAITING)
                .build();


        Mockito.when(userService.findById(anyLong())).thenReturn(user);
        Mockito.when(bookingRepository.findAllByItemOwnerOrderByStartDesc(any())).thenReturn(List.of(booking));
        Mockito.when(bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(any(), any())).thenReturn(List.of(booking));

        List<BookingDto> bookingDtoListForStatusAll = bookingService.getAllOwnerBooking(userId, Optional.empty());
        List<BookingDto> bookingDtoListForStatusWaiting = bookingService.getAllOwnerBooking(userId, Optional.of(BookingStatus.WAITING));

        assertNotNull(bookingDtoListForStatusAll);
        assertEquals(bookingDtoListForStatusAll.size(), 1);
        assertEquals(bookingDtoListForStatusAll.get(0).getId(), booking.getId());
        assertEquals(bookingDtoListForStatusAll.get(0).getBookerId(), booking.getBooker().getId());
        assertEquals(bookingDtoListForStatusAll.get(0).getItemId(), booking.getItem().getId());
        assertEquals(bookingDtoListForStatusAll.get(0).getStart(), booking.getStart());
        assertEquals(bookingDtoListForStatusAll.get(0).getEnd(), booking.getEnd());
        assertEquals(bookingDtoListForStatusAll.get(0).getStatus(), booking.getStatus());

        assertNotNull(bookingDtoListForStatusWaiting);
        assertEquals(bookingDtoListForStatusWaiting.size(), 1);
        assertEquals(bookingDtoListForStatusWaiting.get(0).getId(), booking.getId());
        assertEquals(bookingDtoListForStatusWaiting.get(0).getBookerId(), booking.getBooker().getId());
        assertEquals(bookingDtoListForStatusWaiting.get(0).getItemId(), booking.getItem().getId());
        assertEquals(bookingDtoListForStatusWaiting.get(0).getStart(), booking.getStart());
        assertEquals(bookingDtoListForStatusWaiting.get(0).getEnd(), booking.getEnd());
        assertEquals(bookingDtoListForStatusWaiting.get(0).getStatus(), booking.getStatus());

        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByItemOwnerOrderByStartDesc(any());
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByItemOwnerAndStatusOrderByStartDesc(any(), any());
        Mockito.verifyNoMoreInteractions(bookingRepository);
    }
}