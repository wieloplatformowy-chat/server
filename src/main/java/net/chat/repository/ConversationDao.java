package net.chat.repository;

import com.google.common.base.Optional;
import net.chat.entity.ConversationEntity;
import net.chat.entity.UserEntity;
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
            return Optional.fromNullable(getEm().find(ConversationEntity.class, id));
        } catch (NoResultException e) {
            return Optional.absent();
        }
    }

    public Optional<ConversationEntity> getConversationWithMyself(UserEntity me) {
        try {
            return Optional.fromNullable((ConversationEntity) getEm().createNamedQuery("Conversations.withMe").setParameter("me", me).getSingleResult());
        } catch (NoResultException e) {
            return Optional.absent();
        }
    }

    public Optional<ConversationEntity> getConversationWithUser(UserEntity me, UserEntity user) {
        try {
            return Optional.fromNullable((ConversationEntity) getEm().createNamedQuery("Conversations.with").setParameter("me", me).setParameter("user", user).getSingleResult());
        } catch (NoResultException e) {
            return Optional.absent();
        }
    }

    @SuppressWarnings("unchecked")
    public List<ConversationEntity> getMyGroups(UserEntity me) {
        return getEm().createNamedQuery("Conversations.myGroups").setParameter("me", me).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<ConversationEntity> findAll() {
        return getEm().createNamedQuery("Conversations.findAll").getResultList();
    }
}
