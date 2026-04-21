package com.loginservice.Controller;

import com.loginservice.DTO.CrearUsuarioRequest;
import com.loginservice.DTO.UsuarioDTO;
import com.loginservice.DTO.UsuarioResponse;
import com.loginservice.Model.Usuario;
import com.loginservice.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación, registro y gestión de tokens JWT")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Verificar estado del servicio de autenticación",
            description = "Devuelve un mensaje confirmando que el servicio de autenticación está activo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio activo",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(example = "Servicio de autenticación activo")))
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Servicio de autenticación activo", HttpStatus.OK);
    }


    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario con correo y contraseña, retorna tokens JWT (access y refresh)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"accessToken\": \"eyJhbGci...\", \"refreshToken\": \"eyJhbGci...\"}"))),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO usuarioDTO) {
        String correo = usuarioDTO.getCorreo();
        String contrasena = usuarioDTO.getContrasena();
        return usuarioService.login(correo, contrasena);
    }

    @PostMapping("/crear")
    @Operation(
            summary = "Crear nuevo usuario",
            description = "Crea un nuevo usuario en el sistema. Solo usuarios con rol ADMINISTRADOR pueden crear usuarios."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado exitosamente",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\n  \"error\": \"El correo es obligatorio\"\n}"))
            ),
            @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMINISTRADOR"),
            @ApiResponse(responseCode = "409", description = "El correo ya está registrado")
    })
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        try {
            UsuarioResponse response = usuarioService.crearUsuario(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El correo ya está registrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear usuario: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Retorna la información de un usuario específico por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "500", description = "Usuario no encontrado")
    })
    @GetMapping("obtener/{id}")
    public Usuario obtenerUsuario(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Long id) {
        return usuarioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Operation(
            summary = "Endpoint protegido",
            description = "Endpoint de prueba que requiere autenticación JWT válida",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Acceso permitido"),
            @ApiResponse(responseCode = "401", description = "No autenticado - Token JWT requerido")
    })
    @GetMapping("/protegido")
    public String accesoProtegido() {
        return "✅ Acceso permitido con JWT válido";
    }

    @Operation(
            summary = "Renovar token de acceso",
            description = "Genera un nuevo access token usando un refresh token válido"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token renovado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"accessToken\": \"eyJhbGci...\", \"refreshToken\": \"eyJhbGci...\"}"))),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        return usuarioService.refreshToken(request.get("refreshToken"));
    }

    @Operation(summary = "Listar todos los usuarios", description = "Retorna la lista completa de usuarios registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponse.class)))
    })
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "El correo ya está registrado")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CrearUsuarioRequest request) {
        try {
            UsuarioResponse response = usuarioService.actualizarUsuario(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    }



