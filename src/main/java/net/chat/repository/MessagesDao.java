package net.chat.repository;

import net.chat.entity.MessageEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class MessagesDao extends BaseDao<MessageEntity> {

    public MessageEntity findById(Long id) {
        return getEm().find(MessageEntity.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<MessageEntity> findAll() {
        return getEm().createNamedQuery("Message.findAll").getResultList();
    }
}
