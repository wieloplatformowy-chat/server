package net.chat.repository;

import net.chat.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserDao extends BaseDao<User>{

    public User findById(Long id) {
        return getEm().find(User.class, id);
    }

    public User findByName(String name) {
        return (User) getEm().createNamedQuery("User.findByName").setParameter("name", name).getSingleResult();
    }

    @SuppressWarnings("unchecked")
//    @PostAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> findAll() {
        return getEm().createNamedQuery("User.findAll").getResultList();
    }
}
