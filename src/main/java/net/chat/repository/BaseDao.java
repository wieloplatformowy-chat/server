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

    /**
     * {@link EntityManager#find}
     */
    public void find(Class entityClass, Object primaryKey) {
        em.find(entityClass, primaryKey);
    }

    /**
     * {@link EntityManager#persist}
     */
    public void persist(T entity) {
        em.persist(entity);
    }

    /**
     * {@link EntityManager#remove}
     */
    public void remove(T entity) {
        em.remove(entity);
    }

    /**
     * {@link EntityManager#merge}
     */
    public void merge(T entity) {
        em.merge(entity);
    }

    public EntityManager getEm() {
        return em;
    }
}
