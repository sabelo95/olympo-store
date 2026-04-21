package com.Notificaciones_service.Controlador;

import com.Notificaciones_service.Modelo.Notificacion;
import com.Notificaciones_service.Request.NotificacionRequest;
import com.Notificaciones_service.Service.NotificacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebFluxTest(NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private NotificacionService notificacionService;

    @Test
    void enviarNotificacion_ValidRequest_ReturnsSuccess() {
        // Given
        NotificacionRequest request = new NotificacionRequest();
        request.setTipo("EMAIL");
        request.setDestinatario("test@test.com");
        request.setMensaje("Mensaje de prueba");

        Notificacion notificacion = new Notificacion("1", "EMAIL", "test@test.com", "Mensaje de prueba", LocalDateTime.now());
        
        when(notificacionService.enviarNotificacion(anyString(), anyString(), anyString()))
            .thenReturn(reactor.core.publisher.Mono.just(notificacion));

        // When & Then
        webTestClient.post()
            .uri("/notificaciones")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void enviarNotificacion_NullDestinatario_ReturnsError() {
        // Given
        NotificacionRequest request = new NotificacionRequest();
        request.setTipo("EMAIL");
        request.setDestinatario(null);
        request.setMensaje("Mensaje de prueba");

        // When & Then
        webTestClient.post()
            .uri("/notificaciones")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().is5xxServerError();
    }
}




