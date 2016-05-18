package net.chat.repository;

import net.chat.entity.UserConversationEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 13.05.2016
 */
@Transactional
@Repository
public class UserConversationDao extends BaseDao<UserConversationEntity> {

    public UserConversationEntity findById(Long id) {
        return getEm().find(UserConversationEntity.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<UserConversationEntity> findAll() {
        return getEm().createNamedQuery("UserConversations.findAll").getResultList();
    }
}
