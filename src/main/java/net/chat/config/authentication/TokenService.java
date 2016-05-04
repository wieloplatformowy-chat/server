package net.chat.config.authentication;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * @author Mariusz Gorzycki
 * @since 03.04.2016
 */
@Service
public class TokenService {
    private static final Ehcache restApiAuthTokenCache;
    public static final int HALF_AN_HOUR_IN_MILLISECONDS = 30 * 60 * 1000;

    static {
        CacheManager.getInstance().addCache("restApiAuthTokenCache");
        restApiAuthTokenCache = CacheManager.getInstance().getEhcache("restApiAuthTokenCache");
    }

    @Scheduled(fixedRate = HALF_AN_HOUR_IN_MILLISECONDS)
    public void evictExpiredTokens() {
        restApiAuthTokenCache.evictExpiredElements();
    }

    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    public void store(String token, Authentication authentication) {
        TokenService.restApiAuthTokenCache.put(new Element(token, authentication));
    }

    public boolean contains(String token) {
        return restApiAuthTokenCache.get(token) != null;
    }

    public boolean remove(Authentication authentication) {
        Map<Object, Element> all = restApiAuthTokenCache.<Object, Element>getAll(restApiAuthTokenCache.getKeys());
        for (Map.Entry<Object, Element> entry : all.entrySet()) {
            if (entry.getValue().getObjectValue() == authentication)
                return restApiAuthTokenCache.remove(entry.getKey());
        }

        return false;
    }

    public Authentication retrieve(String token) {
        return (Authentication) restApiAuthTokenCache.get(token).getObjectValue();
    }
}