package com.producto_service.Service;

import com.producto_service.Model.*;
import com.producto_service.Repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTestSimple {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("iPhone 15");
        producto.setPrecio(999.99);
        producto.setCantidad(10);
    }

    @Test
    void test1_obtenerTodosLosProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.obtenerTodosLosProductos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("iPhone 15", resultado.get(0).getNombre());
        verify(productoRepository).findAll();
    }

    @Test
    void test2_obtenerProductoPorNombre_cuandoExiste() {
        when(productoRepository.findByNombre("iPhone 15"))
                .thenReturn(Optional.of(producto));

        Producto resultado = productoService.obtenerProductoPorNombre("iPhone 15");

        assertNotNull(resultado);
        assertEquals("iPhone 15", resultado.getNombre());
        assertEquals(999.99, resultado.getPrecio());
    }

    @Test
    void test3_obtenerProductoPorNombre_cuandoNoExiste() {
        when(productoRepository.findByNombre("Samsung Galaxy"))
                .thenReturn(Optional.empty());

        Producto resultado = productoService.obtenerProductoPorNombre("Samsung Galaxy");

        assertNull(resultado);
    }

    @Test
    void test4_eliminarProducto_exitoso() {
        when(productoRepository.findByNombre("iPhone 15"))
                .thenReturn(Optional.of(producto));

        productoService.eliminarProducto("iPhone 15");

        verify(productoRepository).delete(producto);
    }

    @Test
    void test5_eliminarProducto_queNoExiste_lanzaExcepcion() {
        when(productoRepository.findByNombre("Producto Inexistente"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            productoService.eliminarProducto("Producto Inexistente");
        });
    }



    @Test
    void test9_obtenerProductosConStockBajo() {
        Producto producto1 = new Producto();
        producto1.setNombre("Producto A");
        producto1.setCantidad(3);

        Producto producto2 = new Producto();
        producto2.setNombre("Producto B");
        producto2.setCantidad(20);

        when(productoRepository.findAll()).thenReturn(List.of(producto1, producto2));

        List<Producto> resultado = productoService.obtenerProductosConStockMenorA(10);

        assertEquals(1, resultado.size());
        assertEquals("Producto A", resultado.get(0).getNombre());
    }

    @Test
    void test10_obtenerProductosPorIds() {
        List<Long> ids = List.of(1L);
        when(productoRepository.findAllById(ids)).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.obtenerProductosPorIds(ids);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("iPhone 15", resultado.get(0).getNombre());
    }
}
