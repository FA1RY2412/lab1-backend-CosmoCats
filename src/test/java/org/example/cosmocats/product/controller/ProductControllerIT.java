package org.example.cosmocats.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cosmocats.product.dto.ProductCreateUpdateDto;
import org.example.cosmocats.product.entity.Product;
import org.example.cosmocats.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase 
@Transactional              
class ProductControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired ProductRepository repo;

    @Test
    void list_ok() throws Exception {
        
        Product saved = repo.save(new Product("X", "d", 5.0));

        mvc.perform(get("/api/v1/products"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$[0].id", is(saved.getId().intValue())));
    }

    @Test
    void create_returns201_andBody() throws Exception {
        var payload = new ProductCreateUpdateDto("X", "d", 5.0);

        mvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(payload)))
           
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.name", is("X")));
    }

    @Test
    void delete_noContent() throws Exception {
        Product saved = repo.save(new Product("X", "d", 5.0));

        mvc.perform(delete("/api/v1/products/{id}", saved.getId()))
           .andExpect(status().isNoContent());

        assertFalse(repo.existsById(saved.getId()));
    }
}
