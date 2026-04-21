package com.producto_service.Controller;

import com.producto_service.DTO.CategoriaDto;
import com.producto_service.Model.Categoria;
import com.producto_service.Service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@AllArgsConstructor
@Tag(name = "Categorías", description = "API para gestión de categorías de productos")
public class CategoriaController {

    private final CategoriaService categoriaService;



    @GetMapping
    @Operation(summary = "Listar todas las categorías", description = "Obtiene todas las categorías disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    public ResponseEntity<List<Categoria>> obtenerTodasLasCategorias() {
        return ResponseEntity.ok(categoriaService.obtenerTodasLasCategorias());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Obtiene una categoría específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<?> obtenerCategoriaPorId(
            @Parameter(description = "ID de la categoría", required = true, example = "1")
            @PathVariable Long id) {

        Categoria categoria = categoriaService.obtenerCategoriaPorId(id);

        if (categoria == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe una categoría con el ID " + id);
        }

        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar categoría por nombre", description = "Busca una categoría por su nombre exacto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<?> obtenerCategoriaPorNombre(
            @Parameter(description = "Nombre de la categoría", required = true, example = "Electrónica")
            @RequestParam String nombre) {

        Categoria categoria = categoriaService.obtenerCategoriaPorNombre(nombre);

        if (categoria == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe una categoría con el nombre '" + nombre + "'");
        }

        return ResponseEntity.ok(categoria);
    }



    @PostMapping
    @Operation(
            summary = "Crear nueva categoría",
            description = "Crea una nueva categoría. Valida que el nombre no esté duplicado. Requiere rol ADMINISTRADOR"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o nombre duplicado"),
            @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crearCategoria(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva categoría",
                    required = true
            )
            @RequestBody CategoriaDto categoriaDto) {

        try {
            Categoria nuevaCategoria = categoriaService.crearCategoria(categoriaDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar categoría",
            description = "Actualiza los datos de una categoría existente. Permite cambiar el nombre validando que no esté duplicado. Requiere rol ADMINISTRADOR"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o nombre duplicado"),
            @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizarCategoria(
            @Parameter(description = "ID de la categoría a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la categoría",
                    required = true
            )
            @RequestBody CategoriaDto categoriaDto) {

        try {
            Categoria actualizada = categoriaService.actualizarCategoria(id, categoriaDto);
            return ResponseEntity.ok(actualizada);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar categoría por ID",
            description = "Elimina una categoría existente. No permite eliminar si tiene productos asociados. Requiere rol ADMINISTRADOR"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar - Tiene productos asociados"),
            @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarCategoriaPorId(
            @Parameter(description = "ID de la categoría a eliminar", required = true, example = "1")
            @PathVariable Long id) {

        try {
            categoriaService.eliminarCategoriaPorId(id);
            return ResponseEntity.ok("Categoría eliminada exitosamente");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/nombre/{nombre}")
    @Operation(
            summary = "Eliminar categoría por nombre",
            description = "Elimina una categoría por su nombre. No permite eliminar si tiene productos asociados. Requiere rol ADMINISTRADOR"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar - Tiene productos asociados"),
            @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarCategoriaPorNombre(
            @Parameter(description = "Nombre de la categoría a eliminar", required = true, example = "Electrónica")
            @PathVariable String nombre) {

        try {
            categoriaService.eliminarCategoriaPorNombre(nombre);
            return ResponseEntity.ok("Categoría eliminada exitosamente");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}