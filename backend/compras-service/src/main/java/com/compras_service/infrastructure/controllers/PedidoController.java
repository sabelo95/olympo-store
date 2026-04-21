package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.Enums.EstadoPedido;
import com.compras_service.domain.model.Pedido;
import com.compras_service.domain.usecase.PedidoUseCases.ActualizarPedidoUseCase;
import com.compras_service.domain.usecase.PedidoUseCases.CrearPedidoUseCase;
import com.compras_service.domain.usecase.PedidoUseCases.EliminarPedidoUseCase;
import com.compras_service.domain.usecase.PedidoUseCases.ObtenerPedidoUseCase;
import com.compras_service.infrastructure.adapters.DTOs.ActualizarPedidoRequest;
import com.compras_service.infrastructure.adapters.DTOs.CrearPedidoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@AllArgsConstructor
@Tag(name = "Pedidos", description = "API para gestión de pedidos")
public class PedidoController {

    private final CrearPedidoUseCase crearPedidoUseCase;
    private final ActualizarPedidoUseCase actualizarPedidoUseCase;
    private final EliminarPedidoUseCase eliminarPedidoUseCase;
    private final ObtenerPedidoUseCase obtenerPedidoUseCase;

    @GetMapping
    @Operation(summary = "Obtener todos los pedidos")
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        try {
            List<Pedido> pedidos = obtenerPedidoUseCase.obtenerTodos();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado", content = @Content(schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Pedido> obtenerPorId(
            @Parameter(description = "ID del pedido", required = true, example = "1")
            @PathVariable Long id) {
        try {
            return obtenerPedidoUseCase.obtenerPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener pedidos por cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "No se encontraron pedidos para el cliente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Pedido>> obtenerPorClienteId(
            @Parameter(description = "ID del cliente", required = true, example = "1")
            @PathVariable Long clienteId) {
        try {
            return obtenerPedidoUseCase.obtenerPorClienteId(clienteId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/estado/cancelados")
    @Operation(summary = "Obtener pedidos cancelados")
    public ResponseEntity<List<Pedido>> obtenerCancelados() {
        try {
            return obtenerPedidoUseCase.obtenerCancelados()
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/estado/completados")
    @Operation(summary = "Obtener pedidos completados")
    public ResponseEntity<List<Pedido>> obtenerCompletados() {
        try {
            return obtenerPedidoUseCase.obtenerCompletados()
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/estado/en-proceso")
    @Operation(summary = "Obtener pedidos en proceso")
    public ResponseEntity<List<Pedido>> obtenerEnProceso() {
        try {
            return obtenerPedidoUseCase.obtenerEnProceso()
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/carrito/{id}/cliente/{clienteId}")
    @Operation(summary = "Crear Pedido", description = "Crea un nuevo pedido a partir de un carrito de compra existente. El carrito se convierte en un pedido con los productos seleccionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Carrito o cliente no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> crearPedido(
            @Parameter(description = "ID del carrito de compra", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "ID del cliente propietario del carrito", required = true, example = "1")
            @PathVariable Long clienteId,
            @Parameter(description = "Dirección de envío del pedido", required = true, example = "Calle 123 #45-67, Bogotá")
            @RequestBody CrearPedidoRequest request) {
        try {
            Pedido pedido = crearPedidoUseCase.ejecutar(clienteId, id,  request.getDireccionEnvio(), request.getMetodoPago());
            return ResponseEntity.ok("Pedido creado con éxito + ID: " + pedido.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error al crear el pedido: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el pedido: " + e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    @Operation(summary = "Actualizar un pedido", description = "Actualiza un pedido existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado exitosamente", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<Object> actualizarPedido(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del pedido a actualizar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ActualizarPedidoRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody ActualizarPedidoRequest pedido) {
        try {
            actualizarPedidoUseCase.actualizarPedido(pedido);
            return ResponseEntity.ok("Pedido actualizado exitosamente.");
        } catch (IllegalArgumentException e) {
            // Devuelve el mensaje real que lanzó el use case
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            String mensaje = (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el pedido: " + mensaje);
        }
    }

    @PutMapping("/actualizar-estado")
    @Operation(summary = "Actualizar estado del pedido", description = "Actualiza el estado de un pedido. Estados válidos: PENDIENTE, CONFIRMADO, EN_PROCESO, ENVIADO, ENTREGADO, CANCELADO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del pedido actualizado exitosamente", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> cambiarEstadoPedido(
            @Parameter(description = "ID del pedido a actualizar", required = true, example = "1")
            @RequestParam Long id,
            @Parameter(description = "Nuevo estado del pedido", required = true, example = "CONFIRMADO",
                    schema = @Schema(allowableValues = {"PENDIENTE", "CONFIRMADO", "EN_PROCESO", "ENVIADO", "ENTREGADO", "CANCELADO"}))
            @RequestParam String estado) {
        try {
            EstadoPedido estadoNuevo = EstadoPedido.valueOf(estado.toUpperCase());
            actualizarPedidoUseCase.actualizarEstadoPedido(id, estadoNuevo);
            return ResponseEntity.ok("Estado del pedido actualizado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el estado del pedido: " + e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "Eliminar Pedido", description = "Elimina un pedido por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> eliminarPedido(
            @Parameter(description = "ID del pedido a eliminar", required = true, example = "1")
            @RequestParam Long id) {
        try {
            eliminarPedidoUseCase.eliminarPedido(id);
            return ResponseEntity.ok("Pedido eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el pedido: " + e.getMessage());
        }
    }
}
