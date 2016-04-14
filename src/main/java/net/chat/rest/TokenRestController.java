package net.chat.rest;

import net.chat.config.authentication.TokenService;
import net.chat.entity.User;
import net.chat.logging.LogService;
import net.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

        return DataResponse.error(Errors.INVALID_TOKEN, "Token invalid: " + token);
    }

    @RequestMapping(path = "/whoami", method = RequestMethod.GET, produces = "application/json")
    public DataResponse<User> whoAmI(HttpServletRequest httpRequest) {
        logger.debug("whoami");

        User loggedUser = userService.getLoggedUser();

        return DataResponse.success(loggedUser);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable.class)
    public DataResponse<Throwable> handleException(Throwable throwable) throws Throwable {
        return DataResponse.error(Errors.UNKNOWN_ERROR, throwable);
    }
}
