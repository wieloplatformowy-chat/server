package net.chat.repository;

import net.chat.entity.GroupEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 13.05.2016
 */
@Transactional
@Repository
public class GroupDao extends BaseDao<GroupEntity> {

    public GroupEntity findById(Long id) {
        return getEm().find(GroupEntity.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<GroupEntity> findAll() {
        return getEm().createNamedQuery("Conversation.findAll").getResultList();
    }
}
