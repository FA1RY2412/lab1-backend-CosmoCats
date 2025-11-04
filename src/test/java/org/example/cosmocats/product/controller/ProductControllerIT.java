package org.example.cosmocats.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cosmocats.product.dto.ProductCreateUpdateDto;
import org.example.cosmocats.product.dto.ProductDto;
import org.example.cosmocats.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIT {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;
    
    //@MockBean
    //ProductService productService;

    @Test
    void list_ok() throws Exception {
        given(productService.findAll())
                .willReturn(List.of(new ProductDto(1L, "X", "d", 5.0)));

        mvc.perform(get("/api/v1/products"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void create_returns201_andBody() throws Exception {
        var payload = new ProductCreateUpdateDto("X", "d", 5.0);
        given(productService.create(any()))
                .willReturn(new ProductDto(10L, "X", "d", 5.0));

        mvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(payload)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.name", is("X")));
    }

    @Test
    void delete_noContent() throws Exception {
        mvc.perform(delete("/api/v1/products/{id}", 1L))
           .andExpect(status().isNoContent());
        verify(productService).delete(1L);
    }
}
