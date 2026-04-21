package com.producto_service.Controller;

import com.producto_service.Model.Marca;
import com.producto_service.Service.MarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marcas")
@RequiredArgsConstructor
@Tag(name = "Marcas", description = "API para gestión de marcas")
public class MarcaController {

    private final MarcaService marcaService;



    @GetMapping
    @Operation(
            summary = "Obtener todas las marcas",
            description = "Retorna una lista de todas las marcas disponibles en el sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista de marcas obtenida exitosamente")
    public ResponseEntity<List<Marca>> obtenerTodasLasMarcas() {
        List<Marca> marcas = marcaService.obtenerTodasLasMarcas();
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/{nombre}")
    @Operation(
            summary = "Obtener marca por nombre",
            description = "Retorna una marca específica por su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca encontrada"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    public ResponseEntity<?> obtenerMarcaPorNombre(
            @Parameter(description = "Nombre de la marca", example = "Nike")
            @PathVariable String nombre) {

        Marca marca = marcaService.obtenerMarcaPorNombre(nombre);
        if (marca == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró una marca con el nombre: " + nombre);
        }
        return ResponseEntity.ok(marca);
    }



    @PostMapping
    @Operation(
            summary = "Crear nueva marca",
            description = "Crea una nueva marca en el sistema. Requiere rol de ADMINISTRADOR"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Marca creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crearMarca(@RequestBody Marca marca) {
        if (marca.getNombre() == null || marca.getNombre().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("El nombre de la marca no puede estar vacío.");
        }

        Marca nuevaMarca = marcaService.crearMarca(marca);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMarca);
    }



    @PutMapping("/{nombre}")
    @Operation(
            summary = "Actualizar marca",
            description = "Actualiza una marca existente por su nombre. Requiere rol de ADMINISTRADOR"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMINISTRADOR"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizarMarca(
            @Parameter(description = "Nombre de la marca a actualizar", example = "Nike")
            @PathVariable String nombre,
            @RequestBody Marca marcaActualizada) {

        Marca existente = marcaService.obtenerMarcaPorNombre(nombre);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La marca con nombre '" + nombre + "' no existe.");
        }

        Marca actualizada = marcaService.actualizarMarca(nombre, marcaActualizada);
        return ResponseEntity.ok(actualizada);
    }

    // ────────────────────────────────────────────────
    // 🔹 Eliminación
    // ────────────────────────────────────────────────

    @DeleteMapping("/{nombre}")
    @Operation(
            summary = "Eliminar marca",
            description = "Elimina una marca del sistema por su nombre. Requiere rol de ADMINISTRADOR"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Marca eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMINISTRADOR"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar la marca")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarMarca(
            @Parameter(description = "Nombre de la marca a eliminar", example = "Nike")
            @PathVariable String nombre) {

        Marca existente = marcaService.obtenerMarcaPorNombre(nombre);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró una marca con el nombre: " + nombre);
        }

        try {
            marcaService.eliminarMarca(nombre);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la marca: " + e.getMessage());
        }
    }
}
