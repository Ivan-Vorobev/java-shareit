package ru.practicum.shareit.booking.enumiration;

public enum BookingStatus {
    // новое бронирование, ожидает одобрения
    WAITING,
    // бронирование подтверждено владельцем
    APPROVED,
    // бронирование отклонено владельцем
    REJECTED,
    // бронирование отменено создателем
    CANCELED
}
