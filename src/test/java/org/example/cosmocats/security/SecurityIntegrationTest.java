package org.example.cosmocats.security;

import org.example.cosmocats.category.controller.CategoryController;
import org.example.cosmocats.category.dto.CategoryDto;
import org.example.cosmocats.category.service.CategoryService;
import org.example.cosmocats.config.SecurityConfig;
import org.example.cosmocats.config.SecurityProperties;
import org.example.cosmocats.security.ApiKeyAuthenticationFilter;
import org.example.cosmocats.security.RestAccessDeniedHandler;
import org.example.cosmocats.security.RestAuthenticationEntryPoint;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@Import({
        SecurityConfig.class,
        SecurityProperties.class,
        ApiKeyAuthenticationFilter.class,
        RestAuthenticationEntryPoint.class,
        RestAccessDeniedHandler.class
})
@TestPropertySource(properties = {
        "security.api-key=cosmo-cats-api-key",
        "security.api-key-header=X-API-KEY",
        "security.jwt.secret=disabled",
        "security.jwt.jws-algorithm=HS256",
        "security.jwt.roles-claim=roles"
})
class SecurityIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SecurityProperties securityProperties;

    @MockBean
    CategoryService categoryService;

    @Test
    void whenNoApiKey_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/categories"))
               .andExpect(status().isUnauthorized())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.message").value("API key header is missing"));
    }

    @Test
    void whenValidApiKey_thenRequestIsSuccessful() throws Exception {
        CategoryDto dto = new CategoryDto(1L, "test", "Test Category");
        Mockito.when(categoryService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/categories")
                        .header(securityProperties.getApiKeyHeader(),
                                securityProperties.getApiKey()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].code").value("test"))
               .andExpect(jsonPath("$[0].title").value("Test Category"));
    }
}
