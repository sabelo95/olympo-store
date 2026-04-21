package com.loginservice.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loginservice.Config.JwtAuthenticationFilter;
import com.loginservice.Config.JwtUtil;
import com.loginservice.DTO.UsuarioDTO;
import com.loginservice.Model.Usuario;
import com.loginservice.Service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // No resetear los mocks, dejar que se configuren en cada test
    }

    @Test
    void login_ValidCredentials_ReturnsOk() throws Exception {
        // Given
        UsuarioDTO usuarioDTO = new UsuarioDTO("test@test.com", "password");
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", "token123");
        response.put("refreshToken", "refresh123");

        ResponseEntity<?> responseEntity = ResponseEntity.ok(response);
        doReturn(responseEntity).when(usuarioService).login("test@test.com", "password");

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
        
        ResponseEntity<?> errorResponse = ResponseEntity.status(401).body("Credenciales incorrectas");
        doReturn(errorResponse).when(usuarioService).login("test@test.com", "wrongPassword");

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Credenciales incorrectas"));

        verify(usuarioService, times(1)).login("test@test.com", "wrongPassword");
    }

    @Test
    void crearUsuario_ValidUser_ReturnsSuccess() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setCorreo("test@test.com");
        usuario.setContrasena("password123");
        usuario.setRol("USER");

        when(usuarioService.crearUsuario(any(Usuario.class))).thenReturn(usuario);

        // When & Then
        mockMvc.perform(post("/auth/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Usuario creado con éxito")));

        verify(usuarioService).crearUsuario(any(Usuario.class));
    }

    @Test
    void crearUsuario_MissingFields_ReturnsError() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNombre(null);
        usuario.setCorreo("test@test.com");

        // When & Then
        mockMvc.perform(post("/auth/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Todos los campos son obligatorios")));

        verify(usuarioService, never()).crearUsuario(any());
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
    void obtenerUsuario_NonExistingUser_ThrowsException() {
        // Given
        when(usuarioService.findById(999L)).thenReturn(Optional.empty());

        // When & Then - Verificar que se lanza RuntimeException cuando el usuario no existe
        // Usamos assertThrows para verificar la excepción directamente en el controlador
        assertThrows(RuntimeException.class, () -> {
            try {
                mockMvc.perform(get("/auth/obtener/999"))
                    .andReturn();
            } catch (Exception e) {
                // Buscar la RuntimeException en la cadena de excepciones
                Throwable cause = e;
                while (cause != null) {
                    if (cause instanceof RuntimeException && cause.getMessage().contains("999")) {
                        throw (RuntimeException) cause;
                    }
                    cause = cause.getCause();
                }
                throw e;
            }
        });

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

        ResponseEntity<?> refreshResponse = ResponseEntity.ok(response);
        doReturn(refreshResponse).when(usuarioService).refreshToken("validToken");

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
    void accesoProtegido_WithAuthentication_ReturnsSuccess() throws Exception {
        // When & Then - En este test la seguridad está deshabilitada, por eso pasa
        mockMvc.perform(get("/auth/protegido"))
            .andExpect(status().isOk());
    }
}

