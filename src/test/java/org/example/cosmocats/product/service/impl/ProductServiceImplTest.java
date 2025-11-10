package org.example.cosmocats.product.service.impl;

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

    @InjectMocks
    ProductServiceImpl service;

    @Test
    void findAll_returnsList() {
        Product p = new Product("A", "d", 10.0);
        p.setId(1L);
        lenient().when(repo.findAll()).thenReturn(List.of(p));

        assertEquals(1, service.findAll().size());
        verify(repo).findAll();
    }

    @Test
    void findById_ok() {
        Product p = new Product("A", "d", 10.0);
        p.setId(1L);
        lenient().when(repo.findById(1L)).thenReturn(Optional.of(p));

        assertEquals("A", service.findById(1L).getName());
        verify(repo).findById(1L);
    }

    @Test
    void findById_notFound() {
        lenient().when(repo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void create_ok_validatesAndSaves() {
        ProductCreateUpdateDto dto = new ProductCreateUpdateDto("X", "desc", 5.0);
        lenient().when(repo.save(any(Product.class))).thenAnswer(i -> {
            Product saved = i.getArgument(0);
            saved.setId(42L);
            return saved;
        });

        var res = service.create(dto);
        assertEquals("X", res.getName());

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repo).save(captor.capture());
        assertEquals(5.0, captor.getValue().getPrice());
    }

    @Test
    void create_throws_onBlankName() {
        ProductCreateUpdateDto dto = new ProductCreateUpdateDto("  ", "d", 1.0);
        assertThrows(ValidationException.class, () -> service.create(dto));
        verify(repo, never()).save(any());
    }

    @Test
    void create_throws_onNullPrice() {
        ProductCreateUpdateDto dto = new ProductCreateUpdateDto("A", "d", null);
        assertThrows(ValidationException.class, () -> service.create(dto));
        verify(repo, never()).save(any());
    }

    @Test
    void create_throws_onNonPositivePrice() {
        ProductCreateUpdateDto dto = new ProductCreateUpdateDto("A", "d", 0.0);
        assertThrows(ValidationException.class, () -> service.create(dto));
        verify(repo, never()).save(any());
    }

    @Test
    void update_ok() {
        Product existing = new Product("A", "d", 10.0);
        existing.setId(1L);
        lenient().when(repo.findById(1L)).thenReturn(Optional.of(existing));
        lenient().when(repo.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        var res = service.update(1L, new ProductCreateUpdateDto("B", "z", 12.0));
        assertEquals("B", res.getName());
        verify(repo).save(any(Product.class));
    }

    @Test
    void update_notFound() {
        lenient().when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.update(1L, new ProductCreateUpdateDto("B", "z", 12.0)));
        verify(repo, never()).save(any());
    }

    @Test
    void update_throws_onInvalidDto() {
        
        assertAll(
            () -> assertThrows(ValidationException.class,
                    () -> service.update(1L, new ProductCreateUpdateDto("  ", "z", 12.0))),
            () -> assertThrows(ValidationException.class,
                    () -> service.update(1L, new ProductCreateUpdateDto("B", "z", null))),
            () -> assertThrows(ValidationException.class,
                    () -> service.update(1L, new ProductCreateUpdateDto("B", "z", 0.0)))
        );
        verifyNoInteractions(repo);
    }

    @Test
    void delete_existing_deletesById() {
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
