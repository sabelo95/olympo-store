package com.producto_service.Controller;

import com.producto_service.DTO.TamanoDTO;
import com.producto_service.Service.TamanoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tamanos")
@CrossOrigin(origins = "*")
public class TamanoController {

    @Autowired
    private TamanoService tamanoService;

    // GET: Obtener todos los tamaños
    @GetMapping
    public ResponseEntity<List<TamanoDTO>> getAllTamanos() {
        List<TamanoDTO> tamanos = tamanoService.getAllTamanos();
        return ResponseEntity.ok(tamanos);
    }

    // GET: Obtener solo tamaños activos
    @GetMapping("/activos")
    public ResponseEntity<List<TamanoDTO>> getActiveTamanos() {
        List<TamanoDTO> tamanos = tamanoService.getActiveTamanos();
        return ResponseEntity.ok(tamanos);
    }

    // GET: Obtener tamaño por ID
    @GetMapping("/{id}")
    public ResponseEntity<TamanoDTO> getTamanoById(@PathVariable Integer id) {
        try {
            TamanoDTO tamano = tamanoService.getTamanoById(id);
            return ResponseEntity.ok(tamano);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: Crear nuevo tamaño
    @PostMapping
    public ResponseEntity<?> createTamano(@Valid @RequestBody TamanoDTO tamanoDTO) {
        try {
            TamanoDTO createdTamano = tamanoService.createTamano(tamanoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTamano);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: Actualizar tamaño
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTamano(
            @PathVariable Integer id,
            @Valid @RequestBody TamanoDTO tamanoDTO) {
        try {
            TamanoDTO updatedTamano = tamanoService.updateTamano(id, tamanoDTO);
            return ResponseEntity.ok(updatedTamano);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: Desactivar tamaño (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTamano(@PathVariable Integer id) {
        try {
            tamanoService.deleteTamano(id);
            return ResponseEntity.ok("Tamaño desactivado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: Eliminar permanentemente
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<?> hardDeleteTamano(@PathVariable Integer id) {
        try {
            tamanoService.hardDeleteTamano(id);
            return ResponseEntity.ok("Tamaño eliminado permanentemente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
