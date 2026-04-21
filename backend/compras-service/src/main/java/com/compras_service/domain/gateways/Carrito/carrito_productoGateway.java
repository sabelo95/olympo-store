package com.compras_service.domain.gateways.Carrito;

public interface carrito_productoGateway {
    public void agregarProductoAlCarrito(Long carritoId, Long productoId,Long MarcaId, Double cantidad);
    public void eliminarProductoDelCarrito(Long carritoId, Long productoId);
    public void actualizarCantidadProductoEnCarrito(Long carritoId, Long productoId, Double cantidad);

}
