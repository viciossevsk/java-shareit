package ru.practicum.shareit.exception;

public class BookingNotMatchException extends RuntimeException {
    public BookingNotMatchException(String message) {
        super(message);
    }

}
