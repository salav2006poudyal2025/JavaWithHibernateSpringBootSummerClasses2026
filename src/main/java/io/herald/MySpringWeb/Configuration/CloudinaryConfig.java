package io.herald.MySpringWeb.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for integrating Cloudinary for image storage.
 * It reads credentials from application.properties (or environment variables)
 * and exposes a Cloudinary bean to the Spring context.
 */
@Configuration
public class CloudinaryConfig {

    // The cloud name associated with the Cloudinary account
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    // The API key for Cloudinary authentication
    @Value("${cloudinary.api-key}")
    private String apiKey;

    // The API secret for Cloudinary authentication
    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    /**
     * Creates and configures the Cloudinary instance.
     * @return Cloudinary object initialized with account credentials.
     */
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true // Ensures URLs use HTTPS
        ));
    }

}
