package com.loginservice.Service;

import com.loginservice.Config.JwtUtil;
import com.loginservice.DTO.CrearUsuarioRequest;
import com.loginservice.DTO.UsuarioResponse;
import com.loginservice.Model.Usuario;
import com.loginservice.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private final String correo = "test@test.com";
    private final String contrasena = "password123";
    private final String encodedPassword = "$2a$10$encoded";

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo(correo);
        usuario.setContrasena(encodedPassword);
        usuario.setNombre("Test User");
        usuario.setRol("ADMINISTRADOR");
        usuario.setActivo(true);
    }

    // ── LOGIN ──────────────────────────────────────────────────────────────────

    @Test
    void login_credencialesValidas_retornaTokens() {
        // Given
        when(usuarioRepository.findByCorreo(correo)).thenReturn(usuario);
        when(passwordEncoder.matches(contrasena, encodedPassword)).thenReturn(true);
        when(jwtUtil.generarAccessToken(correo, "ADMINISTRADOR", 1L)).thenReturn("accessToken");
        when(jwtUtil.generarRefreshToken(correo)).thenReturn("refreshToken");

        // When
        ResponseEntity<?> response = usuarioService.login(correo, contrasena);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals("accessToken", body.get("accessToken"));
        assertEquals("refreshToken", body.get("refreshToken"));
        verify(jwtUtil).generarAccessToken(correo, "ADMINISTRADOR", 1L);
        verify(jwtUtil).generarRefreshToken(correo);
    }

    @Test
    void login_contrasenaIncorrecta_retornaUnauthorized() {
        // Given
        when(usuarioRepository.findByCorreo(correo)).thenReturn(usuario);
        when(passwordEncoder.matches(contrasena, encodedPassword)).thenReturn(false);

        // When
        ResponseEntity<?> response = usuarioService.login(correo, contrasena);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales incorrectas", response.getBody());
        verify(jwtUtil, never()).generarAccessToken(anyString(), anyString(), anyLong());
    }

    @Test
    void login_usuarioNoEncontrado_retornaNotFound() {
        // Given
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // When
        ResponseEntity<?> response = usuarioService.login(correo, contrasena);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuario no encontrado", response.getBody());
    }

    // ── REFRESH TOKEN ──────────────────────────────────────────────────────────

    @Test
    void refreshToken_tokenValido_retornaNuevoAccessToken() {
        // Given
        String refreshToken = "validRefreshToken";
        when(jwtUtil.validarToken(refreshToken)).thenReturn(true);
        when(jwtUtil.obtenerCorreoDesdeToken(refreshToken)).thenReturn(correo);
        when(usuarioRepository.findByCorreo(correo)).thenReturn(usuario);
        when(jwtUtil.generarAccessToken(correo, "ADMINISTRADOR", 1L)).thenReturn("newAccessToken");

        // When
        ResponseEntity<?> response = usuarioService.refreshToken(refreshToken);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals("newAccessToken", body.get("accessToken"));
        assertEquals(refreshToken, body.get("refreshToken"));
    }

    @Test
    void refreshToken_tokenInvalido_retornaUnauthorized() {
        // Given
        when(jwtUtil.validarToken("invalidToken")).thenReturn(false);

        // When
        ResponseEntity<?> response = usuarioService.refreshToken("invalidToken");

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Refresh token inválido", response.getBody());
        verify(jwtUtil, never()).generarAccessToken(anyString(), anyString(), anyLong());
    }

    @Test
    void refreshToken_usuarioNoEncontrado_retornaUnauthorized() {
        // Given
        String refreshToken = "validRefreshToken";
        when(jwtUtil.validarToken(refreshToken)).thenReturn(true);
        when(jwtUtil.obtenerCorreoDesdeToken(refreshToken)).thenReturn(correo);
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // When
        ResponseEntity<?> response = usuarioService.refreshToken(refreshToken);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    // ── CREAR USUARIO ──────────────────────────────────────────────────────────

    @Test
    void crearUsuario_correoYaExiste_lanzaExcepcion() {
        // Given
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setNombre("Nuevo Usuario");
        request.setCorreo(correo);
        request.setContrasena("password123");
        request.setRol("CLIENTE");

        when(usuarioRepository.findByCorreo(correo)).thenReturn(usuario);

        // When & Then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.crearUsuario(request)
        );
        assertEquals("El correo ya está registrado", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void crearUsuario_correoNuevo_encriptaContrasenaYGuarda() {
        // Given
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setNombre("Nuevo Usuario");
        request.setCorreo("nuevo@test.com");
        request.setContrasena("password123");
        request.setRol("CLIENTE");

        when(usuarioRepository.findByCorreo("nuevo@test.com")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(2L);
            return u;
        });

        // When
        UsuarioResponse result = usuarioService.crearUsuario(request);

        // Then
        assertNotNull(result);
        verify(passwordEncoder).encode("password123");
        verify(usuarioRepository).save(any(Usuario.class));
    }
}
