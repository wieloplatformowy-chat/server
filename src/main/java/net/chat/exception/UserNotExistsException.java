package net.chat.exception;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
public class UserNotExistsException extends IllegalArgumentException {
    public UserNotExistsException(String message) {
        super(message);
    }
}
