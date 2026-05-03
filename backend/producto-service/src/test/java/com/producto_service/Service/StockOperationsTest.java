package com.producto_service.Service;

import com.producto_service.Mapper.ProductoMapper;
import com.producto_service.Model.Producto;
import com.producto_service.Repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockOperationsTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private MarcaService marcaService;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private HistorialService historialService;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Whey Protein");
        producto.setPrecio(120000.0);
        producto.setCantidad(50);
    }

    // ── REDUCCIÓN DE STOCK ─────────────────────────────────────────────────────

    @Test
    void reducirStock_cantidadValida_actualizaCantidad() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(historialService).agregarHistorial(any(Producto.class), any(Integer.class));

        // When
        productoService.reduccionStock(Map.of(1L, 10));

        // Then
        assertEquals(40, producto.getCantidad());
        verify(productoRepository).save(producto);
        verify(historialService).agregarHistorial(producto, 40);
    }

    @Test
    void reducirStock_stockInsuficiente_lanzaExcepcion() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When & Then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productoService.reduccionStock(Map.of(1L, 100))
        );
        assertTrue(ex.getMessage().contains("Stock insuficiente"));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void reducirStock_productoInexistente_lanzaExcepcion() {
        // Given
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productoService.reduccionStock(Map.of(99L, 5))
        );
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void reducirStock_cantidadCero_lanzaExcepcion() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When & Then — cantidad <= 0 debe rechazarse
        assertThrows(
                IllegalArgumentException.class,
                () -> productoService.reduccionStock(Map.of(1L, 0))
        );
        verify(productoRepository, never()).save(any());
    }

    @Test
    void reducirStock_cantidadNegativa_lanzaExcepcion() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When & Then
        assertThrows(
                IllegalArgumentException.class,
                () -> productoService.reduccionStock(Map.of(1L, -5))
        );
    }

    // ── REPOSICIÓN DE STOCK ────────────────────────────────────────────────────

    @Test
    void reponerStock_cantidadValida_incrementaCantidad() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(historialService).agregarHistorial(any(Producto.class), any(Integer.class));

        // When
        productoService.reposicionStock(Map.of(1L, 20));

        // Then
        assertEquals(70, producto.getCantidad());
        verify(productoRepository).save(producto);
        verify(historialService).agregarHistorial(producto, 70);
    }

    @Test
    void reponerStock_productoInexistente_lanzaExcepcion() {
        // Given
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productoService.reposicionStock(Map.of(99L, 10))
        );
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void reponerStock_cantidadCero_lanzaExcepcion() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When & Then
        assertThrows(
                IllegalArgumentException.class,
                () -> productoService.reposicionStock(Map.of(1L, 0))
        );
        verify(productoRepository, never()).save(any());
    }

    // ── OPERACIONES CON MÚLTIPLES PRODUCTOS ───────────────────────────────────

    @Test
    void reducirStock_variosProductos_actualizaTodos() {
        // Given
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Creatina");
        producto2.setCantidad(30);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto2));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(historialService).agregarHistorial(any(Producto.class), any(Integer.class));

        // When
        productoService.reduccionStock(Map.of(1L, 5, 2L, 10));

        // Then
        assertEquals(45, producto.getCantidad());
        assertEquals(20, producto2.getCantidad());
        verify(productoRepository, times(2)).save(any(Producto.class));
    }
}
