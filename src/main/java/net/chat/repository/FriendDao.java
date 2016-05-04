package net.chat.repository;

import net.chat.entity.FriendEntity;
import net.chat.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Transactional
@Repository
public class FriendDao extends BaseDao<FriendEntity> {

    public FriendEntity findById(Long id) {
        return getEm().find(FriendEntity.class, id);
    }

    public UserEntity findByName(String name) {
        return (UserEntity) getEm().createNamedQuery("User.findByName").setParameter("name", name).getSingleResult();
    }

    public boolean isFriend(UserEntity user, UserEntity friend) {
        return getEm().createNamedQuery("Friend.isFriend").setParameter("user", user).setParameter("friend", friend).getResultList().size() > 0;
    }

    public void addFriend(UserEntity user, UserEntity friend) {
        FriendEntity friendEntity = new FriendEntity().setUser(user).setFriend(friend);
        getEm().persist(friendEntity);
    }

    @SuppressWarnings("unchecked")
    public List<UserEntity> findFriends(UserEntity user) {
        return getEm().createNamedQuery("Friend.findFriends").setParameter("user", user).getResultList();
    }

    public UserEntity findByNameOrNull(String name) {
        try {
            return findByName(name);
        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<UserEntity> findAll() {
        return getEm().createNamedQuery("Friend.findAll").getResultList();
    }
}
