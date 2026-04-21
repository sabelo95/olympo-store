package com.producto_service.Controller;

import com.producto_service.DTO.SaborDTO;
import com.producto_service.Service.SaborService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sabores")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class SaborController {

    private final SaborService saborService;

    // GET: Obtener todos los sabores
    @GetMapping
    public ResponseEntity<List<SaborDTO>> getAllSabores() {
        List<SaborDTO> sabores = saborService.getAllSabores();
        return ResponseEntity.ok(sabores);
    }

    // GET: Obtener solo sabores activos
    @GetMapping("/activos")
    public ResponseEntity<List<SaborDTO>> getActiveSabores() {
        List<SaborDTO> sabores = saborService.getActiveSabores();
        return ResponseEntity.ok(sabores);
    }

    // GET: Obtener sabor por ID
    @GetMapping("/{id}")
    public ResponseEntity<SaborDTO> getSaborById(@PathVariable Integer id) {
        try {
            SaborDTO sabor = saborService.getSaborById(id);
            return ResponseEntity.ok(sabor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: Crear nuevo sabor
    @PostMapping
    public ResponseEntity<?> createSabor(@Valid @RequestBody SaborDTO saborDTO) {
        try {
            SaborDTO createdSabor = saborService.createSabor(saborDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSabor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: Actualizar sabor
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSabor(
            @PathVariable Integer id,
            @Valid @RequestBody SaborDTO saborDTO) {
        try {
            SaborDTO updatedSabor = saborService.updateSabor(id, saborDTO);
            return ResponseEntity.ok(updatedSabor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: Desactivar sabor (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSabor(@PathVariable Integer id) {
        try {
            saborService.deleteSabor(id);
            return ResponseEntity.ok("Sabor desactivado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: Eliminar permanentemente
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<?> hardDeleteSabor(@PathVariable Integer id) {
        try {
            saborService.hardDeleteSabor(id);
            return ResponseEntity.ok("Sabor eliminado permanentemente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
