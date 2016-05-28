package net.chat.service;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import net.chat.entity.ConversationEntity;
import net.chat.entity.MessageEntity;
import net.chat.entity.UserConversationEntity;
import net.chat.entity.UserEntity;
import net.chat.exception.MessageNotExistsException;
import net.chat.exception.NullCredentialsException;
import net.chat.repository.MessageDao;
import net.chat.repository.UserConversationDao;
import net.chat.rest.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
                .setMessage(message);
        messageDao.persist(messageEntity);
    }

    @Transactional
    public List<MessageResponse> last20(Long conversationId) {
        UserEntity loggedUser = userService.getLoggedUser();

        ConversationEntity conversation = conversationService.getConversationContainingUserOrThrow(conversationId);

        List<MessageEntity> entities = messageDao.get20LastMessages(conversation);
        if (entities.size() > 0) {
            UserConversationEntity userConversation = userConversationDao.findByUserAndConversation(loggedUser, conversation);
            userConversation.setLastSeenPost(entities.get(0));
            userConversation.setLastSeenPostDate(new Timestamp(System.currentTimeMillis()));
            userConversationDao.merge(userConversation);
        }

        return FluentIterable.from(entities).transform(new Function<MessageEntity, MessageResponse>() {
            @Override
            public MessageResponse apply(MessageEntity input) {
                return MessageResponse.fromEntity(input);
            }
        }).toList().reverse();
    }

    @Transactional
    public List<MessageResponse> before20(Long conversationId, Long messageId) {
        userService.throwIfNotLoggedIn();

        ConversationEntity conversation = conversationService.getConversationContainingUserOrThrow(conversationId);
        MessageEntity LastMessage = messageDao.findById(messageId);
        if (LastMessage == null)
            throw new MessageNotExistsException("Message with ID: " + messageId + " does not exists within conversation with ID: " + conversationId);

        List<MessageEntity> entities = messageDao.get20MessagesBefore(conversation, LastMessage);

        return FluentIterable.from(entities).transform(new Function<MessageEntity, MessageResponse>() {
            @Override
            public MessageResponse apply(MessageEntity input) {
                return MessageResponse.fromEntity(input);
            }
        }).toList().reverse();
    }

    public List<Long> unread() {
        UserEntity loggedUser = userService.getLoggedUser();

        List<Long> conversationsWithUnreadMessagesId = messageDao.getConversationsWithUnreadMessagesId(loggedUser);

        return conversationsWithUnreadMessagesId;
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
