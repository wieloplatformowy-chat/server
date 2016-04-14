package net.chat.rest;

import net.chat.config.authentication.TokenService;
import net.chat.entity.User;
import net.chat.logging.LogDao;
import net.chat.logging.LogEntity;
import net.chat.logging.LogService;
import net.chat.repository.UserDao;
import net.chat.rest.dto.UserDto;
import net.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/rest/users")
public class UserRestController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private LogDao logDao;

    @Autowired
    private LogService logger;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @ApiIgnore
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public DataResponse<List<User>> listAllMembers() {
        return DataResponse.success(userDao.findAll());
    }

    @ApiIgnore
    @RequestMapping(path = "/logs", method = RequestMethod.GET, produces = "application/json")
    public List<LogEntity> listAllLogs() {
        return logDao.findAll();
    }

    @ApiIgnore
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public User findMemberById(@PathVariable("id") Long id) {
        logger.debug("find member: " + id);
        return userDao.findById(id);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, produces = "application/json")
    public DataResponse<String> login(@RequestBody UserDto userDto) {
        User user = userDto.toUserWithNullId();
        logger.debug("login user: "+userDto);
        String token = userService.login(user);
        return DataResponse.success(token);
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST, produces = "application/json")
    public BaseResponse register(@RequestBody UserDto userDto) {
        User user = userDto.toUserWithNullId();
        logger.debug("registered user: " + user);
        userService.register(user);
        return BaseResponse.success();
    }

    @ExceptionHandler(UserService.UserAlreadyExistsException.class)
    public BaseResponse handleUAEException() throws Throwable {
        return BaseResponse.error(Errors.USERNAME_IS_TAKEN);
    }

    @ExceptionHandler(UserService.UserNotExistsException.class)
    public BaseResponse handleUNEException() throws Throwable {
        return BaseResponse.error(Errors.USER_NOT_EXISTS);
    }

    @ExceptionHandler(UserService.NullCredentialsException.class)
    public BaseResponse handleNCException() throws Throwable {
        return BaseResponse.error(Errors.CREDENTIALS_NOT_PROVIDED);
    }

    @ExceptionHandler(UserService.InvalidPasswordException.class)
    public BaseResponse handleIPException() throws Throwable {
        return BaseResponse.error(Errors.INVALID_PASSWORD);
    }

    @ExceptionHandler(Throwable.class)
    public DataResponse<Throwable> handleException(Throwable throwable) throws Throwable {
        return DataResponse.error(Errors.UNKNOWN_ERROR, throwable);
//        throw throwable;
//        return throwable;
    }
}
