package com.compras_service.infrastructure.adapters.DTOs;

public class CrearPedidoRequest {
    private String direccionEnvio;
    private  String metodoPago;

    // Constructores
    public CrearPedidoRequest() {}

    public CrearPedidoRequest(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    // Getters y Setters
    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }


}
