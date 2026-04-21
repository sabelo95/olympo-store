package com.compras_service.domain.gateways.Carrito;

import com.compras_service.domain.model.Carrito;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CarritoGateway {
    public Carrito crearCarrito(Carrito carrito);
    public Carrito actualizarCarrito(Carrito carrito);
    public Optional<Carrito> obtenerCarritoPorId(Long id);
    public List<Carrito> obtenerCarritosPorClienteId(Long ClienteId);
    public void eliminarCarrito(Long id);
    List<Carrito> obtenerCarritosAbandonadosYFechaActualizacionBetween(LocalDateTime desde, LocalDateTime hasta);
    Optional<Carrito> obtenerCarritoActivoPorCliente(Long clienteId, LocalDateTime limite);


}
