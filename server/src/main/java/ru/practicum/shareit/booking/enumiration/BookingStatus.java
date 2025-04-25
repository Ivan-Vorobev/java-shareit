package ru.practicum.shareit.booking.enumiration;

public enum BookingStatus {
    // синтетический статус для фильтрации
    ALL,
    // новое бронирование, ожидает одобрения
    WAITING,
    // бронирование подтверждено владельцем
    APPROVED,
    // бронирование отклонено владельцем
    REJECTED,
    // бронирование отменено создателем
    CANCELED,
    // Завершенные
    PAST
}
