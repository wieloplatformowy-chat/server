package net.chat.rest.old;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.chat.config.authentication.TokenService;
import net.chat.entity.UserEntity;
import net.chat.exception.UserNotExistsException;
import net.chat.logging.LogService;
import net.chat.rest.dto.UserWithoutPasswordDto;
import net.chat.rest.message.DataResponse;
import net.chat.rest.message.Errors;
import net.chat.rest.message.ResponseError;
import net.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/old/token")
public class OldTokenRestController {
    @Autowired
    private LogService logger;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @RequestMapping(path = "/check", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    public DataResponse<String> checkToken(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("X-Auth-Token");
        if (token == null)
            token = httpRequest.getParameter("api_key");

        logger.debug("token checked: " + token);

        if (tokenService.contains(token))
            return DataResponse.success("Token valid: " + token);

        throw new InvalidTokenException("Token invalid: " + token);
    }

    @RequestMapping(path = "/whoami", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", dataType = "string", paramType = "header")
    @ApiOperation(value = "Returns user associated with given token")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = DataResponse.class),
            @ApiResponse(code = 400, message = "Failure", response = ResponseError.class),
            @ApiResponse(code = 403, message = "Forbidden")})
    public Object whoAmI(HttpServletRequest httpRequest) {
        logger.debug("whoami");

        UserEntity loggedUser = userService.getLoggedUser();

        return DataResponse.success(UserWithoutPasswordDto.fromEntity(loggedUser));
    }

    public static class InvalidTokenException extends IllegalArgumentException {
        public InvalidTokenException(String s) {
            super(s);
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({UserNotExistsException.class, InvalidTokenException.class})
    public DataResponse handleUNEException(Exception e) throws Throwable {
        String message = e.getMessage() != null ? e.getMessage() : "Invalid token or null";
        logger.error(message);
        return DataResponse.error(Errors.INVALID_TOKEN, message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable.class)
    public DataResponse handleException(Throwable throwable) throws Throwable {
        logger.error(throwable.getClass().getName() + ": " + throwable.getMessage());
        return DataResponse.error(Errors.UNKNOWN_ERROR, throwable.getMessage());
    }
}
