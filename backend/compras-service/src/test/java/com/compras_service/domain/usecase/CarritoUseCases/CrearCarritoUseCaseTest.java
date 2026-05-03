package com.compras_service.domain.usecase.CarritoUseCases;

import com.compras_service.domain.gateways.Carrito.CarritoGateway;
import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.model.Carrito;
import com.compras_service.domain.model.CarritoProducto;
import com.compras_service.domain.model.Producto;
import com.compras_service.domain.services.StockService;
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
    private StockService stockService;

    @InjectMocks
    private CrearCarritoUseCase crearCarritoUseCase;

    private Carrito carrito;
    private CarritoProducto carritoProducto;

    @BeforeEach
    void setUp() {
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
        doNothing().when(stockService).validarCarrito(any(Carrito.class));
        doNothing().when(stockService).reducirStockDelCarrito(any(Carrito.class));
        when(carritoGateway.crearCarrito(any(Carrito.class))).thenReturn(carrito);

        // When
        Carrito result = crearCarritoUseCase.crearCarrito(carrito);

        // Then
        assertNotNull(result);
        verify(stockService).validarCarrito(any(Carrito.class));
        verify(stockService).reducirStockDelCarrito(any(Carrito.class));
        verify(carritoGateway).crearCarrito(any(Carrito.class));
    }

    @Test
    void crearCarrito_StockValidationFails_ThrowsException() {
        // Given
        doThrow(new IllegalArgumentException("Uno o más productos no existen"))
                .when(stockService).validarCarrito(any(Carrito.class));

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                crearCarritoUseCase.crearCarrito(carrito));

        verify(carritoGateway, never()).crearCarrito(any(Carrito.class));
    }

    @Test
    void crearCarrito_InsufficientStock_ThrowsException() {
        // Given
        doThrow(new IllegalArgumentException("Stock insuficiente"))
                .when(stockService).validarCarrito(any(Carrito.class));

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                crearCarritoUseCase.crearCarrito(carrito));
    }

    @Test
    void crearCarrito_EmptyProducts_ThrowsException() {
        // Given
        doThrow(new IllegalArgumentException("El carrito debe tener al menos un producto"))
                .when(stockService).validarCarrito(any(Carrito.class));

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                crearCarritoUseCase.crearCarrito(carrito));
    }
}
