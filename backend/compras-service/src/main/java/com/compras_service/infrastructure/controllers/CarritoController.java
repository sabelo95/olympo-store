package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.model.Carrito;
import com.compras_service.domain.usecase.CarritoUseCases.ActualizarCarritoUseCase;
import com.compras_service.domain.usecase.CarritoUseCases.CrearCarritoUseCase;
import com.compras_service.domain.usecase.CarritoUseCases.EliminarCarritoUseCase;
import com.compras_service.domain.usecase.CarritoUseCases.ObtenerCarritoUseCase;
import com.compras_service.infrastructure.adapters.DTOs.CarritoRequest;
import com.compras_service.infrastructure.adapters.DTOs.CarritoResponse;
import com.compras_service.infrastructure.adapters.mapper.web.CarritoWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carritos")
@AllArgsConstructor
@Tag(name = "Carritos", description = "API para gestión de carritos de compra")
public class CarritoController {

    private final CrearCarritoUseCase crearCarritoUseCase;
    private final ActualizarCarritoUseCase actualizarCarritoUseCase;
    private final ObtenerCarritoUseCase obtenerCarritoUseCase;;
    private final EliminarCarritoUseCase eliminarCarritoUseCase;
    private final CarritoWebMapper carritoWebMapper;

    @PostMapping
    @Operation(summary = "Crear Carrito", description = "Crea un nuevo carrito de compra con los productos especificados. Valida la disponibilidad de stock y reduce el inventario automáticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(schema = @Schema(implementation = CarritoResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<Object> crearCarrito(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del carrito a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CarritoRequest.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody CarritoRequest request) {
        try {
            var carrito = carritoWebMapper.toDomain(request);
            var result = crearCarritoUseCase.crearCarrito(carrito);
            return ResponseEntity.ok(carritoWebMapper.toResponse(result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el carrito: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/{clienteId}")
    @Operation(summary = "Actualizar un carrito", description = "Actualiza un carrito existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito actualizado exitosamente", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<Object> actualizarCarrito(
            @Parameter(description = "ID del carrito a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "ID del cliente propietario del carrito", required = true, example = "1")
            @PathVariable Long clienteId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del carrito",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CarritoRequest.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody CarritoRequest request) {
        try {
            Carrito carrito = carritoWebMapper.toDomain(request);
            actualizarCarritoUseCase.modificarCarrito(id, clienteId, carrito);
            return ResponseEntity.ok("Carrito actualizado exitosamente.");

        } catch (IllegalArgumentException | IllegalStateException ex) {

            return ResponseEntity.badRequest().body(ex.getMessage());

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + ex.getMessage());
        }
    }

    @GetMapping("/cliente/{clienteId}/activo")
    @Operation(summary = "Obtener carrito activo del cliente")
    public ResponseEntity<?> obtenerCarritoActivo(@PathVariable Long clienteId) {
        try {
            var carrito = obtenerCarritoUseCase.obtenerCarritoActivo(clienteId);
            return carrito
                    .map(c -> ResponseEntity.ok((Object) carritoWebMapper.toResponse(c)))
                    .orElse(ResponseEntity.noContent().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Carrito", description = "Elimina un carrito por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> eliminarCarrito(
            @Parameter(description = "ID del carrito a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        try {
            eliminarCarritoUseCase.eliminarCarrito(id);
            return ResponseEntity.ok("Carrito eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrito no encontrado.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el carrito: " + e.getMessage());
        }
    }
}
