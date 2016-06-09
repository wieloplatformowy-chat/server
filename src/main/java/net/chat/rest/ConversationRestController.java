package net.chat.rest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.chat.logging.LogService;
import net.chat.rest.dto.ConversationResponse;
import net.chat.rest.message.ResponseError;
import net.chat.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    ConversationService conversationService;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Returns conversation with given user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 401, message = "Unauthorized")})
    public ConversationResponse get(@PathVariable Long id) {
        logger.debug("get conversation: " + id);

        ConversationResponse response = conversationService.getOrCreateConversationWithUser(id);

        return response;
    }
}
