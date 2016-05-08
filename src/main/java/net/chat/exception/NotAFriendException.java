package net.chat.exception;

/**
 * @author Mariusz Gorzycki
 * @since 08.05.2016
 */
public class NotAFriendException extends IllegalArgumentException {
    public NotAFriendException(String message) {
        super(message);
    }
}
