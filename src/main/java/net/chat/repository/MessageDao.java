package net.chat.repository;

import net.chat.entity.ConversationEntity;
import net.chat.entity.MessageEntity;
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
    public List<MessageEntity> getLast20(ConversationEntity conversation) {
        return getEm().createNamedQuery("Message.last20").setParameter("conversation", conversation).setMaxResults(20).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<MessageEntity> findAll() {
        return getEm().createNamedQuery("Message.findAll").getResultList();
    }

}
