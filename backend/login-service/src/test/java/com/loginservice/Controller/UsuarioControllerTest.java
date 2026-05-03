package com.loginservice.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loginservice.Config.JwtAuthenticationFilter;
import com.loginservice.Config.JwtUtil;
import com.loginservice.DTO.CrearUsuarioRequest;
import com.loginservice.DTO.UsuarioDTO;
import com.loginservice.DTO.UsuarioResponse;
import com.loginservice.Model.Usuario;
import com.loginservice.Service.LoginRateLimiterService;
import com.loginservice.Service.TokenBlacklistService;
import com.loginservice.Service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private LoginRateLimiterService rateLimiter;

    @MockBean
    private TokenBlacklistService blacklist;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_ValidCredentials_ReturnsOk() throws Exception {
        // Given
        UsuarioDTO usuarioDTO = new UsuarioDTO("test@test.com", "password");
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", "token123");
        response.put("refreshToken", "refresh123");

        // Rate limiter must allow the request
        when(rateLimiter.permitir(any())).thenReturn(true);
        doReturn(ResponseEntity.ok(response)).when(usuarioService).login("test@test.com", "password");

        // When & Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token123"))
                .andExpect(jsonPath("$.refreshToken").value("refresh123"));

        verify(usuarioService, times(1)).login("test@test.com", "password");
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        // Given
        UsuarioDTO usuarioDTO = new UsuarioDTO("test@test.com", "wrongPassword");

        when(rateLimiter.permitir(any())).thenReturn(true);
        doReturn(ResponseEntity.status(401).body("Credenciales incorrectas"))
                .when(usuarioService).login("test@test.com", "wrongPassword");

        // When & Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciales incorrectas"));

        verify(usuarioService, times(1)).login("test@test.com", "wrongPassword");
    }

    @Test
    void crearUsuario_ValidUser_ReturnsCreated() throws Exception {
        // Given — controller accepts CrearUsuarioRequest, returns 201
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setNombre("Test User");
        request.setCorreo("test@test.com");
        request.setContrasena("password123");
        request.setRol("CLIENTE");

        UsuarioResponse usuarioResponse = new UsuarioResponse(1L, "Test User", "test@test.com", "CLIENTE", true);
        when(usuarioService.crearUsuario(any(CrearUsuarioRequest.class))).thenReturn(usuarioResponse);

        // When & Then
        mockMvc.perform(post("/auth/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(usuarioService).crearUsuario(any(CrearUsuarioRequest.class));
    }

    @Test
    void crearUsuario_DuplicateEmail_ReturnsConflict() throws Exception {
        // Given — controller catches IllegalArgumentException and returns 409
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setNombre("Test User");
        request.setCorreo("test@test.com");
        request.setContrasena("password123");
        request.setRol("CLIENTE");

        when(usuarioService.crearUsuario(any(CrearUsuarioRequest.class)))
                .thenThrow(new IllegalArgumentException("El correo ya está registrado"));

        // When & Then
        mockMvc.perform(post("/auth/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(usuarioService).crearUsuario(any(CrearUsuarioRequest.class));
    }

    @Test
    void obtenerUsuario_ExistingUser_ReturnsUser() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test User");

        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));

        // When & Then
        mockMvc.perform(get("/auth/obtener/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test User"));

        verify(usuarioService).findById(1L);
    }

    @Test
    void obtenerUsuario_NonExistingUser_ThrowsRuntimeException() {
        // Given — controller calls orElseThrow(RuntimeException)
        // MockMvc wraps the unhandled RuntimeException in a ServletException and re-throws it
        when(usuarioService.findById(999L)).thenReturn(Optional.empty());

        // When & Then — the ServletException propagates out of perform()
        org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class,
                () -> mockMvc.perform(get("/auth/obtener/999"))
        );

        verify(usuarioService, times(1)).findById(999L);
    }

    @Test
    void refresh_ValidToken_ReturnsNewAccessToken() throws Exception {
        // Given
        Map<String, String> request = new HashMap<>();
        request.put("refreshToken", "validToken");

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", "newToken");
        response.put("refreshToken", "validToken");

        when(blacklist.estaInvalidado("validToken")).thenReturn(false);
        doReturn(ResponseEntity.ok(response)).when(usuarioService).refreshToken("validToken");

        // When & Then
        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newToken"))
                .andExpect(jsonPath("$.refreshToken").value("validToken"));

        verify(usuarioService, times(1)).refreshToken("validToken");
    }

    @Test
    void accesoProtegido_WithFiltersDisabled_ReturnsSuccess() throws Exception {
        // Security filters are disabled via @AutoConfigureMockMvc(addFilters = false)
        mockMvc.perform(get("/auth/protegido"))
                .andExpect(status().isOk());
    }
}
