package com.mutle.mutle.jwt;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {
    private final Set<String> blacklist= ConcurrentHashMap.newKeySet();

    public boolean isBlackListed(String token){
        return blacklist.contains(token);
    }

    public boolean addBlackList(String token){
        return blacklist.add(token);
    }


}