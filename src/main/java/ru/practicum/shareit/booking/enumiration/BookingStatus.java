package ru.practicum.shareit.booking.enumiration;

public enum BookingStatus {
    // новое бронирование, ожидает одобрения
    WAITING,
    // бронирование подтверждено владельцем
    APPROVED,
    // бронирование отклонено владельцем
    REJECTED,
    // бронирование отменено создателем
    CANCELED;

    public static BookingStatus fromString(String value) {
        return switch (value.toLowerCase()) {
            case "WAITING" -> WAITING;
            case "APPROVED" -> APPROVED;
            case "REJECTED" -> REJECTED;
            case "CANCELED" -> CANCELED;
            default -> null;
        };
    }
}
