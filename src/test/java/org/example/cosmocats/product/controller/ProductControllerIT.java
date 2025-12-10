package org.example.cosmocats.product.controller;

import org.example.cosmocats.config.SecurityProperties;
import org.example.cosmocats.product.dto.ProductCreateUpdateDto;
import org.example.cosmocats.product.dto.ProductDto;
import org.example.cosmocats.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    
    @MockBean
    SecurityProperties securityProperties;

   
    @Test
    void list_returnsOk() throws Exception {
        ProductDto dto = new ProductDto(
                1L,
                "X",
                "desc",
                5.0,
                10L,
                "CAT_CODE"
        );

        Mockito.when(productService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/products"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].name").value("X"))
               .andExpect(jsonPath("$[0].price").value(5.0));
    }

    @Test
    void create_returns201() throws Exception {
        ProductDto saved = new ProductDto(
                1L,
                "X",
                "desc",
                5.0,
                10L,
                "CAT_CODE"
        );

        Mockito.when(productService.create(any(ProductCreateUpdateDto.class)))
               .thenReturn(saved);

        String body = """
                {
                  "name": "X",
                  "description": "desc",
                  "price": 5.0,
                  "categoryId": 10
                }
                """;

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("X"))
               .andExpect(jsonPath("$.price").value(5.0));
    }

    @Test
    void delete_noContent() throws Exception {
        long id = 1L;
        doNothing().when(productService).delete(id);

        mockMvc.perform(delete("/api/v1/products/{id}", id))
               .andExpect(status().isNoContent());
    }
}
