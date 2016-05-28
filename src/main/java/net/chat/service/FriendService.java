package net.chat.service;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import net.chat.entity.UserEntity;
import net.chat.exception.AlreadyAFriendException;
import net.chat.exception.NotAFriendException;
import net.chat.repository.FriendDao;
import net.chat.rest.dto.UserResponse;
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

    @Transactional
    public List<UserResponse> myFriends() {
        UserEntity loggedUser = userService.getLoggedUser();

        List<UserEntity> friends = friendDao.findFriends(loggedUser);

        return FluentIterable.from(friends).transform(new Function<UserEntity, UserResponse>() {
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

    private boolean isFriend(UserEntity user, UserEntity friend) {
        return friendDao.isFriend(user, friend);
    }

    public boolean isMyFriend(UserEntity user) {
        UserEntity loggedUser = userService.getLoggedUser();
        return friendDao.isFriend(loggedUser, user);
    }
}
