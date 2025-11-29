package org.example.cosmocats.category.service.impl;

import org.example.cosmocats.category.dto.CategoryCreateUpdateDto;
import org.example.cosmocats.category.dto.CategoryDto;
import org.example.cosmocats.category.entity.Category;
import org.example.cosmocats.category.repository.CategoryRepository;
import org.example.cosmocats.common.exception.ResourceNotFoundException;
import org.example.cosmocats.common.exception.ValidationException;
import org.example.cosmocats.category.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto create(CategoryCreateUpdateDto dto) {
        if (categoryRepository.existsByCode(dto.getCode())) {
            throw new ValidationException("Category with code '%s' already exists".formatted(dto.getCode()));
        }

        Category entity = new Category(dto.getCode(), dto.getTitle());
        Category saved = categoryRepository.save(entity);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category %d not found".formatted(id)));
        return toDto(category);
    }

    @Override
    public CategoryDto update(Long id, CategoryCreateUpdateDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category %d not found".formatted(id)));

        category.setCode(dto.getCode());
        category.setTitle(dto.getTitle());
        Category saved = categoryRepository.save(category);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDto toDto(Category entity) {
        return new CategoryDto(
                entity.getId(),
                entity.getCode(),
                entity.getTitle()
        );
    }
}
