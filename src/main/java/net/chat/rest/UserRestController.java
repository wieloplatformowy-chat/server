package net.chat.rest;

import net.chat.entity.User;
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

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<User> listAllMembers() {
        return userDao.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public User findMemberById(@PathVariable("id") Long id) {
        return userDao.findById(id);
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET, produces = "application/json")
    public LoginResponse login() {
        return LoginResponse.credentialsNotProvided();
    }

    @ExceptionHandler(Throwable.class)
    public LoginResponse handleException(Throwable throwable) {
        return LoginResponse.success();
    }
}
