package com.Notificaciones_service.Service;

import com.Notificaciones_service.Modelo.Notificacion;
import com.Notificaciones_service.Repositorio.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        notificacion = new Notificacion("1", "EMAIL", "test@test.com", "Mensaje de prueba", LocalDateTime.now());
    }

    @Test
    void enviarNotificacion_ValidRequest_SendsNotification() {
        // Given
        when(emailService.enviarCorreo(anyString(), anyString(), anyString()))
            .thenReturn(Mono.empty());
        when(repository.save(any(Notificacion.class)))
            .thenReturn(Mono.just(notificacion));

        // When
        Mono<Notificacion> result = notificacionService.enviarNotificacion(
            "EMAIL", "test@test.com", "Mensaje de prueba"
        );

        // Then
        StepVerifier.create(result)
            .expectNextMatches(n -> "test@test.com".equals(n.getDestinatario()))
            .verifyComplete();
    }

    @Test
    void listarNotificaciones_ReturnsAllNotifications() {
        // Given
        Notificacion notif1 = new Notificacion("1", "EMAIL", "test1@test.com", "Mensaje 1", LocalDateTime.now());
        Notificacion notif2 = new Notificacion("2", "SMS", "test2@test.com", "Mensaje 2", LocalDateTime.now());
        
        when(repository.findAll()).thenReturn(Flux.just(notif1, notif2));

        // When
        Flux<Notificacion> result = notificacionService.listarNotificaciones();

        // Then
        StepVerifier.create(result)
            .expectNextMatches(n -> "test1@test.com".equals(n.getDestinatario()))
            .expectNextMatches(n -> "test2@test.com".equals(n.getDestinatario()))
            .verifyComplete();
    }
}


