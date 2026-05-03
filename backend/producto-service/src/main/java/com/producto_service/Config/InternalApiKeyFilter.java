package com.producto_service.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class InternalApiKeyFilter extends OncePerRequestFilter {

    private static final Set<String> RUTAS_INTERNAS = Set.of(
            "/productos/reducir-stock",
            "/productos/reposicion-stock"
    );

    @Value("${internal.api.key}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (RUTAS_INTERNAS.contains(request.getRequestURI())) {
            String header = request.getHeader("X-Internal-Api-Key");
            if (header == null || !header.equals(apiKey)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Acceso no autorizado");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
