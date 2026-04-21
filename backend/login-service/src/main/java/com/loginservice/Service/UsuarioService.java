package com.loginservice.Service;

import com.loginservice.Config.JwtUtil;
import com.loginservice.DTO.CrearUsuarioRequest;
import com.loginservice.DTO.UsuarioResponse;
import com.loginservice.Model.Usuario;
import com.loginservice.Repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> login(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario != null && passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            String accessToken = jwtUtil.generarAccessToken(usuario.getCorreo(), usuario.getRol(),usuario.getId());
            String refreshToken = jwtUtil.generarRefreshToken(usuario.getCorreo());

            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));
        } else if (usuario != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    public ResponseEntity<?> refreshToken(String refreshToken) {
        if (jwtUtil.validarToken(refreshToken)) {
            String correo = jwtUtil.obtenerCorreoDesdeToken(refreshToken);
            Usuario usuario = usuarioRepository.findByCorreo(correo);

            if (usuario != null) {
                String newAccessToken = jwtUtil.generarAccessToken(usuario.getCorreo(), usuario.getRol(),usuario.getId());
                return ResponseEntity.ok(Map.of(
                        "accessToken", newAccessToken,
                        "refreshToken", refreshToken
                ));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token inválido");
    }

    public UsuarioResponse crearUsuario(CrearUsuarioRequest request) {
        // Verificar si el correo ya existe
        if (usuarioRepository.findByCorreo(request.getCorreo()) != null) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // Mapear DTO a entidad
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setCorreo(request.getCorreo());
        nuevoUsuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        nuevoUsuario.setRol(request.getRol());
        nuevoUsuario.setActivo(true);


        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);


        return new UsuarioResponse(
                usuarioGuardado.getId(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getCorreo(),
                usuarioGuardado.getRol(),
                usuarioGuardado.isActivo()
        );
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNombre(), u.getCorreo(), u.getRol(), u.isActivo()))
                .toList();
    }

    public UsuarioResponse actualizarUsuario(Long id, CrearUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (!usuario.getCorreo().equals(request.getCorreo())
                && usuarioRepository.findByCorreo(request.getCorreo()) != null) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }
        usuario.setRol(request.getRol());

        Usuario guardado = usuarioRepository.save(usuario);
        return new UsuarioResponse(guardado.getId(), guardado.getNombre(), guardado.getCorreo(), guardado.getRol(), guardado.isActivo());
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}