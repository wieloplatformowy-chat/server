package net.chat.repository;

import net.chat.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class UserDao {
    @Autowired
    private EntityManager em;

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public User findByName(String name) {
        return (User) em.createNamedQuery("User.findByName").setParameter("name", name).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return em.createNamedQuery("User.findAll").getResultList();
    }

    public void register(User user) {
        em.persist(user);
    }
}
