package net.chat.service;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import net.chat.entity.ConversationEntity;
import net.chat.entity.MessageEntity;
import net.chat.entity.UserConversationEntity;
import net.chat.entity.UserEntity;
import net.chat.exception.NullCredentialsException;
import net.chat.repository.ConversationDao;
import net.chat.repository.MessageDao;
import net.chat.repository.UserConversationDao;
import net.chat.repository.UserDao;
import net.chat.rest.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 26.05.2016
 */
@Service
public class MessageService {
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    ConversationDao conversationDao;

    @Autowired
    ConversationService conversationService;

    @Autowired
    MessageDao messageDao;

    @Autowired
    UserConversationDao userConversationDao;

    @Transactional
    public void send(Long conversationId, String message) {
        UserEntity loggedUser = userService.getLoggedUser();
        throwIfCredentialsNotExists(conversationId, message);

        ConversationEntity conversation = conversationService.getConversationContainingUserOrThrow(conversationId);

        MessageEntity messageEntity = MessageEntity.withCurrentDate()
                .setUser(loggedUser)
                .setConversation(conversation)
                .setMessaage(message);
        messageDao.persist(messageEntity);

        UserConversationEntity userConversation = userConversationDao.findByUserAndConversation(loggedUser, conversation);
        userConversation.setLastSeenPostId(messageEntity.getId());
        userConversationDao.getEm().merge(userConversation);
    }

    @Transactional
    public List<MessageResponse> last20(Long conversationId) {
        userService.throwIfNotLoggedIn();

        ConversationEntity conversation = conversationService.getConversationContainingUserOrThrow(conversationId);

        List<MessageEntity> entities = messageDao.getLast20(conversation);
        return FluentIterable.from(entities).transform(new Function<MessageEntity, MessageResponse>() {
            @Override
            public MessageResponse apply(MessageEntity input) {
                return MessageResponse.fromEntity(input);
            }
        }).toList().reverse();
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
