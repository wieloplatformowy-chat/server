package net.chat.exception;

/**
 * @author Mariusz Gorzycki
 * @since 24.05.2016
 */
public class GroupNotExistsException extends IllegalArgumentException {
    public GroupNotExistsException(String message) {
        super(message);
    }
}
