package ru.practicum.shareit.otherFunction;

public class AddvansedFunctions {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static String stringToBlueColor(String text) {
        return ANSI_BLUE + text + ANSI_RESET;
    }

    public static String stringToGreenColor(String text) {
        return ANSI_GREEN + text + ANSI_RESET;
    }

    public static String stringToRedColor(String text) {
        return ANSI_RED + text + ANSI_RESET;
    }

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    public static final String A_ERROR = "error";
    public static final String LOG_ERROR = "Error message: {}";
    public static final String SERVER_ERROR = "Server error:";
    public static final String UNKNOWN_STATE = "Unknown state: %s";
    public static final String MISTAKEN_ITEM_ID = "Mistaken Item id: %s";
    public static final String MISTAKEN_USER_ID = "Mistaken user id: %s";
    public static final String MISTAKEN_OWNER_ID = "Mistaken owner id: %s";
    public static final String MISTAKEN_BOOKING_ID = "mistaken booking id: %s";
    public static final String DUPLICATED_EMAIL = "Duplicated email found: %s";
    public static final String FAILED_TO_FOUND_ENTITY = "Failed to find an entity: %s in database";
    public static final String ITEM_OWNER_ID_DIFFERENT_OWNER_ID = "The item id %s has a different owner";

}
