package com.compras_service.domain.usecase.NotificacionesUseCases;

import com.compras_service.domain.gateways.Carrito.CarritoGateway;
import com.compras_service.domain.gateways.Cliente.ClienteGateway;
import com.compras_service.domain.gateways.ProductoGateway;
import com.compras_service.domain.model.Carrito;
import com.compras_service.domain.model.Producto;
import com.compras_service.domain.services.CarritoAbandonadoService;
import com.compras_service.infrastructure.adapters.External.NotificacionService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificarCarritosAbandonadosUseCase {

    private final CarritoGateway carritoGateway;
    private final ClienteGateway clienteGateway;
    private final ProductoGateway productoGateway;
    private final NotificacionService notificacionService;
    private final CarritoAbandonadoService carritoAbandonadoService;

    private static final int HORAS_MIN_INACTIVIDAD = 1;
    private static final int HORAS_MAX_INACTIVIDAD = 2;

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional(readOnly = true)
    public void revisarCarritos() {
        notificarCarritosAbandonados();
    }

    public void notificarCarritosAbandonados() {
        List<Carrito> carritos = obtenerCarritosCandidatos();

        for (Carrito carrito : carritos) {

            if (!carritoAbandonadoService.esAbandonado(carrito)) continue;

            String email = clienteGateway.obtenerEmailPorId(carrito.getCliente_id())
                    .orElse(null);

            if (email == null) continue;

            var productos = obtenerProductos(carrito);
            if (productos.isEmpty()) continue;

            String resumen = carritoAbandonadoService.generarResumenProductos(carrito.getProductos(), productos);
            String mensaje = carritoAbandonadoService.construirMensaje(resumen);

            enviarNotificacion(email, mensaje);
        }
    }

    private List<Carrito> obtenerCarritosCandidatos() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime min = ahora.minusHours(HORAS_MAX_INACTIVIDAD);
        LocalDateTime max = ahora.minusHours(HORAS_MIN_INACTIVIDAD);

        return carritoGateway.obtenerCarritosAbandonadosYFechaActualizacionBetween(min, max);
    }

    private List<Producto> obtenerProductos(Carrito carrito) {
        List<Long> ids = carrito.getProductos().stream()
                .map(p -> p.getProducto_id())
                .toList();

        return productoGateway.obtenerProductoPorId(ids).orElse(List.of());
    }

    private void enviarNotificacion(String correo, String mensaje) {
        notificacionService.enviarNotificacion(
                "Notificación de Carrito Abandonado",
                correo,
                mensaje
        );
    }
}
