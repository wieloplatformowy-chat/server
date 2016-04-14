package net.chat.service;

import com.google.common.base.Optional;
import net.chat.config.authentication.AuthenticationWithToken;
import net.chat.entity.User;
import net.chat.repository.UserDao;
import net.chat.rest.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mariusz Gorzycki
 * @since 03.04.2016
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Transactional
    public void register(User user) {
        throwIfCredentialsNotExists(user);

        if (checkIfUserWithNameExists(user))
            throw new UserAlreadyExistsException("User: " + user.getName() + " already exists");

        userDao.persist(user);
    }

    public String login(UserDto user) {
        return login(user.toUserWithNullId());
    }

    @Transactional
    public String login(User user) {
        throwIfCredentialsNotExists(user);

        Authentication authentication = tryToAuthenticateWithUsernameAndPassword(user.getName(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthenticationWithToken token = (AuthenticationWithToken) authentication;

        return token.getToken();
    }

    public User getLoggedUser() {
        String loggedUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userDao.isUserNameTaken(loggedUser))
            throw new UserNotExistsException("User: " + loggedUser + " not exists");

        return userDao.findByName(loggedUser);
    }

    private Authentication tryToAuthenticateWithUsernameAndPassword(String username, String password) {
        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        return tryToAuthenticate(requestAuthentication);
    }

    private void processTokenAuthentication(Optional<String> token) {
        Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    }

    private Authentication tryToAuthenticateWithToken(Optional<String> token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        return tryToAuthenticate(requestAuthentication);
    }

    private Authentication tryToAuthenticate(Authentication requestAuthentication) {

        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
//        logger.debug("User successfully authenticated");
        return responseAuthentication;
    }

    private boolean checkIfUserWithNameExists(User user) {
        return userDao.isUserNameTaken(user.getName());
    }

    private void throwIfCredentialsNotExists(User user) {
        if (user.getName() == null || user.getPassword() == null)
            throw new NullCredentialsException("Credentials are null");
    }

    public static class NullCredentialsException extends NullPointerException {
        public NullCredentialsException(String s) {
            super(s);
        }
    }

    public static class UserAlreadyExistsException extends IllegalArgumentException {
        public UserAlreadyExistsException(String s) {
            super(s);
        }
    }

    public static class UserNotExistsException extends IllegalArgumentException {
        public UserNotExistsException(String s) {
            super(s);
        }
    }

    public static class InvalidPasswordException extends IllegalArgumentException {
        public InvalidPasswordException(String s) {
            super(s);
        }
    }
}
