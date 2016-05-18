package net.chat.rest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.chat.config.authentication.TokenService;
import net.chat.entity.ConversationEntity;
import net.chat.logging.LogService;
import net.chat.rest.dto.ConversationResponse;
import net.chat.rest.dto.OnlineResponse;
import net.chat.rest.dto.UserResponse;
import net.chat.rest.message.ResponseError;
import net.chat.rest.message.RestResponse;
import net.chat.service.ConversationService;
import net.chat.service.FriendService;
import net.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
@CrossOrigin
@RestController
@RequestMapping("/conversations")
public class ConversationRestController extends RestExceptionHandler {
    @Autowired
    private LogService logger;

    @Autowired
    UserService userService;

    @Autowired
    FriendService friendService;

    @Autowired
    TokenService tokenService;

    @Autowired
    ConversationService conversationService;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Returns conversation with given user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public ConversationResponse get(@PathVariable Long id) {
        logger.debug("get conversation: " + id);

        ConversationEntity conversation = conversationService.getOrCreateConversationWithUser(id);

        return ConversationResponse.fromEntity(conversation);
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
        return RestResponse.with("Friend added succesfully.");
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

        return new OnlineResponse().setOnline(new Random().nextBoolean());
    }
}
