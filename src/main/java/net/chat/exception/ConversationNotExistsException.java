package net.chat.exception;

/**
 * @author Mariusz Gorzycki
 * @since 24.05.2016
 */
public class ConversationNotExistsException extends IllegalArgumentException {
    public ConversationNotExistsException(String message) {
        super(message);
    }
}
