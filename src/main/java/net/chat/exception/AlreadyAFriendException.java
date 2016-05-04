package net.chat.exception;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
public class AlreadyAFriendException extends IllegalArgumentException {
    public AlreadyAFriendException(String message) {
        super(message);
    }
}
