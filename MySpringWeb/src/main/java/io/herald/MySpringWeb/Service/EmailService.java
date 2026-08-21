package io.herald.MySpringWeb.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(String toEmail, String username) {
        if (toEmail != null && !toEmail.isEmpty()) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("np03cs4a240047@heraldcollege.edu.np");
            message.setTo(toEmail);
            message.setSubject("Welcome to MySpringWeb");
            message.setText("Hello " + username + ",\n\nYou have successfully registered!\n\nBest Regards,\nMySpringWeb Team");
            
            try {
                mailSender.send(message);
                System.out.println("Email sent successfully to: " + toEmail);
            } catch (Exception e) {
                System.err.println("Error sending email to " + toEmail + ": " + e.getMessage());
            }
        }
    }
}
