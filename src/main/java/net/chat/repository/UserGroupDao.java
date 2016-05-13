package net.chat.repository;

import net.chat.entity.UserGroupEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 13.05.2016
 */
@Transactional
@Repository
public class UserGroupDao extends BaseDao<UserGroupEntity> {

    public UserGroupEntity findById(Long id) {
        return getEm().find(UserGroupEntity.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<UserGroupEntity> findAll() {
        return getEm().createNamedQuery("UserConversations.findAll").getResultList();
    }
}
