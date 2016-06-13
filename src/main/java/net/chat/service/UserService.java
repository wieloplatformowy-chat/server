package net.chat.service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import net.chat.config.authentication.AuthenticationWithToken;
import net.chat.config.authentication.TokenService;
import net.chat.entity.UserEntity;
import net.chat.exception.*;
import net.chat.repository.UserDao;
import net.chat.rest.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author Mariusz Gorzycki
 * @since 03.04.2016
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    TokenService tokenService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Transactional
    public void register(UserEntity user) {
        throwIfCredentialsNotExists(user);

        if (checkIfUserWithNameExists(user))
            throw new UserAlreadyExistsException("UserEntity: " + user.getName() + " already exists");

        userDao.persist(user);
    }

    @Transactional
    public String login(UserEntity user) {
        throwIfCredentialsNotExists(user);

        Authentication authentication = tryToAuthenticateWithUsernameAndPassword(user.getName(), user.getPassword());
        AuthenticationWithToken token = (AuthenticationWithToken) authentication;

        return token.getToken();
    }

    public void logout() {
        throwIfNotLoggedIn();
        tokenService.remove(SecurityContextHolder.getContext().getAuthentication());
        SecurityContextHolder.clearContext();
    }

    public List<UserResponse> search(String nameFragment, String emailFragment) {
        final UserEntity loggedUser = getLoggedUser();

        List<UserEntity> byNameLike = Collections.emptyList();
        List<UserEntity> byEmailLike = Collections.emptyList();

        if (!StringUtils.isEmpty(nameFragment))
            byNameLike = userDao.findByNameLike(nameFragment);
        if (!StringUtils.isEmpty(emailFragment))
            byEmailLike = userDao.findByEmailLike(emailFragment);

        Set<UserEntity> set = new TreeSet<>(new Comparator<UserEntity>() {
            @Override
            public int compare(UserEntity o1, UserEntity o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
        set.addAll(byNameLike);
        set.addAll(byEmailLike);

        return FluentIterable.from(set).filter(new Predicate<UserEntity>() {
            @Override
            public boolean apply(UserEntity input) {
                return !input.getName().equals(loggedUser.getName());
            }
        }).transform(new Function<UserEntity, UserResponse>() {
            @Override
            public UserResponse apply(UserEntity input) {
                return UserResponse.fromEntity(input);
            }
        }).toSortedList(new Comparator<UserResponse>() {
            @Override
            public int compare(UserResponse o1, UserResponse o2) {
                return o1.name.compareTo(o2.name);
            }
        });
    }

    public void throwIfNotLoggedIn() {
        if (getLoggedUser() == null)
            throw new LoginRequiredException("You are not logged in");
    }

    public UserEntity getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new LoginRequiredException("You are not logged in");

        String loggedUser = (String) authentication.getPrincipal();

        if (!userDao.isUserNameTaken(loggedUser))
            throw new UserNotExistsException("UserEntity: " + loggedUser + " not exists");

        return userDao.findByName(loggedUser);
    }

    public boolean isUserNameTaken(String name) {
        return userDao.isUserNameTaken(name);
    }

    private boolean checkIfUserWithNameExists(UserEntity user) {
        return userDao.isUserNameTaken(user.getName());
    }

    @Transactional
    public void delete(String password) {
        throwIfCredentialsNotExists(password);
        UserEntity user = getLoggedUser();

        if (!user.getPassword().equals(password))
            throw new InvalidPasswordException("Password: " + password + " is not valid for user: " + user.getName());

        userDao.remove(user);
    }

    public UserEntity findById(Long id) {
        UserEntity user = userDao.findById(id);
        if (user == null)
            throw new UserNotExistsException("UserEntity with id: " + id + " not exists");
        return user;
    }

    private Authentication tryToAuthenticateWithUsernameAndPassword(String username, String password) {
        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);

        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain UserEntity for provided credentials");
        }
        return responseAuthentication;
    }

    private void throwIfCredentialsNotExists(UserEntity user) {
        if (user.getName() == null || user.getPassword() == null)
            throw new NullCredentialsException("Credentials are null");
    }

    private void throwIfCredentialsNotExists(String name, String password) {
        if (name == null || password == null)
            throw new NullCredentialsException("Credentials are null");
    }

    private void throwIfCredentialsNotExists(String credential) {
        if (credential == null)
            throw new NullCredentialsException("Credentials are null");
    }
}
