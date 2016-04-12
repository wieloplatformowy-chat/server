package net.chat.rest;

import net.chat.config.authentication.TokenService;
import net.chat.entity.User;
import net.chat.logging.LogService;
import net.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest/users")
public class DiagnosticsRestController {
    @Autowired
    private LogService logger;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @RequestMapping(path = "/checktoken", method = RequestMethod.PATCH, produces = "application/json")
    public DataResponse<String> checkToken(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("X-Auth-Token");
        if (token == null)
            token = httpRequest.getParameter("api_key");

        logger.debug("token checked: " + token);

        if (tokenService.contains(token))
            return DataResponse.success("Token valid: " + token);

        return DataResponse.error(Errors.INVALID_TOKEN, "Token invalid: " + token);
    }

    @RequestMapping(path = "/whoami", method = RequestMethod.PATCH, produces = "application/json")
    public DataResponse<User> whoAmI(HttpServletRequest httpRequest) {
        logger.debug("whoami");

        User loggedUser = userService.getLoggedUser();

        return DataResponse.success(loggedUser);
    }

    @ExceptionHandler(UserService.UserAlreadyExistsException.class)
    public BaseResponse handleUAEException() throws Throwable {
        return BaseResponse.error(Errors.USERNAME_IS_TAKEN);
    }

    @ExceptionHandler(UserService.UserNotExistsException.class)
    public BaseResponse handleUNEException() throws Throwable {
        return BaseResponse.error(Errors.USER_NOT_EXISTS);
    }

    @ExceptionHandler(UserService.NullCredentialsException.class)
    public BaseResponse handleNCException() throws Throwable {
        return BaseResponse.error(Errors.CREDENTIALS_NOT_PROVIDED);
    }

    @ExceptionHandler(Throwable.class)
    public Throwable handleException(Throwable throwable) throws Throwable {
        throw throwable;
//        return throwable;
    }
}
