package net.chat.service;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import net.chat.entity.UserEntity;
import net.chat.exception.AlreadyAFriendException;
import net.chat.exception.NotAFriendException;
import net.chat.exception.NullCredentialsException;
import net.chat.repository.FriendDao;
import net.chat.rest.dto.UserWithoutPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 04.05.2016
 */
@Service
public class FriendService {
    @Autowired
    UserService userService;

    @Autowired
    FriendDao friendDao;

    @Transactional
    public void register(UserEntity user) {
//        throwIfCredentialsNotExists(user);

//        if (checkIfUserWithNameExists(user))
//            throw new UserAlreadyExistsException("UserEntity: " + user.getName() + " already exists");
//
//        userDao.persist(user);
    }

    @Transactional
    public void addFriend(Long friendId) {
        UserEntity loggedUser = userService.getLoggedUser();
        UserEntity friend = userService.findById(friendId);

        if (isFriend(loggedUser, friend))
            throw new AlreadyAFriendException("User with id: " + friendId + " has been added as friend before.");

        friendDao.addFriend(loggedUser, friend);
    }

    @Transactional
    public void deleteFriend(Long friendId) {
        UserEntity loggedUser = userService.getLoggedUser();
        UserEntity friend = userService.findById(friendId);

        if (!isFriend(loggedUser, friend))
            throw new NotAFriendException("User with id: " + friendId + " is not your friend.");

        friendDao.deleteFriend(loggedUser, friend);
    }

    public List<UserWithoutPasswordDto> myFriends() {
        UserEntity loggedUser = userService.getLoggedUser();

        List<UserEntity> friends = friendDao.findFriends(loggedUser);

        return FluentIterable.from(friends).transform(new Function<UserEntity, UserWithoutPasswordDto>() {
            @Override
            public UserWithoutPasswordDto apply(UserEntity input) {
                return UserWithoutPasswordDto.fromEntity(input);
            }
        }).toSortedList(new Comparator<UserWithoutPasswordDto>() {
            @Override
            public int compare(UserWithoutPasswordDto o1, UserWithoutPasswordDto o2) {
                return o1.name.compareTo(o2.name);
            }
        });
    }

    private boolean isFriend(UserEntity user, UserEntity friend) {
        return friendDao.isFriend(user, friend);
    }

    public boolean isMyFriend(UserEntity user) {
        UserEntity loggedUser = userService.getLoggedUser();
        return friendDao.isFriend(loggedUser, user);
    }

    private void throwIfCredentialsNotExists(String name, String password) {
        if (name == null || password == null)
            throw new NullCredentialsException("Credentials are null");
    }

    private void throwIfCredentialsNotExists(Object credential) {
        if (credential == null)
            throw new NullCredentialsException("Credentials are null");
    }
}
