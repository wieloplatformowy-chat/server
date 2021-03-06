package net.chat.config.authentication;

import net.chat.entity.UserEntity;
import net.chat.exception.InvalidPasswordException;
import net.chat.exception.UserNotExistsException;
import net.chat.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

/**
 * @author Mariusz Gorzycki
 * @since 12.04.2016
 */
@Service
public class PasswordAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private TokenService tokenService;

    @Autowired
    UserDao userDao;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        throwIfUserNameNotExists(username);
        UserEntity foundUser = userDao.findByName(username);

        if (!foundUser.getPassword().equals(password))
            throw new InvalidPasswordException("Password: " + password + " is not valid for user: " + username);

        AuthenticationWithToken resultOfAuthentication = new AuthenticationWithToken(username, null, AuthorityUtils.createAuthorityList("USER"));
        String newToken = tokenService.generateNewToken();
        resultOfAuthentication.setToken(newToken);
        tokenService.store(newToken, resultOfAuthentication);

        return resultOfAuthentication;
    }

    private void throwIfUserNameNotExists(String username) {
        if (!userDao.isUserNameTaken(username))
            throw new UserNotExistsException("UserEntity: " + username + " not exists");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
