package io.herald.MySpringWeb.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service handling out-bound email transmissions via SMTP.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Composes and sends a welcome registration email to a newly signed-up user.
     * @param toEmail The recipient's email address.
     * @param username The recipient's username for personalization.
     */
    public void sendRegistrationEmail(String toEmail, String username) {
        // Validate destination email string
        if (toEmail != null && !toEmail.isEmpty()) {
            SimpleMailMessage message = new SimpleMailMessage();
            
            // Should match the SMTP authenticated user in properties
            message.setFrom("np03cs4a240047@heraldcollege.edu.np");
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
