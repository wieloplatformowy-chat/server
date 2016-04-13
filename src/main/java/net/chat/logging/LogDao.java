package net.chat.logging;

import net.chat.repository.BaseDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 30.03.2016
 */
@Repository
@Transactional
public class LogDao extends BaseDao<LogEntity> {
    @SuppressWarnings("unchecked")
    public List<LogEntity> findAll() {
        return getEm().createNamedQuery("LogEntity.findAll").getResultList();
    }
}
