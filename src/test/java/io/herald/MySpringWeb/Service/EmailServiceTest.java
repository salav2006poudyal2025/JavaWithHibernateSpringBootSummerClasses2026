package io.herald.MySpringWeb.Service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class EmailServiceTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final EmailService emailService = new EmailService(mailSender, "sender@example.com");

    @Test
    void sendsPersonalizedRegistrationEmail() {
        emailService.sendRegistrationEmail("recipient@example.com", "Alice");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage message = messageCaptor.getValue();
        assertEquals("sender@example.com", message.getFrom());
        assertEquals("recipient@example.com", message.getTo()[0]);
        assertEquals("Welcome to MySpringWeb", message.getSubject());
        assertTrue(message.getText().contains("Hello Alice"));
    }

    @Test
    void skipsBlankRecipientAddress() {
        emailService.sendRegistrationEmail("  ", "Alice");

        verifyNoInteractions(mailSender);
    }
}
