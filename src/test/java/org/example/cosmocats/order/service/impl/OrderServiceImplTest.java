package org.example.cosmocats.order.service.impl;

import org.example.cosmocats.common.exception.ResourceNotFoundException;
import org.example.cosmocats.common.exception.ValidationException;
import org.example.cosmocats.order.dto.OrderCreateUpdateDto;
import org.example.cosmocats.order.dto.OrderDto;
import org.example.cosmocats.order.entity.OrderEntity;
import org.example.cosmocats.order.repository.OrderRepository;
import org.example.cosmocats.order.service.OrderService;
import org.example.cosmocats.product.entity.Product;
import org.example.cosmocats.product.repository.ProductRepository;
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
class OrderServiceImplTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    OrderServiceImpl service;

    @Test
    void create_success() {
        OrderCreateUpdateDto dto = new OrderCreateUpdateDto();
        dto.setNumber("ORD-001");
        dto.setCustomerName("Голуб Денис");
        dto.setStatus("NEW");
        dto.setProductIds(List.of(2L));

        Product p = new Product();
        p.setId(2L);

        when(productRepository.findAllById(List.of(2L))).thenReturn(List.of(p));
        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderDto result = service.create(dto);

        assertNotNull(result);
        assertEquals("ORD-001", result.getNumber());
        assertEquals("Голуб Денис", result.getCustomerName());
        assertEquals("NEW", result.getStatus());
        assertNotNull(result.getProductIds());
        assertEquals(1, result.getProductIds().size());

        verify(orderRepository).save(any(OrderEntity.class));
    }

    @Test
    void create_withoutNumber_throwsValidation() {
        OrderCreateUpdateDto dto = new OrderCreateUpdateDto();
        dto.setCustomerName("Cat");
        dto.setStatus("NEW");
        dto.setProductIds(List.of(1L));

        assertThrows(ValidationException.class, () -> service.create(dto));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void create_withoutProducts_throwsValidation() {
        OrderCreateUpdateDto dto = new OrderCreateUpdateDto();
        dto.setNumber("ORD-002");
        dto.setCustomerName("Cat");
        dto.setStatus("NEW");
        dto.setProductIds(List.of());

        assertThrows(ValidationException.class, () -> service.create(dto));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void findById_notFound_throwsResourceNotFound() {
        when(orderRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(42L));
    }

    @Test
    void delete_callsRepository() {
        assertDoesNotThrow(() -> service.delete(10L));
    }

    @Test
    void create_throwsValidation_whenNumberBlank() {
        OrderCreateUpdateDto dto = new OrderCreateUpdateDto();
        dto.setNumber("   ");
        dto.setProductIds(List.of(1L));

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> service.create(dto)
        );

        assertTrue(ex.getMessage().contains("number"));
    }

    @Test
    void create_throwsValidation_whenProductIdsEmpty() {
        OrderCreateUpdateDto dto = new OrderCreateUpdateDto();
        dto.setNumber("ORD-1");
        dto.setProductIds(List.of());

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> service.create(dto)
        );

        assertTrue(ex.getMessage().contains("productIds"));
    }

    @Test
    void findAll_returnsListOfDtos() {
        OrderEntity o1 = new OrderEntity("ORD-1", "Cat One", "NEW");
        o1.setId(1L);

        Product p = new Product();
        p.setId(10L);
        o1.setProducts(List.of(p));

        when(orderRepository.findAll()).thenReturn(List.of(o1));

        List<OrderDto> result = service.findAll();

        assertEquals(1, result.size());
        OrderDto dto = result.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("ORD-1", dto.getNumber());
        assertEquals("Cat One", dto.getCustomerName());
        assertEquals("NEW", dto.getStatus());
        assertEquals(1, dto.getProductIds().size());

        verify(orderRepository).findAll();
    }

    @Test
    void findByNumber_returnsDto() {
        OrderEntity o = new OrderEntity("ORD-777", "Space Cat", "PAID");
        o.setId(7L);

        Product p = new Product();
        p.setId(100L);
        o.setProducts(List.of(p));

        when(orderRepository.findByNumber("ORD-777"))
                .thenReturn(Optional.of(o));

        OrderDto dto = service.findByNumber("ORD-777");

        assertEquals("ORD-777", dto.getNumber());
        assertEquals("Space Cat", dto.getCustomerName());
        assertEquals("PAID", dto.getStatus());
        assertEquals(1, dto.getProductIds().size());

        verify(orderRepository).findByNumber("ORD-777");
    }

    @Test
    void findByNumber_notFound_throwsResourceNotFound() {
        when(orderRepository.findByNumber("MISSING"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findByNumber("MISSING")
        );
    }

    @Test
    void create_throwsValidation_whenSomeProductsNotFound() {
        OrderCreateUpdateDto dto = new OrderCreateUpdateDto();
        dto.setNumber("ORD-003");
        dto.setCustomerName("Cat");
        dto.setStatus("NEW");
        dto.setProductIds(List.of(1L, 2L));

        Product existing = new Product();
        existing.setId(1L);

        when(productRepository.findAllById(dto.getProductIds()))
                .thenReturn(List.of(existing));

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> service.create(dto)
        );

        assertTrue(ex.getMessage().contains("Some products not found"));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void update_successfullyUpdatesOrder() {
        Long id = 5L;

        OrderEntity existing = new OrderEntity("OLD-1", "Old Cat", "NEW");
        existing.setId(id);
        existing.setProducts(List.of());

        when(orderRepository.findById(id))
                .thenReturn(Optional.of(existing));

        OrderCreateUpdateDto dto = new OrderCreateUpdateDto();
        dto.setNumber("ORD-5");
        dto.setCustomerName("New Name");
        dto.setStatus("PAID");
        dto.setProductIds(List.of(10L));

        Product product = new Product();
        product.setId(10L);

        when(productRepository.findAllById(dto.getProductIds()))
                .thenReturn(List.of(product));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderDto result = service.update(id, dto);

        assertEquals("ORD-5", result.getNumber());
        assertEquals("New Name", result.getCustomerName());
        assertEquals("PAID", result.getStatus());
        assertEquals(1, result.getProductIds().size());

        verify(orderRepository).findById(id);
        verify(orderRepository).save(existing);
    }

    @Test
    void update_throwsValidation_whenSomeProductsNotFound() {
        Long id = 10L;

        OrderEntity existing = new OrderEntity("ORD-OLD", "Someone", "NEW");
        existing.setId(id);

        when(orderRepository.findById(id))
                .thenReturn(Optional.of(existing));

        OrderCreateUpdateDto dto = new OrderCreateUpdateDto();
        dto.setNumber("ORD-NEW");
        dto.setCustomerName("New");
        dto.setStatus("NEW");
        dto.setProductIds(List.of(1L, 2L));

        Product onlyOne = new Product();
        onlyOne.setId(1L);

        when(productRepository.findAllById(dto.getProductIds()))
                .thenReturn(List.of(onlyOne));

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> service.update(id, dto)
        );

        assertTrue(ex.getMessage().contains("Some products not found"));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void delete_whenOrderExists_deletesIt() {
        when(orderRepository.existsById(100L)).thenReturn(true);

        service.delete(100L);

        verify(orderRepository).existsById(100L);
        verify(orderRepository).deleteById(100L);
    }
}
