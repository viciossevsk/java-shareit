package ru.practicum.shareit.otherFunction;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.ValidationException;

public class AddvansedFunctions {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static String stringToGreenColor(String text) {
        return ANSI_GREEN + text + ANSI_RESET;
    }

    public static String stringToRedColor(String text) {
        return ANSI_RED + text + ANSI_RESET;
    }

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    public static final String UNKNOWN_STATE = "Unknown state: %s";
    public static final String MISTAKEN_ITEM_ID = "Mistaken Item id: %s";
    public static final String MISTAKEN_ITEM_REQUEST_ID = "Mistaken Item request id: %s";
    public static final String MISTAKEN_USER_ID = "Mistaken user id: %s";
    public static final String MISTAKEN_OWNER_ID = "Mistaken owner id: %s";
    public static final String MISTAKEN_BOOKING_ID = "mistaken booking id: %s";
    public static final String ITEM_OWNER_ID_DIFFERENT_OWNER_ID = "The item id %s has a different owner";

    public static final PageRequest getPage(Integer start, Integer size) {
        if (start < 0 || size <= 0) {
            throw new ValidationException("Mistaken item request parameters");
        }
        return PageRequest.of(start > 0 ? start / size : 0, size);
    }

}
