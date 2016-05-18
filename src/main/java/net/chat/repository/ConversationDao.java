package net.chat.repository;

import com.google.common.base.Optional;
import net.chat.entity.ConversationEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 13.05.2016
 */
@Transactional
@Repository
public class ConversationDao extends BaseDao<ConversationEntity> {

    public Optional<ConversationEntity> findById(Long id) {
        try {
            return Optional.of(getEm().find(ConversationEntity.class, id));
        } catch (NoResultException e) {
            return Optional.absent();
        }
    }

    @SuppressWarnings("unchecked")
    public List<ConversationEntity> findAll() {
        return getEm().createNamedQuery("Conversations.findAll").getResultList();
    }
}
