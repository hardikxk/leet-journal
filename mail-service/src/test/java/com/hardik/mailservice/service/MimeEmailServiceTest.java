package com.hardik.mailservice.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MimeEmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private MimeEmailService mimeEmailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(mimeEmailService, "senderEmail", "test@example.com");
    }

    @Test
    void sendEmail_Success() throws Exception {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mimeMessage).setFrom(anyString());
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        assertDoesNotThrow(() -> mimeEmailService.sendEmail("to@example.com", "Subject", "Text"));

        // Assert
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendEmail_Failure() throws Exception {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mimeMessage).setFrom(anyString());
        doThrow(new RuntimeException("Send failed")).when(mailSender).send(any(MimeMessage.class));

        // Act & Assert
        MailException exception = assertThrows(MailException.class,
            () -> mimeEmailService.sendEmail("to@example.com", "Subject", "Text"));
        assertTrue(exception.getMessage().contains("Failed to send email"));

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendEmail_UsesConfiguredSender() throws Exception {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mimeMessage).setFrom(anyString());
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        mimeEmailService.sendEmail("to@example.com", "Subject", "Text");

        // Assert
        verify(mimeMessage).setFrom("test@example.com");
    }
}