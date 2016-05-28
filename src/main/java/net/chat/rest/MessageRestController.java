package net.chat.rest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.chat.logging.LogService;
import net.chat.rest.dto.MessageResponse;
import net.chat.rest.dto.SendMessageParams;
import net.chat.rest.message.ResponseError;
import net.chat.rest.message.RestResponse;
import net.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 26.05.2016
 */
@CrossOrigin
@RestController
@RequestMapping("/messages")
public class MessageRestController extends RestExceptionHandler {
    @Autowired
    private LogService logger;

    @Autowired
    MessageService messageService;

    @RequestMapping(path = "/send", method = RequestMethod.POST, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Sends a message within the conversation")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public RestResponse send(@RequestBody SendMessageParams params) {
        logger.debug("send message: " + params);
        messageService.send(params.getConversationId(), params.getMessage());
        return RestResponse.with("Message sent successfully.");
    }

    @RequestMapping(path = "/last/{id}", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Return last 20 messages from the conversation")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public List<MessageResponse> last(@PathVariable Long id) {
        logger.debug("get messages: " + id);
        List<MessageResponse> result = messageService.last20(id);
        return result;
    }

    @RequestMapping(path = "/before/{conversationId}/{messageId}", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Return 20 messages from the conversation older than given message")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public List<MessageResponse> before(@PathVariable Long conversationId, @PathVariable Long messageId) {
        logger.debug("before messages: " + conversationId + "/" + messageId);
        List<MessageResponse> result = messageService.before20(conversationId, messageId);
        return result;
    }

    @RequestMapping(path = "/unread", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Return ID's of groups with unread messages")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public List<Long> unread() {
        logger.debug("unread messages");
        List<Long> result = messageService.unread();
        return result;
    }
}
