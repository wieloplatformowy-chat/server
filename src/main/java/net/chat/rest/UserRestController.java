package net.chat.rest;

import net.chat.entity.User;
import net.chat.logging.LogDao;
import net.chat.logging.LogEntity;
import net.chat.logging.LogService;
import net.chat.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/rest/users")
public class UserRestController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private LogDao logDao;

    @Autowired
    private LogService logger;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public DataResponse<List<User>> listAllMembers() {
        return DataResponse.success(userDao.findAll());
    }

    @RequestMapping(path = "/logs", method = RequestMethod.GET, produces = "application/json")
    public List<LogEntity> listAllLogs() {
        return logDao.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public User findMemberById(@PathVariable("id") Long id) {
        logger.debug("find member: "+id);
        return userDao.findById(id);
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET, produces = "application/json")
    public BaseResponse login() {
        logger.debug("logged in");
        return BaseResponse.error(ResponseError.from(Errors.INVALID_DUPA));
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST, produces = "application/json")
    public DataResponse<User> register(@RequestBody User user) {
        logger.debug("registered user: " + user);
        return DataResponse.success(user);
    }

    @ExceptionHandler(Throwable.class)
    public Throwable handleException(Throwable throwable) throws Throwable{
        throw throwable;
//        return throwable;
    }
}
