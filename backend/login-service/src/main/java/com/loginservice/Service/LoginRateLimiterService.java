package com.loginservice.Service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginRateLimiterService {

    private static final int MAX_INTENTOS = 10;
    private static final long VENTANA_MS = 15 * 60 * 1000L;

    private final Map<String, List<Long>> intentos = new ConcurrentHashMap<>();

    public boolean permitir(String ip) {
        long ahora = System.currentTimeMillis();
        List<Long> tiempos = intentos.computeIfAbsent(ip, k -> new ArrayList<>());
        synchronized (tiempos) {
            tiempos.removeIf(t -> ahora - t > VENTANA_MS);
            if (tiempos.size() >= MAX_INTENTOS) return false;
            tiempos.add(ahora);
        }
        return true;
    }
}
