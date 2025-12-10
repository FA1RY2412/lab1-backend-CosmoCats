package org.example.cosmocats.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cosmocats.category.entity.Category;
import org.example.cosmocats.category.repository.CategoryRepository;
import org.example.cosmocats.config.PostgresTestConfig;
import org.example.cosmocats.product.dto.ProductCreateUpdateDto;
import org.example.cosmocats.product.entity.Product;
import org.example.cosmocats.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresTestConfig.class)
@Transactional
class ProductControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired ProductRepository repo;
    @Autowired CategoryRepository categoryRepository;

    private Category ensureCategory() {
        return categoryRepository.findByCode("DEFAULT")
                .orElseGet(() -> categoryRepository.save(new Category("DEFAULT", "Default category")));
    }

    @Test
    void list_ok() throws Exception {
        Category cat = ensureCategory();
        Product saved = repo.save(new Product("X", "d", 5.0, cat));

        mvc.perform(get("/api/v1/products"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$[0].id", is(saved.getId().intValue())));
    }

    @Test
    void create_ok() throws Exception {
        Category cat = ensureCategory();
        ProductCreateUpdateDto dto = new ProductCreateUpdateDto("X", "d", 5.0, cat.getId());

        mvc.perform(post("/api/v1/products")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(om.writeValueAsString(dto)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.name", is("X")))
           .andExpect(jsonPath("$.categoryId", is(cat.getId().intValue())));
    }

    @Test
    void delete_noContent() throws Exception {
        Category cat = ensureCategory();
        Product saved = repo.save(new Product("X", "d", 5.0, cat));

        mvc.perform(delete("/api/v1/products/{id}", saved.getId()))
           .andExpect(status().isNoContent());

        assertFalse(repo.existsById(saved.getId()));
    }
}
