package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enumiration.BookingStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BookingController.class})
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getAllBooking_shouldReturnListOfBookingsAndResponseHTTP200() throws Exception {
        Long id = 13L;
        Long userId = 10L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        BookingDto bookingDto = BookingDto
                .builder()
                .id(id)
                .itemId(1L)
                .bookerId(userId)
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.APPROVED)
                .build();

        Mockito.when(bookingService.getAllBooking(userId, Optional.empty())).thenReturn(List.of(bookingDto));

        mockMvc
                .perform(
                        get("/bookings")
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].itemId").value(bookingDto.getItemId()))
                .andExpect(jsonPath("$[0].bookerId").value(bookingDto.getBookerId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().name()))
                .andExpect(jsonPath("$[0].start").value(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[0].end").value(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @Test
    void addBooking_shouldAddBookingAndResponseHTTP200() throws Exception {
        Long id = 13L;
        Long userId = 10L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        BookingDto bookingDto = BookingDto
                .builder()
                .itemId(1L)
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.APPROVED)
                .build();
        BookingDto createdBookingDto = BookingDto
                .builder()
                .id(id)
                .itemId(1L)
                .bookerId(userId)
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.APPROVED)
                .build();

        Mockito.when(bookingService.addBooking(bookingDto, userId)).thenReturn(createdBookingDto);

        mockMvc
                .perform(
                        post("/bookings")
                                .content(mapper.writeValueAsString(bookingDto))
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdBookingDto.getId()))
                .andExpect(jsonPath("$.itemId").value(createdBookingDto.getItemId()))
                .andExpect(jsonPath("$.bookerId").value(createdBookingDto.getBookerId()))
                .andExpect(jsonPath("$.status").value(createdBookingDto.getStatus().name()))
                .andExpect(jsonPath("$.start").value(createdBookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.end").value(createdBookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @Test
    void getBooking_shouldGetBookingAndResponseHTTP200() throws Exception {
        Long id = 13L;
        Long bookingId = 15L;
        Long userId = 10L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        BookingDto bookingDto = BookingDto
                .builder()
                .id(id)
                .itemId(1L)
                .bookerId(userId)
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.APPROVED)
                .build();

        Mockito.when(bookingService.getBooking(userId, bookingId)).thenReturn(bookingDto);

        mockMvc
                .perform(
                        get("/bookings/" + bookingId)
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.itemId").value(bookingDto.getItemId()))
                .andExpect(jsonPath("$.bookerId").value(bookingDto.getBookerId()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().name()))
                .andExpect(jsonPath("$.start").value(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.end").value(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @Test
    void getAllOwnerBooking_shouldGetAllOwnerBookingAndResponseHTTP200() throws Exception {
        Long id = 13L;
        Long userId = 10L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        BookingDto bookingDto = BookingDto
                .builder()
                .id(id)
                .itemId(1L)
                .bookerId(userId)
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.APPROVED)
                .build();

        Mockito.when(bookingService.getAllOwnerBooking(userId, Optional.empty())).thenReturn(List.of(bookingDto));

        mockMvc
                .perform(
                        get("/bookings/owner")
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].itemId").value(bookingDto.getItemId()))
                .andExpect(jsonPath("$[0].bookerId").value(bookingDto.getBookerId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().name()))
                .andExpect(jsonPath("$[0].start").value(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[0].end").value(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @Test
    void approveBooking_shouldApproveBookingAndResponseHTTP200() throws Exception {
        Long bookingId = 13L;
        Long userId = 10L;
        boolean isApproved = false;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        BookingDto bookingDto = BookingDto
                .builder()
                .id(bookingId)
                .itemId(1L)
                .bookerId(userId)
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.REJECTED)
                .build();

        Mockito.when(bookingService.approveBooking(userId, bookingId, isApproved)).thenReturn(bookingDto);

        mockMvc
                .perform(
                        patch("/bookings/" + bookingId + "?approved=" + isApproved)
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.itemId").value(bookingDto.getItemId()))
                .andExpect(jsonPath("$.bookerId").value(bookingDto.getBookerId()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().name()))
                .andExpect(jsonPath("$.start").value(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.end").value(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }
}
