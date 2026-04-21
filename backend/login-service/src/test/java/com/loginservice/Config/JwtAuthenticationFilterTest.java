package com.loginservice.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_ValidToken_SetsAuthentication() throws Exception {
        // Given
        String token = "validToken123";
        request.addHeader("Authorization", "Bearer " + token);
        
        when(jwtUtil.validarToken(token)).thenReturn(true);
        when(jwtUtil.obtenerCorreoDesdeToken(token)).thenReturn("test@test.com");
        when(jwtUtil.obtenerRolDesdeToken(token)).thenReturn("ADMINISTRADOR");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil).validarToken(token);
        verify(jwtUtil).obtenerCorreoDesdeToken(token);
        verify(jwtUtil).obtenerRolDesdeToken(token);
    }

    @Test
    void doFilterInternal_InvalidToken_NoAuthenticationSet() throws Exception {
        // Given
        String token = "invalidToken123";
        request.addHeader("Authorization", "Bearer " + token);
        
        when(jwtUtil.validarToken(token)).thenReturn(false);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil).validarToken(token);
        verify(jwtUtil, never()).obtenerCorreoDesdeToken(anyString());
    }

    @Test
    void doFilterInternal_NoAuthorizationHeader_NoAuthenticationSet() throws Exception {
        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil, never()).validarToken(anyString());
    }

    @Test
    void doFilterInternal_NoBearerPrefix_NoAuthenticationSet() throws Exception {
        // Given
        request.addHeader("Authorization", "Invalid " + "someToken");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil, never()).validarToken(anyString());
    }

    @Test
    void doFilterInternal_CallsFilterChain() throws Exception {
        // Given
        FilterChain mockFilterChain = mock(FilterChain.class);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, mockFilterChain);

        // Then
        verify(mockFilterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_DifferentRoles_SetsCorrectAuthority() throws Exception {
        // Given
        String token = "validToken";
        request.addHeader("Authorization", "Bearer " + token);
        
        when(jwtUtil.validarToken(token)).thenReturn(true);
        when(jwtUtil.obtenerCorreoDesdeToken(token)).thenReturn("admin@test.com");
        when(jwtUtil.obtenerRolDesdeToken(token)).thenReturn("ADMINISTRADOR");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertTrue(authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR")));
    }
}


