package net.chat.exception;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
public class NullCredentialsException extends NullPointerException {
    public NullCredentialsException(String message) {
        super(message);
    }
}
