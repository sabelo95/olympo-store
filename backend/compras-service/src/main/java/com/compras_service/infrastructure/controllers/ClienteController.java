package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.model.Cliente;
import com.compras_service.domain.usecase.ClienteUseCases.ActualizarClienteUseCase;
import com.compras_service.domain.usecase.ClienteUseCases.CrearClienteUseCase;
import com.compras_service.domain.usecase.ClienteUseCases.EliminarClienteUseCase;
import com.compras_service.domain.usecase.ClienteUseCases.ObtenerClienteUseCase;
import com.compras_service.infrastructure.adapters.DTOs.ClienteRequest;
import com.compras_service.infrastructure.adapters.DTOs.ClienteResponse;
import com.compras_service.infrastructure.adapters.mapper.web.ClienteWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "API para gestión de clientes")
public class ClienteController {

    private final CrearClienteUseCase crearClienteUseCase;
    private final ObtenerClienteUseCase obtenerClienteUseCase;
    private final ActualizarClienteUseCase actualizarClienteUseCase;
    private final EliminarClienteUseCase eliminarClienteUseCase;
    private final ClienteWebMapper clienteWebMapper;

    @PostMapping
    @Operation(summary = "Crear Cliente", description = "Crea un nuevo cliente en el sistema. Requiere que el usuario asociado exista y tenga rol CLIENTE. Valida que el NIT no esté duplicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida - NIT duplicado, usuario no existe o datos incompletos", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<Object> crearCliente(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del cliente a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequest.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody ClienteRequest request) {
        try {
            Cliente cliente = clienteWebMapper.toDomain(request);
            Cliente nuevo = crearClienteUseCase.crearCliente(cliente);
            return ResponseEntity.ok(clienteWebMapper.toResponse(nuevo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear el cliente: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el cliente: " + e.getMessage());
        }
    }

    @GetMapping("/{nit}")
    @Operation(summary = "Obtener lista de cliente por NIT", description = "Obtiene la información completa de un cliente utilizando su número de identificación tributaria (NIT).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado con el NIT proporcionado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\n  \"error\": \"Cliente no encontrado con el NIT: 123456789\"\n}")))
    })
    public ResponseEntity<?> obtenerCliente(
            @Parameter(description = "Número de identificación tributaria (NIT) del cliente", required = true, example = "123456789")
            @PathVariable String nit) {
        try {
            Cliente cliente = obtenerClienteUseCase.obtenerPorNit(nit);
            if (cliente != null) {
                return ResponseEntity.ok(clienteWebMapper.toResponse(cliente));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Cliente no encontrado con el NIT: " + nit));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Cliente no encontrado con el NIT: " + nit));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener cliente por usuarioId")
    public ResponseEntity<?> obtenerClientePorUsuarioId(
            @PathVariable Long usuarioId) {
        try {
            Cliente cliente = obtenerClienteUseCase.obtenerPorUsuarioId(usuarioId);
            return ResponseEntity.ok(clienteWebMapper.toResponse(cliente));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{nit}")
    @Operation(summary = "Actualizar un cliente", description = "Actualiza un cliente existente. El NIT se toma de la URL y no puede ser modificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<Object> actualizarCliente(
            @Parameter(description = "NIT del cliente a actualizar", required = true, example = "123456789")
            @PathVariable String nit,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del cliente",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequest.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody ClienteRequest request) {
        try {
            Cliente cliente = clienteWebMapper.toDomain(request);
            cliente.setNit(nit);
            actualizarClienteUseCase.actualizarCliente(cliente);
            return ResponseEntity.ok("Cliente actualizado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("/{nit}")
    @Operation(summary = "Eliminar Cliente", description = "Elimina un cliente por su NIT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> eliminarCliente(
            @Parameter(description = "NIT del cliente a eliminar", required = true, example = "123456789")
            @PathVariable String nit) {
        try {
            eliminarClienteUseCase.eliminarCliente(nit);
            return ResponseEntity.ok("Cliente eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el cliente: " + e.getMessage());
        }
    }
}
