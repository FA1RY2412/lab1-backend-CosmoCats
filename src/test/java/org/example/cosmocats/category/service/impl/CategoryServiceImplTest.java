package org.example.cosmocats.category.service.impl;

import org.example.cosmocats.category.dto.CategoryCreateUpdateDto;
import org.example.cosmocats.category.dto.CategoryDto;
import org.example.cosmocats.category.entity.Category;
import org.example.cosmocats.category.repository.CategoryRepository;
import org.example.cosmocats.common.exception.ResourceNotFoundException;
import org.example.cosmocats.common.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryServiceImpl service;

    @Test
    void findAll_returnsMappedDtos() {
        Category c = new Category();
        c.setCode("FOOD");
        c.setTitle("Food cosmo-cats");

        when(categoryRepository.findAll()).thenReturn(List.of(c));

        List<CategoryDto> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("FOOD", result.get(0).getCode());
        assertEquals("Food cosmo-cats", result.get(0).getTitle());
        verify(categoryRepository).findAll();
    }

    @Test
    void findById_notFound_throwsResourceNotFound() {
        when(categoryRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findById(10L));
    }

    @Test
    void create_success() {
        CategoryCreateUpdateDto dto =
                new CategoryCreateUpdateDto("FOOD", "Food cosmo-cats");

        when(categoryRepository.existsByCode("FOOD")).thenReturn(false);

        Category saved = new Category();
        saved.setCode("FOOD");
        saved.setTitle("Food cosmo-cats");
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        CategoryDto result = service.create(dto);

        assertEquals("FOOD", result.getCode());
        assertEquals("Food cosmo-cats", result.getTitle());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void create_duplicateCode_throwsValidationException() {
        CategoryCreateUpdateDto dto =
                new CategoryCreateUpdateDto("FOOD", "Food cosmo-cats");

        when(categoryRepository.existsByCode("FOOD")).thenReturn(true);

        assertThrows(ValidationException.class,
                () -> service.create(dto));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void update_notFound_throwsResourceNotFound() {
        CategoryCreateUpdateDto dto =
                new CategoryCreateUpdateDto("NEW", "New title");

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.update(99L, dto));
    }

    @Test
    void delete_callsRepository() {
        service.delete(5L);
        verify(categoryRepository).deleteById(5L);
    }
}
