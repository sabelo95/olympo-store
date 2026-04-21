//package com.Gateway.ApiGateway.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.ReactiveSecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import javax.crypto.SecretKey;
//import java.util.Base64;
//import java.util.Collections;
//import java.util.List;
//
//@Component
//public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//
//    private final SecretKey secretKey;
//
//    // Lista de rutas públicas que NO requieren JWT
//    private static final List<String> PUBLIC_PATHS = List.of(
//            // Auth service
//            "/auth/login",
//            "/auth/register",
//            "/auth/refresh",
//            "/auth/health",
//            "/auth/obtener",
//
//            // Gateway health
//            "/gateway/health",
//
//            // Health checks de microservicios
//            "/productos/health",
//            "/api/health",
//
//            // Swagger UI
//            "/swagger-ui.html",
//            "/swagger-ui/",
//            "/webjars/",
//            "/swagger-resources",
//
//            // API Docs
//            "/v3/api-docs",
//            "/auth-api-docs",
//            "/productos-api-docs",
//            "/categorias-api-docs",
//            "/marcas-api-docs",
//            "/reporte-api-docs",
//            "/ordenes-api-docs",
//            "/notificaciones-api-docs",
//
//            // Endpoints públicos de productos
//            "/productos/lista-ids",
//            "/productos/reducir-stock",
//            "/productos/reposicion-stock",
//            "/reporte/inventario-bajo"
//    );
//
//    public JwtAuthenticationFilter(@Value("${jwt.secret}") String secret) {
//        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
//        logger.info("JwtAuthenticationFilter inicializado correctamente.");
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getURI().getPath();
//        logger.debug("Procesando petición a: {}", path);
//
//        // Verificar si la ruta es pública
//        if (isPublicPath(path)) {
//            logger.debug("Ruta pública detectada: {}, saltando validación JWT", path);
//            return chain.filter(exchange);
//        }
//
//        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            logger.warn("JWT ausente o mal formado en path: {}", path);
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        String tokenJwt = authHeader.substring(7);
//        try {
//            // Validar token y obtener claims
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(secretKey)
//                    .build()
//                    .parseClaimsJws(tokenJwt)
//                    .getBody();
//
//            String username = claims.getSubject();
//            logger.debug("JWT válido. Usuario: {}", username);
//
//            // Crear Authentication para Spring Security
//            UsernamePasswordAuthenticationToken auth =
//                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
//
//            // Colocar Authentication en el contexto reactivo
//            return chain.filter(exchange)
//                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
//
//        } catch (JwtException e) {
//            logger.error("Error al validar JWT en path {}: {}", path, e.getMessage());
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//    }
//
//    /**
//     * Verifica si una ruta es pública (no requiere JWT)
//     */
//    private boolean isPublicPath(String path) {
//        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
//    }
//
//    @Override
//    public int getOrder() {
//        return -1; // Ejecutar antes de otros filtros
//    }
//}