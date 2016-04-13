package net.chat.repository;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

/**
 * @author Mariusz Gorzycki
 * @since 30.03.2016
 */
public class BaseDao<T> {
    @Autowired
    private EntityManager em;

    public void find(Class entityClass, Object primaryKey) {
        em.find(entityClass, primaryKey);
    }

    public void persist(T entity) {
        em.persist(entity);
    }

    public void remove(T entity) {
        em.remove(entity);
    }

    public void merge(T entity) {
        em.merge(entity);
    }

    public EntityManager getEm() {
        return em;
    }
}
