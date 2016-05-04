package net.chat.exception;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
public class InvalidTokenException extends BadCredentialsException {
    public InvalidTokenException(String s) {
        super(s);
    }
}
