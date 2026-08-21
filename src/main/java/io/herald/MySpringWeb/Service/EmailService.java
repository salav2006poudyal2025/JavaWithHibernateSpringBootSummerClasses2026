package io.herald.MySpringWeb.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service handling out-bound email transmissions via SMTP.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Composes and sends a welcome registration email to a newly signed-up user.
     * @param toEmail The recipient's email address.
     * @param username The recipient's username for personalization.
     */
    @Async
    public void sendRegistrationEmail(String toEmail, String username) {
        // Validate destination email string
        if (toEmail != null && !toEmail.isEmpty()) {
            SimpleMailMessage message = new SimpleMailMessage();
            
            // Use the authenticated SMTP account configured by MAIL_USERNAME.
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to MySpringWeb");
            
            // Build simple plain text message
            message.setText("Hello " + username + ",\n\nYou have successfully registered!\n\nBest Regards,\nMySpringWeb Team");
            
            try {
                // Dispatch the email synchronously
                mailSender.send(message);
                System.out.println("Email sent successfully to: " + toEmail);
            } catch (Exception e) {
                // Log delivery failures instead of breaking the transaction
                System.err.println("Error sending email to " + toEmail + ": " + e.getMessage());
            }
        }
    }
}
