package net.chat.rest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.chat.config.authentication.TokenService;
import net.chat.entity.UserEntity;
import net.chat.logging.LogDao;
import net.chat.logging.LogEntity;
import net.chat.logging.LogService;
import net.chat.repository.UserDao;
import net.chat.rest.dto.*;
import net.chat.rest.message.DataResponse;
import net.chat.rest.message.ResponseError;
import net.chat.rest.message.RestResponse;
import net.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserRestController extends RestExceptionHandler {
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
    public DataResponse<List<UserEntity>> listAllMembers() {
        return DataResponse.success(userDao.findAll());
    }

    @ApiIgnore
    @RequestMapping(path = "/logs", method = RequestMethod.GET, produces = "application/json")
    public List<LogEntity> listAllLogs() {
        return logDao.findAll();
    }

    @ApiIgnore
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public UserEntity findMemberById(@PathVariable("id") Long id) {
        logger.debug("find member: " + id);
        return userDao.findById(id);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Login user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class)})
    public TokenResponse login(@RequestBody LoginParams userDto) {
        UserEntity user = userDto.toUserWithNullId();
        logger.debug("logging user: " + userDto);
        String token = userService.login(user);
        return TokenResponse.with(token);
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Register user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class)})
    public RestResponse register(@RequestBody RegisterParams registerParams) {
        UserEntity user = registerParams.toUserWithNullId();
        logger.debug("registering user: " + user);
        userService.register(user);
        return RestResponse.with("User registered succesfully.");
    }

    @ApiIgnore
    @RequestMapping(path = "/delete", method = RequestMethod.DELETE, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Delete user", notes = "throws: UNKNOWN_ERROR, INVALID_JSON")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public RestResponse delete(@RequestBody PasswordParam restData) {
        logger.debug("deleting user");
        userService.delete(restData.getPassword());
        return RestResponse.with("User deleted succesfully.");
    }

    @RequestMapping(path = "/whoami", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Returns user associated with given token")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public UserResponse whoAmI() {
        logger.debug("whoami");

        UserEntity loggedUser = userService.getLoggedUser();

        return UserResponse.fromEntity(loggedUser);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Logout user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public RestResponse logout() {
        logger.debug("logout");

        userService.logout();

        return RestResponse.with("Logged out succesfully.");
    }

    @RequestMapping(path = "/search", method = RequestMethod.POST, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Search for users with given name and email")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public List<UserResponse> search(@RequestBody SearchUserParams search) {
        logger.debug("search for: " + search);

        List<UserResponse> result = userService.search(search.name, search.email);

        return result;
    }
}
