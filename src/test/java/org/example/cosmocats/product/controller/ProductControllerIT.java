package org.example.cosmocats.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cosmocats.common.exception.GlobalExceptionHandler;
import org.example.cosmocats.product.dto.ProductCreateUpdateDto;
import org.example.cosmocats.product.dto.ProductDto;
import org.example.cosmocats.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerIT {

    private MockMvc mvc;
    private ObjectMapper om;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);

        
        var controller = new ProductController(productService);

        
        var validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        om = new ObjectMapper();
    }

    @Test
    void list_ok() throws Exception {
        when(productService.findAll()).thenReturn(List.of(new ProductDto(1L,"X","d",5.0)));

        mvc.perform(get("/api/v1/products"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void create_2xx() throws Exception {
        var dto = new ProductCreateUpdateDto("X","d",5.0);
        when(productService.create(any())).thenReturn(new ProductDto(10L,"X","d",5.0));

        mvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(dto)))
           .andExpect(status().is2xxSuccessful())
           .andExpect(jsonPath("$.name", is("X")));
    }

    @Test
    void delete_noContent() throws Exception {
        mvc.perform(delete("/api/v1/products/{id}", 1L))
           .andExpect(status().isNoContent());
        verify(productService).delete(1L);
    }
}
