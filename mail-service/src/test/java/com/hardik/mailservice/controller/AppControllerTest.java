package com.hardik.mailservice.controller;

import com.hardik.mailservice.service.MimeEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AppControllerTest {

    @Mock
    private MimeEmailService mimeEmailService;

    @InjectMocks
    private AppController appController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void hello_ReturnsHello() {
        // Act
        String result = appController.hello();

        // Assert
        assertEquals("hello", result);
    }

    @Test
    void mime_SuccessfulSend_ReturnsOk() throws MailException {
        // Arrange
        doNothing().when(mimeEmailService).sendEmail(anyString(), anyString(), anyString());

        // Act
        ResponseEntity<String> response = appController.mime("to@example.com", "Subject", "Text");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mail sent successfully using MIME!", response.getBody());
        verify(mimeEmailService).sendEmail("to@example.com", "Subject", "Text");
    }

    @Test
    void mime_FailedSend_ReturnsInternalServerError() throws MailException {
        // Arrange
        doThrow(new MailException("Send failed") {}).when(mimeEmailService).sendEmail(anyString(), anyString(), anyString());

        // Act
        ResponseEntity<String> response = appController.mime("to@example.com", "Subject", "Text");

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Failed to send email: Send failed"));
        verify(mimeEmailService).sendEmail("to@example.com", "Subject", "Text");
    }
}