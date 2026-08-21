package io.herald.MySpringWeb.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service handling out-bound email transmissions via SMTP.
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    /**
     * Composes and sends a welcome registration email to a newly signed-up user.
     * @param toEmail The recipient's email address.
     * @param username The recipient's username for personalization.
     */
    @Async
    public void sendRegistrationEmail(String toEmail, String username) {
        if (toEmail == null || toEmail.isBlank()) {
            logger.warn("Registration email was skipped because the recipient address is blank.");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome to MySpringWeb");
        message.setText("Hello " + username + ",\n\nYou have successfully registered!\n\nBest Regards,\nMySpringWeb Team");

        try {
            mailSender.send(message);
            logger.info("Registration email sent successfully to {}.", toEmail);
        } catch (Exception e) {
            // Registration has already completed; retain the failure in logs for investigation.
            logger.error("Unable to send registration email to {}: {}", toEmail, e.getMessage(), e);
        }
    }
}
