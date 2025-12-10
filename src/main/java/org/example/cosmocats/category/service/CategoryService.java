package org.example.cosmocats.category.service;

import org.example.cosmocats.category.dto.CategoryCreateUpdateDto;
import org.example.cosmocats.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryCreateUpdateDto dto);
    List<CategoryDto> findAll();
    CategoryDto findById(Long id);
    CategoryDto update(Long id, CategoryCreateUpdateDto dto);
    void delete(Long id);
}
