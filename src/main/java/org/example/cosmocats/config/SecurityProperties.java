package org.example.cosmocats.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {

    private String apiKeyHeader = "X-API-KEY";
    private String apiKey = "cosmo-cats-api-key";

    private Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Jwt {
        private String secret = "cosmo-cats-jwt-secret";
        private String jwsAlgorithm = "HS256";
        private String rolesClaim = "roles";
    }
}
