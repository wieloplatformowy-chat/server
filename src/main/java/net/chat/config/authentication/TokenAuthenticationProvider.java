package net.chat.config.authentication;

import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * @author Mariusz Gorzycki
 * @since 03.04.2016
 */
@Service
public class TokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((Optional<String>) authentication.getPrincipal()).orNull();

        if (!tokenService.contains(token)) {
            throw new BadCredentialsException("Invalid token or token expired");
        }

        return tokenService.retrieve(token);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}