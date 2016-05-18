package net.chat.service;

import net.chat.entity.ConversationEntity;
import net.chat.entity.UserEntity;
import net.chat.repository.ConversationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mariusz Gorzycki
 * @since 04.05.2016
 */
@Service
public class ConversationService {
    @Autowired
    UserService userService;

    @Autowired
    ConversationDao conversationDao;

    public ConversationEntity getOrCreateConversationWithUser(Long userId) {
        UserEntity loggedUser = userService.getLoggedUser();

        ConversationEntity conversationEntity = new ConversationEntity().setUsers(loggedUser);
        if (userId != loggedUser.getId()) {
            UserEntity interlocutor = userService.findById(userId);
            conversationEntity.getUsers().add(interlocutor);
        }
        conversationDao.persist(conversationEntity);

        return conversationEntity;
    }
}
