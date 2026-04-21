package com.producto_service.Service;

import com.producto_service.Model.Marca;
import com.producto_service.Repository.MarcaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private MarcaService marcaService;

    private Marca marca;

    @BeforeEach
    void setUp() {
        marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Samsung");
    }

    @Test
    void obtenerTodasLasMarcas_ReturnsList() {
        // Given
        when(marcaRepository.findAll()).thenReturn(List.of(marca));

        // When
        List<Marca> result = marcaService.obtenerTodasLasMarcas();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(marcaRepository).findAll();
    }

    @Test
    void obtenerMarcaPorNombre_ExistingMarca_ReturnsMarca() {
        // Given
        when(marcaRepository.findByNombre("Samsung")).thenReturn(Optional.of(marca));

        // When
        Marca result = marcaService.obtenerMarcaPorNombre("Samsung");

        // Then
        assertNotNull(result);
        assertEquals("Samsung", result.getNombre());
    }

    @Test
    void obtenerMarcaPorNombre_NonExistingMarca_ThrowsException() {
        // Given
        when(marcaRepository.findByNombre("No existe")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            marcaService.obtenerMarcaPorNombre("No existe"));
    }

    @Test
    void crearMarca_ValidMarca_CreatesMarca() {
        // Given
        when(marcaRepository.save(marca)).thenReturn(marca);

        // When
        Marca result = marcaService.crearMarca(marca);

        // Then
        assertNotNull(result);
        verify(marcaRepository).save(marca);
    }

    @Test
    void eliminarMarca_ExistingMarca_DeletesMarca() {
        // Given
        when(marcaRepository.findByNombre("Samsung")).thenReturn(Optional.of(marca));

        // When
        marcaService.eliminarMarca("Samsung");

        // Then
        verify(marcaRepository).delete(marca);
    }

    @Test
    void actualizarMarca_ValidMarca_UpdatesMarca() {
        // Given
        Marca marcaActualizada = new Marca();
        marcaActualizada.setNombre("Samsung Updated");
        
        when(marcaRepository.findByNombre("Samsung")).thenReturn(Optional.of(marca));
        when(marcaRepository.save(any(Marca.class))).thenReturn(marca);

        // When
        Marca result = marcaService.actualizarMarca("Samsung", marcaActualizada);

        // Then
        assertNotNull(result);
        verify(marcaRepository).save(any(Marca.class));
    }

    @Test
    void validarMarca_ExistingMarca_ReturnsTrue() {
        // Given
        when(marcaRepository.findAll()).thenReturn(List.of(marca));

        // When
        Boolean result = marcaService.validarMarca("Samsung");

        // Then
        assertTrue(result);
    }

    @Test
    void validarMarca_NonExistingMarca_ReturnsFalse() {
        // Given
        when(marcaRepository.findAll()).thenReturn(List.of(marca));

        // When
        Boolean result = marcaService.validarMarca("Apple");

        // Then
        assertFalse(result);
    }
}


