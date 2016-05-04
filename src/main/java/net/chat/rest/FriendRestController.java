package net.chat.rest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.chat.config.authentication.TokenService;
import net.chat.logging.LogService;
import net.chat.rest.dto.IdDto;
import net.chat.rest.dto.UserWithoutPasswordDto;
import net.chat.rest.message.ResponseError;
import net.chat.rest.message.RestResponse;
import net.chat.service.FriendService;
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

    @RequestMapping(path = "/add", method = RequestMethod.POST, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Adds friend for logged user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public RestResponse register(@RequestBody IdDto idDto) {
        logger.debug("addFriend friend: " + idDto);
        friendService.addFriend(idDto.id);
        return RestResponse.with("Friend added succesfully.");
    }

    @RequestMapping(path = "/my", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Lists all friends of logged user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public List<UserWithoutPasswordDto> my() {
        logger.debug("myFriends friends");
        List<UserWithoutPasswordDto> result = friendService.myFriends();
        return result;
    }
}
