package net.chat.service;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mariusz Gorzycki
 * @since 15.06.2016
 */
@Service
public class OnlineService {
    private static final Ehcache onlineCache;

    @Autowired
    UserService userService;

    static {
        CacheManager.getInstance().addCache("onlineCache");
        onlineCache = CacheManager.getInstance().getEhcache("onlineCache");
    }

    public void markMeOnline() {
        Long myId = userService.getLoggedUser().getId();
        OnlineService.onlineCache.put(new Element(myId, myId, 15, 15));
    }

    public boolean isOnline(Long userId) {
        return onlineCache.get(userId) != null;
    }
}