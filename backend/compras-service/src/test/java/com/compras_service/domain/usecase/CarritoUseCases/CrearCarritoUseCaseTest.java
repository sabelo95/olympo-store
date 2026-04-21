package com.compras_service.domain.usecase.CarritoUseCases;

import com.compras_service.domain.gateways.Carrito.CarritoGateway;
import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.model.Carrito;
import com.compras_service.domain.model.CarritoProducto;
import com.compras_service.domain.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearCarritoUseCaseTest {

    @Mock
    private CarritoGateway carritoGateway;

    @Mock
    private ProductoGateway productoGateway;

    @InjectMocks
    private CrearCarritoUseCase crearCarritoUseCase;

    private Carrito carrito;
    private CarritoProducto carritoProducto;
    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setPrecio(100.0);
        producto.setCantidad(10);

        carritoProducto = new CarritoProducto();
        carritoProducto.setProducto_id(1L);
        carritoProducto.setCantidad(5);

        carrito = new Carrito();
        carrito.setCliente_id(1L);
        carrito.setProductos(List.of(carritoProducto));
    }

    @Test
    void crearCarrito_ValidCarrito_CreatesCarrito() {
        // Given
        when(productoGateway.obtenerProductoPorId(List.of(1L)))
            .thenReturn(Optional.of(List.of(producto)));
        when(carritoGateway.crearCarrito(any(Carrito.class))).thenReturn(carrito);
        doNothing().when(productoGateway).reducirStock(anyMap());

        // When
        Carrito result = crearCarritoUseCase.crearCarrito(carrito);

        // Then
        assertNotNull(result);
        verify(carritoGateway).crearCarrito(any(Carrito.class));
        verify(productoGateway).reducirStock(anyMap());
    }

    @Test
    void crearCarrito_InvalidProduct_ThrowsException() {
        // Given
        when(productoGateway.obtenerProductoPorId(List.of(1L)))
            .thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            crearCarritoUseCase.crearCarrito(carrito));
    }

    @Test
    void crearCarrito_ZeroStock_ThrowsException() {
        // Given
        producto.setCantidad(0);
        when(productoGateway.obtenerProductoPorId(List.of(1L)))
            .thenReturn(Optional.of(List.of(producto)));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            crearCarritoUseCase.crearCarrito(carrito));
    }

    @Test
    void crearCarrito_InsufficientStock_ThrowsException() {
        // Given
        carritoProducto.setCantidad(20); // More than available stock
        when(productoGateway.obtenerProductoPorId(List.of(1L)))
            .thenReturn(Optional.of(List.of(producto)));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            crearCarritoUseCase.crearCarrito(carrito));
    }
}



