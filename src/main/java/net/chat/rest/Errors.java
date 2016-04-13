package net.chat.rest;

/**
 * @author Mariusz Gorzycki
 * @since 29.03.2016
 */
public enum Errors {
    INVALID_JSON(1, "dupa"),
    INVALID_DUPA(2, "dupa");

    Errors(int id, String message) {
        this.id = id;
        this.message = message;
    }

    private final int id;
    private final String message;

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
