//package com.loginservice.Service;
//
//import com.loginservice.Config.JwtUtil;
//import com.loginservice.Model.Usuario;
//import com.loginservice.Repository.UsuarioRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UsuarioServiceTest {
//
//    @Mock
//    private UsuarioRepository usuarioRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    @InjectMocks
//    private UsuarioService usuarioService;
//
//    private Usuario usuario;
//    private String correo = "test@test.com";
//    private String contrasena = "password123";
//    private String encodedPassword = "$2a$10$encoded";
//
//    @BeforeEach
//    void setUp() {
//        usuario = new Usuario();
//        usuario.setId(1L);
//        usuario.setCorreo(correo);
//        usuario.setContrasena(encodedPassword);
//        usuario.setNombre("Test User");
//        usuario.setRol("ADMINISTRADOR");
//        usuario.setActivo(true);
//    }
//
//    @Test
//    void login_Success_ReturnsTokens() {
//        // Given
//        when(usuarioRepository.findByCorreo(correo)).thenReturn(usuario);
//        when(passwordEncoder.matches(contrasena, encodedPassword)).thenReturn(true);
//        when(jwtUtil.generarAccessToken(correo, "ADMINISTRADOR", usuario.getId())).thenReturn("accessToken");
//        when(jwtUtil.generarRefreshToken(correo)).thenReturn("refreshToken");
//
//        // When
//        ResponseEntity<?> response = usuarioService.login(correo, contrasena);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        verify(jwtUtil).generarAccessToken(correo, "ADMINISTRADOR", usuario.getId());
//        verify(jwtUtil).generarRefreshToken(correo);
//    }
//
//    @Test
//    void login_IncorrectPassword_ReturnsUnauthorized() {
//        // Given
//        when(usuarioRepository.findByCorreo(correo)).thenReturn(usuario);
//        when(passwordEncoder.matches(contrasena, encodedPassword)).thenReturn(false);
//
//        // When
//        ResponseEntity<?> response = usuarioService.login(correo, contrasena);
//
//        // Then
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//        assertEquals("Credenciales incorrectas", response.getBody());
//        verify(jwtUtil, never()).generarAccessToken(anyString(), anyString(), anyLong());
//    }
//
//    @Test
//    void login_UserNotFound_ReturnsNotFound() {
//        // Given
//        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);
//
//        // When
//        ResponseEntity<?> response = usuarioService.login(correo, contrasena);
//
//        // Then
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("Usuario no encontrado", response.getBody());
//    }
//
//    @Test
//    void refreshToken_ValidToken_ReturnsNewAccessToken() {
//        // Given
//        String refreshToken = "validRefreshToken";
//        when(jwtUtil.validarToken(refreshToken)).thenReturn(true);
//        when(jwtUtil.obtenerCorreoDesdeToken(refreshToken)).thenReturn(correo);
//        when(usuarioRepository.findByCorreo(correo)).thenReturn(usuario);
//        when(jwtUtil.generarAccessToken(correo, "ADMINISTRADOR",)).thenReturn("newAccessToken");
//
//        // When
//        ResponseEntity<?> response = usuarioService.refreshToken(refreshToken);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        verify(jwtUtil).generarAccessToken(correo, "ADMINISTRADOR");
//    }
//
//    @Test
//    void refreshToken_InvalidToken_ReturnsUnauthorized() {
//        // Given
//        String refreshToken = "invalidRefreshToken";
//        when(jwtUtil.validarToken(refreshToken)).thenReturn(false);
//
//        // When
//        ResponseEntity<?> response = usuarioService.refreshToken(refreshToken);
//
//        // Then
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//        assertEquals("Refresh token inválido", response.getBody());
//        verify(jwtUtil, never()).generarAccessToken(anyString(), anyString());
//    }
//
//    @Test
//    void refreshToken_UserNotFound_ReturnsUnauthorized() {
//        // Given
//        String refreshToken = "validRefreshToken";
//        when(jwtUtil.validarToken(refreshToken)).thenReturn(true);
//        when(jwtUtil.obtenerCorreoDesdeToken(refreshToken)).thenReturn(correo);
//        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);
//
//        // When
//        ResponseEntity<?> response = usuarioService.refreshToken(refreshToken);
//
//        // Then
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//    }
//
//    @Test
//    void crearUsuario_EncryptsPassword() {
//        // Given
//        Usuario newUsuario = new Usuario();
//        newUsuario.setContrasena("plainPassword");
//        newUsuario.setNombre("New User");
//        newUsuario.setCorreo("new@test.com");
//        newUsuario.setRol("USER");
//
//        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
//        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
//            Usuario u = invocation.getArgument(0);
//            u.setId(2L);
//            return u;
//        });
//
//        // When
//        Usuario result = usuarioService.crearUsuario(newUsuario);
//
//        // Then
//        assertEquals("encodedPassword", newUsuario.getContrasena());
//        assertNotNull(result.getId());
//        verify(passwordEncoder).encode("plainPassword");
//        verify(usuarioRepository).save(newUsuario);
//    }
//
//    @Test
//    void findById_ExistingUser_ReturnsUser() {
//        // Given
//        Long userId = 1L;
//        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
//
//        // When
//        Optional<Usuario> result = usuarioService.findById(userId);
//
//        // Then
//        assertTrue(result.isPresent());
//        assertEquals(usuario, result.get());
//        verify(usuarioRepository).findById(userId);
//    }
//
//    @Test
//    void findById_NonExistingUser_ReturnsEmpty() {
//        // Given
//        Long userId = 999L;
//        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // When
//        Optional<Usuario> result = usuarioService.findById(userId);
//
//        // Then
//        assertTrue(result.isEmpty());
//        verify(usuarioRepository).findById(userId);
//    }
//}
//
//
