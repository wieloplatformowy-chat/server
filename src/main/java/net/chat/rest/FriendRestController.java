package net.chat.rest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.chat.config.authentication.TokenService;
import net.chat.logging.LogService;
import net.chat.rest.dto.OnlineResponse;
import net.chat.rest.dto.UserResponse;
import net.chat.rest.message.ResponseError;
import net.chat.rest.message.RestResponse;
import net.chat.service.FriendService;
import net.chat.service.OnlineService;
import net.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
@CrossOrigin
@RestController
@RequestMapping("/friends")
public class FriendRestController extends RestExceptionHandler {
    @Autowired
    private LogService logger;

    @Autowired
    UserService userService;

    @Autowired
    FriendService friendService;

    @Autowired
    TokenService tokenService;

    @Autowired
    OnlineService onlineService;

    @RequestMapping(path = "/add/{id}", method = RequestMethod.POST, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Adds friend of logged user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public RestResponse register(@PathVariable Long id) {
        logger.debug("add friend: " + id);
        friendService.addFriend(id);
        return RestResponse.with("Friend added successfully.");
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Removes friend of logged user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public RestResponse delete(@PathVariable Long id) {
        logger.debug("delete friend: " + id);
        friendService.deleteFriend(id);
        return RestResponse.with("Friend added successfully.");
    }

    @RequestMapping(path = "/my", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Lists all friends of logged user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public List<UserResponse> my() {
        logger.debug("my friends");
        List<UserResponse> result = friendService.myFriends();
        return result;
    }

    @RequestMapping(path = "/online/{id}", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Return whether requested user is online.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public OnlineResponse online(@PathVariable Long id) {
        logger.debug("check online: : " + id);
        userService.throwIfNotLoggedIn();

        onlineService.markMeOnline();
        boolean online = onlineService.isOnline(id);

        return new OnlineResponse().setOnline(online);
    }
}
