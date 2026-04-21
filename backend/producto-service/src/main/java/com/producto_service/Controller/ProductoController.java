package com.producto_service.Controller;

import com.producto_service.DTO.RequestProductoDto;
import com.producto_service.DTO.ProductoResponseDto;
import com.producto_service.Model.Producto;
import com.producto_service.Service.FileUploadService;
import com.producto_service.Service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoController {

    private final ProductoService productoService;
    private final FileUploadService fileUploadService;

    public ProductoController(ProductoService productoService, FileUploadService fileUploadService) {
        this.productoService = productoService;
        this.fileUploadService=fileUploadService;
    }


    @Operation(
            summary = "Verificar estado del servicio de productos",
            description = "Devuelve un mensaje confirmando que el servicio de productos está activo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio activo",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(example = "Servicio de productos activo")))
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Servicio de productos activo", HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/categoria/{nombre}")
    @Operation(summary = "Obtener productos por categoría", description = "Retorna todos los productos que pertenecen a una categoría específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> obtenerPorCategoria(
            @Parameter(description = "Nombre de la categoría", required = true, example = "Electrónicos")
            @PathVariable String nombre) {
        List<Producto> productos = productoService.obtenerProductosPorCategoria(nombre);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/marca/{nombre}")
    @Operation(summary = "Obtener productos por marca", description = "Retorna todos los productos que pertenecen a una marca específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<String>> obtenerPorMarca(
            @Parameter(description = "Nombre de la marca", required = true, example = "Samsung")
            @PathVariable String nombre) {
        List<String> marcas = productoService.obtenerProductosPorMarca(nombre);
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/lista-ids")
    @Operation(summary = "Obtener productos por lista de IDs", description = "Retorna productos basados en una lista de IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados"),
            @ApiResponse(responseCode = "400", description = "Lista de IDs inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> obtenerPorIds(
            @Parameter(description = "Lista de IDs de productos", required = true, example = "1,2,3")
            @RequestParam List<Long> ids) {
        List<Producto> productos = productoService.obtenerProductosPorIds(ids);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{nombre}")
    @Operation(summary = "Obtener producto por nombre", description = "Retorna un producto específico por su nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerPorNombre(
            @Parameter(description = "Nombre del producto", required = true, example = "Laptop Dell")
            @PathVariable String nombre) {
        Producto producto = productoService.obtenerProductoPorNombre(nombre);
        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró un producto con nombre: " + nombre);
        }
        return ResponseEntity.ok(producto);
    }



    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto en el sistema con imágenes. Requiere rol de ADMINISTRADOR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o producto ya existe"),
            @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMINISTRADOR"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> crearProducto(
            @Valid @RequestPart("producto") RequestProductoDto productoDto,
            @RequestPart(value = "imagenGeneral", required = false) MultipartFile imagenGeneral,
            @RequestPart(value = "imagenNutricional", required = false) MultipartFile imagenNutricional
    ) {
        try {
            // Guardar las imágenes si vienen
            if (imagenGeneral != null && !imagenGeneral.isEmpty()) {
                String nombreImagenGeneral = fileUploadService.guardarImagen(imagenGeneral, "generales");
                System.out.println(nombreImagenGeneral);
                productoDto.setImagenGeneral(nombreImagenGeneral);
            }

            if (imagenNutricional != null && !imagenNutricional.isEmpty()) {
                String nombreImagenNutricional = fileUploadService.guardarImagen(imagenNutricional, "nutricionales");
                productoDto.setImagenNutricional(nombreImagenNutricional);
            }

            ProductoResponseDto nuevo = productoService.crearProducto(productoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar las imágenes: " + e.getMessage());
        }
    }

    @PutMapping("/{nombre}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente por su nombre. Requiere rol de ADMINISTRADOR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMINISTRADOR"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> actualizarProducto(
            @Parameter(description = "Nombre del producto a actualizar", required = true, example = "Laptop Dell")
            @PathVariable String nombre,
            @Valid @RequestBody RequestProductoDto productoDto) {

        Producto productoExistente = productoService.obtenerProductoPorNombre(nombre);
        if (productoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El producto con nombre '" + nombre + "' no existe.");
        }

        Producto actualizado = productoService.actualizarProducto(nombre, productoDto);
        return ResponseEntity.ok(actualizado);
    }



    @DeleteMapping("/{nombre}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema por su nombre. Requiere rol de ADMINISTRADOR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMINISTRADOR"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> eliminarProducto(
            @Parameter(description = "Nombre del producto a eliminar", required = true, example = "Laptop Dell")
            @PathVariable String nombre) {
        try {
            productoService.eliminarProducto(nombre);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/reducir-stock")
    @Operation(summary = "Reducir stock de productos", description = "Reduce el stock de múltiples productos. El cuerpo debe contener un mapa de ID de producto -> cantidad a reducir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Stock reducido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o stock insuficiente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> reduccionStock(
            @Parameter(description = "Mapa de ID de producto a cantidad a reducir", required = true, example = "{\"1\": 5, \"2\": 10}")
            @RequestBody Map<Long, Integer> productos) {
        productoService.reduccionStock(productos);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reposicion-stock")
    @Operation(summary = "Reponer stock de productos", description = "Aumenta el stock de múltiples productos. El cuerpo debe contener un mapa de ID de producto -> cantidad a reponer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Stock repuesto exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> reposicionStock(
            @Parameter(description = "Mapa de ID de producto a cantidad a reponer", required = true, example = "{\"1\": 5, \"2\": 10}")
            @RequestBody Map<Long, Integer> productos) {
        productoService.reposicionStock(productos);
        return ResponseEntity.noContent().build();
    }
}
