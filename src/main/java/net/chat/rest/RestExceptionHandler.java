package net.chat.rest;

import net.chat.exception.*;
import net.chat.logging.LogService;
import net.chat.rest.message.Errors;
import net.chat.rest.message.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
@ControllerAdvice
public class RestExceptionHandler {
    @Autowired
    private LogService logger;

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginRequiredException.class)
    public ResponseError handleLRException(LoginRequiredException e) throws Throwable {
        logger.error(e.getMessage());
        return ResponseError.from(Errors.LOGIN_REQUIRED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseError handleUAEException(Exception e) throws Throwable {
        logger.error(e.getMessage());
        return ResponseError.from(Errors.USERNAME_IS_TAKEN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotExistsException.class)
    public ResponseError handleUNEException(Exception e) throws Throwable {
        logger.error(e.getMessage());
        return ResponseError.from(Errors.USER_NOT_EXISTS);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullCredentialsException.class)
    public ResponseError handleNCException(Exception e) throws Throwable {
        logger.error(e.getMessage());
        return ResponseError.from(Errors.CREDENTIALS_NOT_PROVIDED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseError handleIPException(Exception e) throws Throwable {
        logger.error(e.getMessage());
        return ResponseError.from(Errors.INVALID_PASSWORD);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyAFriendException.class)
    public ResponseError handleAAFException(AlreadyAFriendException e) throws Throwable {
        logger.error(e.getMessage());
        return ResponseError.from(Errors.ALREADY_A_FRIEND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotAFriendException.class)
    public ResponseError handleNAFException(NotAFriendException e) throws Throwable {
        logger.error(e.getMessage());
        return ResponseError.from(Errors.NOT_A_FRIEND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseError handleJSONException(HttpMessageNotReadableException e) throws Throwable {
        logger.error(e.getMessage());
        return ResponseError.from(Errors.INVALID_JSON);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(Throwable.class)
//    public ResponseError handleException(Throwable throwable) throws Throwable {
//        logger.error(throwable.getClass().getName() + ": " + throwable.getMessage());
//        return ResponseError.from(Errors.UNKNOWN_ERROR);
//    }
}
