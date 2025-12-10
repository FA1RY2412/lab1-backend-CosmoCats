package org.example.cosmocats.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "CosmoCats API",
        version = "v1",
        description = "Lab1 API specification"
    )
)
public class OpenApiConfig { }
