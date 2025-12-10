package org.example.cosmocats.category.controller;

import org.example.cosmocats.category.dto.CategoryCreateUpdateDto;
import org.example.cosmocats.category.dto.CategoryDto;
import org.example.cosmocats.category.service.CategoryService;
import org.example.cosmocats.common.exception.ResourceNotFoundException;
import org.example.cosmocats.security.SecurityTestConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;



import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;


@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(SecurityTestConfig.class)
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryService categoryService;

    @Test
    void list_returnsOk() throws Exception {
        CategoryDto dto = new CategoryDto(1L, "fd1", "Food cosmo-cats");

        Mockito.when(categoryService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/categories"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].code").value("fd1"));
    }

    @Test
    void create_returns201() throws Exception {
        CategoryDto dto = new CategoryDto(1L, "fd1", "Food cosmo-cats");

        Mockito.when(categoryService.create(any(CategoryCreateUpdateDto.class)))
               .thenReturn(dto);

        String body = """
                {
                  "code": "fd1",
                  "title": "Food cosmo-cats"
                }
                """;

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.code").value("fd1"));
    }

       @Test
       void getById_notFound_returns404() throws Exception {
       Mockito.when(categoryService.findById(42L))
              .thenThrow(new ResourceNotFoundException("Category with id 42 not found"));

       mockMvc.perform(get("/api/v1/categories/42"))
              .andExpect(status().isNotFound());
       }

}
