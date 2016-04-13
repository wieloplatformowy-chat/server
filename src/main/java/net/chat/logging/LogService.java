package net.chat.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mariusz Gorzycki
 * @since 30.03.2016
 */
@Service
@Transactional
public class LogService {
    @Autowired
    LogDao logDao;

    public void debug(String message){
        LogEntity log = prepareLog(message).setPriority(LogPriority.DEBUG);
        dispatchLog(log);
    }

    public void info(String message){
        LogEntity log = prepareLog(message).setPriority(LogPriority.INFO);
        dispatchLog(log);
    }

    public void warn(String message){
        LogEntity log = prepareLog(message).setPriority(LogPriority.WARN);
        dispatchLog(log);
    }

    public void error(String message){
        LogEntity log = prepareLog(message).setPriority(LogPriority.ERROR);
        dispatchLog(log);
    }

    private LogEntity prepareLog(String message){
        return LogEntity.withCurrentDate().setUserId(findUserId()).setMessage(message);
    }

    private void dispatchLog(LogEntity logEntity){
        System.out.println(logEntity);
        logDao.persist(logEntity);
    }

    private Long findUserId(){
        return null;
    }
}
