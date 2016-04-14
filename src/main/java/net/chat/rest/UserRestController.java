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
import org.springframework.http.HttpStatus;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserService.UserAlreadyExistsException.class)
    public BaseResponse handleUAEException(Exception e) throws Throwable {
        logger.error(e.getMessage());
        return BaseResponse.error(Errors.USERNAME_IS_TAKEN);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserService.UserNotExistsException.class)
    public BaseResponse handleUNEException(Exception e) throws Throwable {
        logger.error(e.getMessage());
        return BaseResponse.error(Errors.USER_NOT_EXISTS);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserService.NullCredentialsException.class)
    public BaseResponse handleNCException(Exception e) throws Throwable {
        logger.error(e.getMessage());
        return BaseResponse.error(Errors.CREDENTIALS_NOT_PROVIDED);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserService.InvalidPasswordException.class)
    public BaseResponse handleIPException(Exception e) throws Throwable {
        logger.error(e.getMessage());
        return BaseResponse.error(Errors.INVALID_PASSWORD);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable.class)
    public DataResponse handleException(Throwable throwable) throws Throwable {
        logger.error(throwable.getClass().getName()+": "+throwable.getMessage());
        return DataResponse.error(Errors.UNKNOWN_ERROR, throwable.getMessage());
    }
}
