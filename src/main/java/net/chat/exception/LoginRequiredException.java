package net.chat.exception;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
public class LoginRequiredException extends RuntimeException {
    public LoginRequiredException(String message) {
        super(message);
    }
}
