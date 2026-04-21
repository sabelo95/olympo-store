package com.Notificaciones_service.Controlador;

import com.Notificaciones_service.Request.NotificacionRequest;
import com.Notificaciones_service.Service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Endpoints para enviar notificaciones por correo electrónico con soporte para archivos adjuntos")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @Operation(
            summary = "Verificar estado del servicio de notificaciones",
            description = "Devuelve un mensaje confirmando que el servicio de notificaciones está activo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio activo",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(example = "Servicio de Notificaciones está activo")))
    })
    @GetMapping("/health")
    public Mono<ResponseEntity<String>> healthCheck() {
        return Mono.just(ResponseEntity.ok("Servicio de Notificaciones está activo"));
    }


    @Operation(
            summary = "Enviar notificación simple",
            description = "Envía una notificación por correo electrónico sin archivos adjuntos. " +
                    "El servicio guarda la notificación en MongoDB y envía el correo de forma asíncrona."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación enviada exitosamente",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(example = "Notificación enviada correctamente a usuario@example.com"))),
            @ApiResponse(responseCode = "500", description = "Error al enviar la notificación")
    })
    @PostMapping
    public Mono<String> enviarNotificacion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la notificación a enviar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NotificacionRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody NotificacionRequest request) {
        if (request.getDestinatario() == null || request.getDestinatario().isBlank()) {
            return Mono.error(new IllegalArgumentException("El destinatario no puede ser nulo o vacío"));
        }

        return notificacionService.enviarNotificacion(
                        request.getTipo(),
                        request.getDestinatario(),
                        request.getMensaje()
                )
                .then(Mono.just("Notificación enviada correctamente a " + request.getDestinatario()))
                .onErrorResume(e -> Mono.just("Error al enviar notificación: " + e.getMessage()));
    }

    @Operation(
            summary = "Enviar notificación con archivo adjunto",
            description = "Envía una notificación por correo electrónico con soporte para archivos adjuntos. " +
                    "El archivo es opcional. Tamaño máximo de archivo: 20MB."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo enviado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"body\": \"Correo enviado con adjunto: documento.pdf\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (destinatario nulo o vacío)"),
            @ApiResponse(responseCode = "500", description = "Error al enviar el correo")
    })
    @PostMapping(path = "/con-adjunto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> enviarNotificacionConAdjunto(
            @Parameter(description = "Tipo de notificación", example = "EMAIL", required = true)
            @RequestPart("tipo") String tipo,
            @Parameter(description = "Correo electrónico del destinatario", example = "usuario@example.com", required = true)
            @RequestPart("destinatario") String destinatario,
            @Parameter(description = "Mensaje de la notificación", example = "Mensaje de prueba", required = true)
            @RequestPart("mensaje") String mensaje,
            @Parameter(description = "Archivo adjunto (opcional)", required = false)
            @RequestPart(value = "archivo", required = false) FilePart archivo) {

        if (destinatario == null || destinatario.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().body("El destinatario no puede ser nulo o vacío"));
        }

        if (archivo != null) {
            return archivo.content()
                    .reduce(new byte[0], (acum, buffer) -> {
                        byte[] bytes = new byte[buffer.readableByteCount()];
                        buffer.read(bytes);
                        byte[] combined = new byte[acum.length + bytes.length];
                        System.arraycopy(acum, 0, combined, 0, acum.length);
                        System.arraycopy(bytes, 0, combined, acum.length, bytes.length);
                        return combined;
                    })
                    .flatMap(contenido ->
                            notificacionService.enviarAdjunto(tipo, destinatario, mensaje, contenido, archivo.filename())
                                    .thenReturn(ResponseEntity.ok("Correo enviado con adjunto: " + archivo.filename()))
                    )
                    .onErrorResume(e ->
                            Mono.just(ResponseEntity.internalServerError()
                                    .body("Error enviando correo: " + e.getMessage())));
        }

        // Si no hay archivo adjunto
        return notificacionService.enviarAdjunto(tipo, destinatario, mensaje, null, null)
                .thenReturn(ResponseEntity.ok("Correo enviado sin adjunto"))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.internalServerError()
                                .body("Error enviando correo: " + e.getMessage())));
    }
}
