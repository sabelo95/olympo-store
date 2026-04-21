package com.Notificaciones_service.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;
    
    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @Test
    void enviarCorreo_ValidData_SendsEmail() throws Exception {
        // Given
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // When
        var result = emailService.enviarCorreo("test@test.com", "Asunto", "Cuerpo del mensaje");

        // Then
        StepVerifier.create(result)
            .verifyComplete();
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void enviarConAdjunto_ValidData_SendsEmailWithAttachment() throws Exception {
        // Given
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // When
        emailService.enviarConAdjunto("test@test.com", "Asunto", "Mensaje", 
            "archivo".getBytes(), "archivo.pdf");

        // Then
        verify(mailSender).send(any(MimeMessage.class));
    }
}


