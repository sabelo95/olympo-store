package com.producto_service.Service;

import com.producto_service.DTO.TamanoDTO;
import com.producto_service.Model.Tamano;
import com.producto_service.Repository.TamanoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TamanoService {

    private final TamanoRepository tamanoRepository;



    // Obtener todos los tamaños
    public List<TamanoDTO> getAllTamanos() {
        return tamanoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener solo tamaños activos
    public List<TamanoDTO> getActiveTamanos() {
        return tamanoRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener tamaño por ID
    public TamanoDTO getTamanoById(Integer id) {
        Tamano tamano = tamanoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tamaño no encontrado con id: " + id));
        return convertToDTO(tamano);
    }

    // Crear nuevo tamaño
    @Transactional
    public TamanoDTO createTamano(TamanoDTO tamanoDTO) {
        // Validar que no exista el nombre
        if (tamanoRepository.existsByNombre(tamanoDTO.getNombre())) {
            throw new RuntimeException("Ya existe un tamaño con el nombre: " + tamanoDTO.getNombre());
        }

        Tamano tamano = convertToEntity(tamanoDTO);
        Tamano savedTamano = tamanoRepository.save(tamano);
        return convertToDTO(savedTamano);
    }

    // Actualizar tamaño
    @Transactional
    public TamanoDTO updateTamano(Integer id, TamanoDTO tamanoDTO) {
        Tamano tamano = tamanoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tamaño no encontrado con id: " + id));

        // Validar que no exista otro tamaño con el mismo nombre
        if (!tamano.getNombre().equals(tamanoDTO.getNombre()) &&
                tamanoRepository.existsByNombre(tamanoDTO.getNombre())) {
            throw new RuntimeException("Ya existe un tamaño con el nombre: " + tamanoDTO.getNombre());
        }

        tamano.setNombre(tamanoDTO.getNombre());
        tamano.setActivo(tamanoDTO.getActivo());

        Tamano updatedTamano = tamanoRepository.save(tamano);
        return convertToDTO(updatedTamano);
    }

    // Eliminar tamaño (soft delete)
    @Transactional
    public void deleteTamano(Integer id) {
        Tamano tamano = tamanoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tamaño no encontrado con id: " + id));

        tamano.setActivo(false);
        tamanoRepository.save(tamano);
    }

    // Eliminar permanentemente
    @Transactional
    public void hardDeleteTamano(Integer id) {
        if (!tamanoRepository.existsById(id)) {
            throw new RuntimeException("Tamaño no encontrado con id: " + id);
        }
        tamanoRepository.deleteById(id);
    }

    // Métodos de conversión
    private TamanoDTO convertToDTO(Tamano tamano) {
        TamanoDTO dto = new TamanoDTO();
        dto.setId(tamano.getId());
        dto.setNombre(tamano.getNombre());
        dto.setActivo(tamano.getActivo());
        return dto;
    }

    private Tamano convertToEntity(TamanoDTO dto) {
        Tamano tamano = new Tamano();
        tamano.setNombre(dto.getNombre());
        tamano.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return tamano;
    }
}