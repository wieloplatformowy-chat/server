package net.chat.exception;

/**
 * @author Mariusz Gorzycki
 * @since 28.05.2016
 */
public class MessageNotExistsException extends IllegalArgumentException {
    public MessageNotExistsException(String message) {
        super(message);
    }
}
