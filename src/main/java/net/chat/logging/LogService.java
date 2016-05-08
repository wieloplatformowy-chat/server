package net.chat.logging;

import net.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mariusz Gorzycki
 * @since 30.03.2016
 */
@Service
@Transactional
public class LogService {
    @Autowired
    LogDao logDao;

    @Autowired(required = true)
    private HttpServletRequest request;

    @Autowired
    UserService userService;

    public void debug(String message) {
        LogEntity log = prepareLog(message).setPriority(LogPriority.DEBUG);
        dispatchLog(log);
    }

    public void info(String message) {
        LogEntity log = prepareLog(message).setPriority(LogPriority.INFO);
        dispatchLog(log);
    }

    public void warn(String message) {
        LogEntity log = prepareLog(message).setPriority(LogPriority.WARN);
        dispatchLog(log);
    }

    public void error(String message) {
        LogEntity log = prepareLog(message).setPriority(LogPriority.ERROR);
        dispatchLog(log);
    }

    private LogEntity prepareLog(String message) {
        return LogEntity.withCurrentDate().setUserName(findUserName()).setUserIp(findUserIp()).setMessage(message);
    }

    private String findUserIp() {
        String remoteAddress = null;

        try {
            remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getLocalName() + " : ";
            remoteAddress = request.getRemoteAddr() + " :R: ";
            remoteAddress = request.getLocalAddr() + " :L: ";
            remoteAddress = request.getRemoteHost() + " :H: ";
            remoteAddress += ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteUser() + " : ";
            remoteAddress += ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getContextPath() + " : ";
            remoteAddress += ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteHost() + " : ";
            remoteAddress += ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr() + " : ";
            remoteAddress += ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemotePort();
        } catch (Exception e) {

        }

        return remoteAddress;
    }

    private void dispatchLog(LogEntity logEntity) {
        System.out.println(logEntity);
        logDao.persist(logEntity);
    }

    private String findUserName() {
        String userName = null;

        try {
            userName = userService.getLoggedUser().getName();
        } catch (Exception e) {

        }

        return userName;
    }
}
