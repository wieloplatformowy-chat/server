package net.chat.rest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.chat.logging.LogService;
import net.chat.rest.dto.ConversationResponse;
import net.chat.rest.dto.CreateGroupResponse;
import net.chat.rest.dto.InviteToGroupParams;
import net.chat.rest.dto.RenameGroupParams;
import net.chat.rest.message.ResponseError;
import net.chat.rest.message.RestResponse;
import net.chat.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 24.05.2016
 */
@CrossOrigin
@RestController
@RequestMapping("/groups")
public class GroupRestController extends RestExceptionHandler {
    @Autowired
    private LogService logger;

    @Autowired
    ConversationService conversationService;

    @RequestMapping(path = "/create", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Creates a new group with the logged user assigned")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public CreateGroupResponse create() {
        logger.debug("create group");
        Long id = conversationService.createGroup();
        return CreateGroupResponse.with(id);
    }

    @RequestMapping(path = "/invite", method = RequestMethod.POST, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Invites a user to the group.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public RestResponse invite(@RequestBody InviteToGroupParams params) {
        logger.debug("invite to group: " + params);
        conversationService.inviteToGroup(params.getGroupId(), params.getUserIds());
        return RestResponse.with("Users invited successfully.");
    }

    @RequestMapping(path = "/rename", method = RequestMethod.POST, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Changes name of given group")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public RestResponse rename(@RequestBody RenameGroupParams params) {
        logger.debug("rename group: " + params);
        conversationService.renameGroup(params.getGroupId(), params.getNewName());
        return RestResponse.with("Group renamed successfully.");
    }

    @RequestMapping(path = "/my", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Lists all groups of logged user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public List<ConversationResponse> my() {
        logger.debug("my groups");
        List<ConversationResponse> result = conversationService.myGroups();
        return result;
    }
}
