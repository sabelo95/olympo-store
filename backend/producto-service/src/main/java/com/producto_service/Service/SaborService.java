package com.producto_service.Service;

import com.producto_service.DTO.SaborDTO;
import com.producto_service.Model.Sabor;
import com.producto_service.Repository.SaborRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SaborService {

    private final SaborRepository saborRepository;

    // Obtener todos los sabores
    public List<SaborDTO> getAllSabores() {
        return saborRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener solo sabores activos
    public List<SaborDTO> getActiveSabores() {
        return saborRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener sabor por ID
    public SaborDTO getSaborById(Integer id) {
        Sabor sabor = saborRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sabor no encontrado con id: " + id));
        return convertToDTO(sabor);
    }

    // Crear nuevo sabor
    @Transactional
    public SaborDTO createSabor(SaborDTO saborDTO) {
        // Validar que no exista el nombre
        if (saborRepository.existsByNombre(saborDTO.getNombre())) {
            throw new RuntimeException("Ya existe un sabor con el nombre: " + saborDTO.getNombre());
        }

        Sabor sabor = convertToEntity(saborDTO);
        Sabor savedSabor = saborRepository.save(sabor);
        return convertToDTO(savedSabor);
    }

    // Actualizar sabor
    @Transactional
    public SaborDTO updateSabor(Integer id, SaborDTO saborDTO) {
        Sabor sabor = saborRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sabor no encontrado con id: " + id));

        // Validar que no exista otro sabor con el mismo nombre
        if (!sabor.getNombre().equals(saborDTO.getNombre()) &&
                saborRepository.existsByNombre(saborDTO.getNombre())) {
            throw new RuntimeException("Ya existe un sabor con el nombre: " + saborDTO.getNombre());
        }

        sabor.setNombre(saborDTO.getNombre());
        sabor.setActivo(saborDTO.getActivo());

        Sabor updatedSabor = saborRepository.save(sabor);
        return convertToDTO(updatedSabor);
    }

    // Eliminar sabor (soft delete)
    @Transactional
    public void deleteSabor(Integer id) {
        Sabor sabor = saborRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sabor no encontrado con id: " + id));

        sabor.setActivo(false);
        saborRepository.save(sabor);
    }

    // Eliminar permanentemente
    @Transactional
    public void hardDeleteSabor(Integer id) {
        if (!saborRepository.existsById(id)) {
            throw new RuntimeException("Sabor no encontrado con id: " + id);
        }
        saborRepository.deleteById(id);
    }

    // Métodos de conversión
    private SaborDTO convertToDTO(Sabor sabor) {
        SaborDTO dto = new SaborDTO();
        dto.setId(sabor.getId());
        dto.setNombre(sabor.getNombre());
        dto.setActivo(sabor.getActivo());
        return dto;
    }

    private Sabor convertToEntity(SaborDTO dto) {
        Sabor sabor = new Sabor();
        sabor.setNombre(dto.getNombre());
        sabor.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return sabor;
    }
}