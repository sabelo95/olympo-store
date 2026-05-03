package com.loginservice.Service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {

    private final Set<String> blacklist = Collections.synchronizedSet(new HashSet<>());

    public void invalidar(String token) {
        if (token != null && !token.isBlank()) {
            blacklist.add(token);
        }
    }

    public boolean estaInvalidado(String token) {
        return token != null && blacklist.contains(token);
    }
}
