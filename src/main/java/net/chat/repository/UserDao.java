package net.chat.repository;

import net.chat.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class UserDao extends BaseDao<User>{

    public User findById(Long id) {
        return getEm().find(User.class, id);
    }

    public User findByName(String name) {
        return (User) getEm().createNamedQuery("User.findByName").setParameter("name", name).getSingleResult();
    }

    public boolean isUserNameTaken(String name) {
        return getEm().createNamedQuery("User.findByName").setParameter("name", name).getResultList().size() > 0;
    }

    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return getEm().createNamedQuery("User.findAll").getResultList();
    }
}
