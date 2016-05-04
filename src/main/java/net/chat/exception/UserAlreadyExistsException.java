package net.chat.exception;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
public class UserAlreadyExistsException extends IllegalArgumentException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
