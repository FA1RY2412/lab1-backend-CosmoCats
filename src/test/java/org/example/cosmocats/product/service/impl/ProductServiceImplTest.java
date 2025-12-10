package org.example.cosmocats.product.service.impl;

import org.example.cosmocats.category.entity.Category;
import org.example.cosmocats.category.repository.CategoryRepository;
import org.example.cosmocats.common.exception.ResourceNotFoundException;
import org.example.cosmocats.common.exception.ValidationException;
import org.example.cosmocats.product.dto.ProductCreateUpdateDto;
import org.example.cosmocats.product.entity.Product;
import org.example.cosmocats.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceImplTest {

    @Mock
    ProductRepository repo;

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    ProductServiceImpl service;

    private Category defaultCategory() {
        Category c = new Category("C1", "Cat 1");
        c.setId(1L);
        return c;
    }

    @Test
    void findAll_returnsList() {
        Product p = new Product("A", "d", 10.0, defaultCategory());
        p.setId(1L);
        lenient().when(repo.findAll()).thenReturn(List.of(p));

        assertEquals(1, service.findAll().size());
        verify(repo).findAll();
    }

    @Test
    void findById_ok() {
        Product p = new Product("A", "d", 10.0, defaultCategory());
        p.setId(1L);
        lenient().when(repo.findById(1L)).thenReturn(Optional.of(p));

        assertEquals("A", service.findById(1L).getName());
        verify(repo).findById(1L);
    }

    @Test
    void findById_notFound_throws() {
        lenient().when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
        verify(repo).findById(99L);
    }

    @Test
    void create_ok_validatesAndSaves() {
        Category cat = defaultCategory();
        lenient().when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        lenient().when(repo.save(any(Product.class))).thenAnswer(i -> {
            Product saved = i.getArgument(0);
            saved.setId(42L);
            return saved;
        });

        ProductCreateUpdateDto dto = new ProductCreateUpdateDto("X", "desc", 5.0, 1L);

        var res = service.create(dto);
        assertEquals("X", res.getName());

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repo).save(captor.capture());
        assertEquals(5.0, captor.getValue().getPrice());
        assertEquals(cat, captor.getValue().getCategory());
    }

    @Test
    void create_throws_onBlankName() {
        ProductCreateUpdateDto dto = new ProductCreateUpdateDto("  ", "d", 1.0, 1L);
        assertThrows(ValidationException.class, () -> service.create(dto));
        verify(repo, never()).save(any());
    }

    @Test
    void create_throws_onNullPrice() {
        ProductCreateUpdateDto dto = new ProductCreateUpdateDto("A", "d", null, 1L);
        assertThrows(ValidationException.class, () -> service.create(dto));
        verify(repo, never()).save(any());
    }

    @Test
    void update_ok_updatesAndSaves() {
        Category cat = defaultCategory();
        Product existing = new Product("Old", "d", 1.0, cat);
        existing.setId(5L);

        lenient().when(repo.findById(5L)).thenReturn(Optional.of(existing));
        lenient().when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        lenient().when(repo.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ProductCreateUpdateDto dto = new ProductCreateUpdateDto("New", "desc", 2.0, 1L);

        var res = service.update(5L, dto);
        assertEquals("New", res.getName());
        verify(repo).save(existing);
    }

    @Test
    void update_notFound_throws() {
        ProductCreateUpdateDto dto = new ProductCreateUpdateDto("X", "d", 1.0, 1L);
        lenient().when(repo.findById(77L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(77L, dto));
        verify(repo).findById(77L);
    }

    @Test
    void delete_existing_deletes() {
        lenient().when(repo.existsById(7L)).thenReturn(true);

        service.delete(7L);

        verify(repo).existsById(7L);
        verify(repo).deleteById(7L);
    }

    @Test
    void delete_missing_noop() {
        lenient().when(repo.existsById(8L)).thenReturn(false);

        service.delete(8L);

        verify(repo).existsById(8L);
        verify(repo, never()).deleteById(anyLong());
    }
}
