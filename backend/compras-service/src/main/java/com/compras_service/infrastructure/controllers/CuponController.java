package com.compras_service.infrastructure.controllers;

import com.compras_service.domain.model.Cupon;
import com.compras_service.domain.usecase.CuponUseCases.*;
import com.compras_service.infrastructure.adapters.DTOs.CuponRequest;
import com.compras_service.infrastructure.adapters.DTOs.CuponResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cupones")
@RequiredArgsConstructor
@Tag(name = "Cupones", description = "API para gestión y validación de cupones de descuento")
public class CuponController {

    private final ValidarCuponUseCase validarCuponUseCase;
    private final CrearCuponUseCase crearCuponUseCase;
    private final ObtenerCuponUseCase obtenerCuponUseCase;
    private final ActualizarCuponUseCase actualizarCuponUseCase;
    private final EliminarCuponUseCase eliminarCuponUseCase;

    // ─── PÚBLICO ────────────────────────────────────────────────────────────────

    @GetMapping("/validar/{codigo}")
    @Operation(summary = "Validar cupón", description = "Valida un cupón e incrementa su uso si es válido. No requiere autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupón válido",
                    content = @Content(schema = @Schema(implementation = CuponResponse.class))),
            @ApiResponse(responseCode = "400", description = "Cupón inválido, expirado, sin usos o monto insuficiente")
    })
    public ResponseEntity<?> validarCupon(
            @Parameter(description = "Código del cupón", required = true, example = "DESCUENTO10")
            @PathVariable String codigo,
            @Parameter(description = "Total de la compra en pesos", required = true, example = "100000")
            @RequestParam Long total) {
        try {
            Cupon cupon = validarCuponUseCase.validar(codigo, total);
            return ResponseEntity.ok(toResponse(cupon));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al validar el cupón: " + e.getMessage());
        }
    }

    // ─── ADMINISTRADOR ───────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Listar todos los cupones", description = "Requiere rol ADMINISTRADOR.")
    public ResponseEntity<?> listarCupones() {
        try {
            List<CuponResponse> cupones = obtenerCuponUseCase.obtenerTodos()
                    .stream().map(this::toResponse).toList();
            return ResponseEntity.ok(cupones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los cupones: " + e.getMessage());
        }
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Obtener cupón por código", description = "Requiere rol ADMINISTRADOR.")
    public ResponseEntity<?> obtenerCupon(
            @Parameter(description = "Código del cupón", required = true)
            @PathVariable String codigo) {
        try {
            return ResponseEntity.ok(toResponse(obtenerCuponUseCase.obtenerPorCodigo(codigo)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Crear cupón", description = "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cupón creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o código duplicado")
    })
    public ResponseEntity<?> crearCupon(
            @Valid @org.springframework.web.bind.annotation.RequestBody CuponRequest request) {
        try {
            Cupon cupon = toDomain(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(crearCuponUseCase.crear(cupon)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el cupón: " + e.getMessage());
        }
    }

    @PutMapping("/{codigo}")
    @Operation(summary = "Actualizar cupón", description = "El código no puede modificarse. Requiere rol ADMINISTRADOR.")
    public ResponseEntity<?> actualizarCupon(
            @Parameter(description = "Código del cupón a actualizar", required = true)
            @PathVariable String codigo,
            @Valid @org.springframework.web.bind.annotation.RequestBody CuponRequest request) {
        try {
            Cupon datos = toDomain(request);
            return ResponseEntity.ok(toResponse(actualizarCuponUseCase.actualizar(codigo, datos)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el cupón: " + e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Eliminar cupón", description = "Requiere rol ADMINISTRADOR.")
    public ResponseEntity<String> eliminarCupon(
            @Parameter(description = "Código del cupón a eliminar", required = true)
            @PathVariable String codigo) {
        try {
            eliminarCuponUseCase.eliminar(codigo);
            return ResponseEntity.ok("Cupón eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el cupón: " + e.getMessage());
        }
    }

    // ─── HELPERS ────────────────────────────────────────────────────────────────

    private Cupon toDomain(CuponRequest request) {
        Cupon cupon = new Cupon();
        cupon.setCodigo(request.getCodigo());
        cupon.setDescuentoPorcentaje(request.getDescuentoPorcentaje());
        cupon.setDescuentoFijo(request.getDescuentoFijo());
        cupon.setActivo(request.getActivo());
        cupon.setFechaExpiracion(request.getFechaExpiracion());
        cupon.setUsoMaximo(request.getUsoMaximo());
        cupon.setMontoMinimo(request.getMontoMinimo());
        return cupon;
    }

    private CuponResponse toResponse(Cupon cupon) {
        CuponResponse response = new CuponResponse();
        response.setId(cupon.getId());
        response.setCodigo(cupon.getCodigo());
        response.setDescuentoPorcentaje(cupon.getDescuentoPorcentaje());
        response.setDescuentoFijo(cupon.getDescuentoFijo());
        response.setActivo(cupon.isActivo());
        response.setFechaExpiracion(cupon.getFechaExpiracion());
        response.setUsoMaximo(cupon.getUsoMaximo());
        response.setUsoActual(cupon.getUsoActual());
        response.setMontoMinimo(cupon.getMontoMinimo());
        response.setValido(true);
        response.setMensaje("Cupón aplicado correctamente");
        return response;
    }
}
