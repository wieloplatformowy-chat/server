package net.chat.rest;

import net.chat.config.authentication.TokenService;
import net.chat.entity.UserEntity;
import net.chat.logging.LogService;
import net.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/token")
public class TokenRestController {
    @Autowired
    private LogService logger;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @RequestMapping(path = "/check", method = RequestMethod.GET, produces = "application/json")
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
    public DataResponse<UserEntity> whoAmI(HttpServletRequest httpRequest) {
        logger.debug("whoami");

        UserEntity loggedUser = userService.getLoggedUser();

        return DataResponse.success(loggedUser);
    }

    @RequestMapping(path = "/version", method = RequestMethod.PATCH, produces = "application/json")
    public String version() {
        logger.debug("version");
        return "1 - cross origin added";
    }

    public static class InvalidTokenException extends IllegalArgumentException {
        public InvalidTokenException(String s) {
            super(s);
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserService.UserNotExistsException.class)
    public DataResponse handleUNEException(Exception e) throws Throwable {
        logger.error("Invalid token or null");
        return DataResponse.error(Errors.INVALID_TOKEN, "Invalid token or null");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidTokenException.class)
    public DataResponse handleITException(Exception e) throws Throwable {
        logger.error(e.getMessage());
        return DataResponse.error(Errors.INVALID_TOKEN, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable.class)
    public DataResponse handleException(Throwable throwable) throws Throwable {
        logger.error(throwable.getClass().getName() + ": " + throwable.getMessage());
        return DataResponse.error(Errors.UNKNOWN_ERROR, throwable.getMessage());
    }
}
