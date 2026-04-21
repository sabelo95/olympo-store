package com.producto_service.Service;

import com.producto_service.DTO.CategoriaDto;
import com.producto_service.Model.Categoria;
import com.producto_service.Repository.CategoriaRepository;
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
class CategoriaServiceTestSimple {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaDto categoriaDto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Electrónica");
        categoria.setDescripcion("Productos electrónicos");

        categoriaDto = new CategoriaDto();
        categoriaDto.setNombre("Electrónica");
        categoriaDto.setDescripcion("Productos electrónicos");
    }

    @Test
    void test1_obtenerTodasLasCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<Categoria> resultado = categoriaService.obtenerTodasLasCategorias();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Electrónica", resultado.get(0).getNombre());
        verify(categoriaRepository).findAll();
    }

    @Test
    void test2_crearCategoria_exitoso() {
        when(categoriaRepository.findByNombre("Electrónica")).thenReturn(null);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria resultado = categoriaService.crearCategoria(categoriaDto);

        assertNotNull(resultado);
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void test3_crearCategoria_nombreDuplicado_lanzaExcepcion() {
        when(categoriaRepository.findByNombre("Electrónica")).thenReturn(categoria);

        assertThrows(IllegalArgumentException.class, () -> {
            categoriaService.crearCategoria(categoriaDto);
        });

        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void test4_eliminarCategoria_exitoso() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        categoriaService.eliminarCategoriaPorId(1L);

        verify(categoriaRepository).delete(categoria);
    }

    @Test
    void test5_existeCategoria_retornaTrue() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        boolean resultado = categoriaService.existeCategoria(1L);

        assertTrue(resultado);
        verify(categoriaRepository).existsById(1L);
    }
}