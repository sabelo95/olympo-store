package com.loginservice.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String testSecret;

    @BeforeEach
    void setUp() {
        // Generate a test secret
        testSecret = Base64.getEncoder().encodeToString("test-secret-key-for-jwt-testing-purposes-only".getBytes());
        jwtUtil = new JwtUtil(testSecret);
    }

    @Test
    void generarAccessToken_ValidInput_ReturnsToken() {
        // Given
        String correo = "test@test.com";
        String rol = "ADMINISTRADOR";
        Long id = 123L;

        // When
        String token = jwtUtil.generarAccessToken(correo, rol,id);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verify token can be parsed
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(generateKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        
        assertEquals(correo, claims.getSubject());
        assertEquals(rol, claims.get("rol"));
    }

    @Test
    void generarRefreshToken_ValidInput_ReturnsToken() {
        // Given
        String correo = "test@test.com";

        // When
        String token = jwtUtil.generarRefreshToken(correo);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verify token can be parsed
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(generateKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        
        assertEquals(correo, claims.getSubject());
    }

    @Test
    void obtenerCorreoDesdeToken_ValidToken_ReturnsCorreo() {
        // Given
        String correo = "test@test.com";
        String token = jwtUtil.generarAccessToken(correo, "USER", 1L);

        // When
        String result = jwtUtil.obtenerCorreoDesdeToken(token);

        // Then
        assertEquals(correo, result);
    }

    @Test
    void obtenerRolDesdeToken_ValidToken_ReturnsRol() {
        // Given
        String correo = "test@test.com";
        String rol = "ADMINISTRADOR";
        Long id = 123L;
        String token = jwtUtil.generarAccessToken(correo, rol, id);

        // When
        String result = jwtUtil.obtenerRolDesdeToken(token);

        // Then
        assertEquals(rol, result);
    }

    @Test
    void validarToken_ValidToken_ReturnsTrue() {
        // Given
        String token = jwtUtil.generarAccessToken("test@test.com", "USER", 1L);

        // When
        boolean result = jwtUtil.validarToken(token);

        // Then
        assertTrue(result);
    }

    @Test
    void validarToken_InvalidToken_ReturnsFalse() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        boolean result = jwtUtil.validarToken(invalidToken);

        // Then
        assertFalse(result);
    }

    @Test
    void validarToken_ExpiredToken_ReturnsFalse() {
        // Given
        JwtUtil expiredJwtUtil = new JwtUtil(testSecret);
        // Create a token with very short expiration
        Key key = generateKey();
        String expiredToken = Jwts.builder()
            .setSubject("test@test.com")
            .setExpiration(new java.util.Date(System.currentTimeMillis() - 1000))
            .signWith(key)
            .compact();

        // When
        boolean result = expiredJwtUtil.validarToken(expiredToken);

        // Then
        assertFalse(result);
    }

    @Test
    void generarAccessToken_DifferentRoles_DifferentTokens() {
        // Given
        String correo = "test@test.com";
        String rol1 = "ADMIN";
        String rol2 = "USER";
        Long id = 123L;


        // When
        String token1 = jwtUtil.generarAccessToken(correo, rol1, id);
        String token2 = jwtUtil.generarAccessToken(correo, rol2, id);

        // Then
        assertNotEquals(token1, token2);
        assertEquals(rol1, jwtUtil.obtenerRolDesdeToken(token1));
        assertEquals(rol2, jwtUtil.obtenerRolDesdeToken(token2));
    }

    private Key generateKey() {
        byte[] keyBytes = Base64.getDecoder().decode(testSecret);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }
}


