package net.chat.repository;

import net.chat.entity.ConversationEntity;
import net.chat.entity.MessageEntity;
import net.chat.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class MessageDao extends BaseDao<MessageEntity> {

    public MessageEntity findById(Long id) {
        return getEm().find(MessageEntity.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<MessageEntity> get20LastMessages(ConversationEntity conversation) {
        return getEm().createNamedQuery("Message.last").setParameter("conversation", conversation).setMaxResults(20).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<MessageEntity> get20MessagesBefore(ConversationEntity conversation, MessageEntity message) {
        return getEm().createNamedQuery("Message.before").setParameter("conversation", conversation).setParameter("date", message.getDate()).setMaxResults(20).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Long> getConversationsWithUnreadMessagesId(UserEntity user) {
        return getEm().createNamedQuery("Message.unreadGroupsId").setParameter("user", user).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<MessageEntity> findAll() {
        return getEm().createNamedQuery("Message.findAll").getResultList();
    }

}
