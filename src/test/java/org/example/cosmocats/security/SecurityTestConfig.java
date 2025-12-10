package org.example.cosmocats.security;

import org.example.cosmocats.config.SecurityProperties;
import org.example.cosmocats.config.SecurityProperties.Jwt;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SecurityTestConfig {

    @Bean
    public SecurityProperties securityProperties() {
        SecurityProperties props = new SecurityProperties();
        props.setApiKey("cosmo-cats-api-key");
        props.setApiKeyHeader("X-API-KEY");

        Jwt jwt = new Jwt();
        jwt.setSecret("cosmo-cats-jwt-secret");
        jwt.setJwsAlgorithm("HS256");
        jwt.setRolesClaim("roles");

        props.setJwt(jwt);

        return props;
    }
}
