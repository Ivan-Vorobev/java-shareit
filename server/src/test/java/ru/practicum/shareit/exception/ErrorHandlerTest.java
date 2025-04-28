package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ErrorHandlerTest {
    @Autowired
    private ErrorHandler errorHandler;


    @Test
    void handleCompareEmailException() {
        ConflictException e = new ConflictException("ConflictException");
        ErrorHandler.ErrorResponse expectedErrorResponse = new ErrorHandler.ErrorResponse(e.getMessage());
        ErrorHandler.ErrorResponse errorResponse = errorHandler.handleCompareEmailException(e);
        assertEquals(expectedErrorResponse.error, errorResponse.error);
    }

    @Test
    void handleBadRequestException() {
        BadRequestException e = new BadRequestException("BadRequestException");
        ErrorHandler.ErrorResponse expectedErrorResponse = new ErrorHandler.ErrorResponse("Ошибка в запросе: " + e.getMessage());
        ErrorHandler.ErrorResponse errorResponse = errorHandler.handleBadRequestException(e);
        assertEquals(expectedErrorResponse.error, errorResponse.error);
    }

    @Test
    void handleUserNotFoundException() {
        NotFoundException e = new NotFoundException("NotFoundException");
        ErrorHandler.ErrorResponse expectedErrorResponse = new ErrorHandler.ErrorResponse(e.getMessage());
        ErrorHandler.ErrorResponse errorResponse = errorHandler.handleUserNotFoundException(e);
        assertEquals(expectedErrorResponse.error, errorResponse.error);
    }
}