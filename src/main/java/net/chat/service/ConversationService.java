package net.chat.service;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import net.chat.entity.ConversationEntity;
import net.chat.entity.UserEntity;
import net.chat.exception.GroupNotExistsException;
import net.chat.exception.NullCredentialsException;
import net.chat.repository.ConversationDao;
import net.chat.repository.UserDao;
import net.chat.rest.dto.ConversationResponse;
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
public class ConversationService {
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    ConversationDao conversationDao;

    @Transactional
    public ConversationEntity getOrCreateConversationWithUser(Long userId) {
        UserEntity loggedUser = userService.getLoggedUser();

        Optional<ConversationEntity> conversationOptional;
        if (userId == loggedUser.getId())
            conversationOptional = conversationDao.getConversationWithMyself(loggedUser);
        else
            conversationOptional = conversationDao.getConversationWithUser(loggedUser, userService.findById(userId));

        ConversationEntity conversationEntity;

        if (conversationOptional.isPresent()) {
            conversationEntity = conversationOptional.get();
        } else {
            conversationEntity = new ConversationEntity().setUsers(loggedUser);
            if (userId != loggedUser.getId()) {
                UserEntity interlocutor = userService.findById(userId);
                conversationEntity.getUsers().add(interlocutor);
            }
            conversationDao.persist(conversationEntity);
        }

        return conversationEntity;
    }

    @Transactional
    public Long createGroup() {
        UserEntity loggedUser = userService.getLoggedUser();

        ConversationEntity conversationEntity = new ConversationEntity().setUsers(loggedUser).setGroup(true);
        conversationDao.persist(conversationEntity);

        return conversationEntity.getId();
    }

    @Transactional
    public void inviteToGroup(Long groupId, Long... userIds) {
        userService.throwIfNotLoggedIn();
        throwIfCredentialsNotExists(groupId, userIds);
        ConversationEntity conversation = getConversationContainingUserOrThrow(groupId);

        for (Long id : userIds) {
            UserEntity userEntity = userService.findById(id);
            conversation.getUsers().add(userEntity);
        }
        conversationDao.persist(conversation);
    }

    @Transactional
    public void renameGroup(Long groupId, String newName) {
        userService.throwIfNotLoggedIn();
        throwIfCredentialsNotExists(groupId, newName);
        ConversationEntity conversation = getConversationContainingUserOrThrow(groupId);
        conversation.setName(newName);
        conversationDao.persist(conversation);
    }

    private ConversationEntity getConversationContainingUserOrThrow(Long groupId) {
        UserEntity loggedUser = userService.getLoggedUser();

        Optional<ConversationEntity> conversationOptional = conversationDao.findById(groupId);
        if (!conversationOptional.isPresent())
            throw new GroupNotExistsException("Group with id: " + groupId + " not exists");

        ConversationEntity conversation = conversationOptional.get();
        if (!conversation.getUsers().contains(loggedUser))
            throw new GroupNotExistsException("Group with id: " + groupId + " not exists");

        return conversation;
    }

    @Transactional
    public List<ConversationResponse> myGroups() {
        UserEntity loggedUser = userService.getLoggedUser();

        List<ConversationEntity> conversationEntities = conversationDao.getMyGroups(loggedUser);

        return FluentIterable.from(conversationEntities).transform(new Function<ConversationEntity, ConversationResponse>() {
            @Override
            public ConversationResponse apply(ConversationEntity input) {
                return ConversationResponse.fromEntity(input);
            }
        }).toSortedList(new Comparator<ConversationResponse>() {
            @Override
            public int compare(ConversationResponse o1, ConversationResponse o2) {
                if (o1.getName() != null && o2.getName() != null)
                    return o1.getName().compareTo(o2.getName());
                return 0;
            }
        });
    }

    private void throwIfCredentialsNotExists(Object o1, Object o2) {
        if (o1 == null || o2 == null)
            throw new NullCredentialsException("Required params are null");
    }

    private void throwIfCredentialsNotExists(Long groupId, Long... userIds) {
        if (groupId == null || userIds == null || userIds.length == 0)
            throw new NullCredentialsException("Required params are null");
    }
}
