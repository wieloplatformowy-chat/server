package net.chat.repository;

import net.chat.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Transactional
@Repository
public class UserDao extends BaseDao<UserEntity> {

    public UserEntity findById(Long id) {
        return getEm().find(UserEntity.class, id);
    }

    public UserEntity findByName(String name) {
        return (UserEntity) getEm().createNamedQuery("User.findByName").setParameter("name", name).getSingleResult();
    }

    public List<UserEntity> findByNameLike(String nameFragment) {
        return getEm().createNamedQuery("User.findByNameLike").setParameter("name", "%" + nameFragment + "%").getResultList();
    }

    public List<UserEntity> findByEmailLike(String emailFragment) {
        return getEm().createNamedQuery("User.findByEmailLike").setParameter("email", "%" + emailFragment + "%").getResultList();
    }

    public UserEntity findByNameOrNull(String name) {
        try {
            return findByName(name);
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean isUserNameTaken(String name) {
        return getEm().createNamedQuery("User.findByName").setParameter("name", name).getResultList().size() > 0;
    }

    @SuppressWarnings("unchecked")
    public List<UserEntity> findAll() {
        return getEm().createNamedQuery("User.findAll").getResultList();
    }
}
