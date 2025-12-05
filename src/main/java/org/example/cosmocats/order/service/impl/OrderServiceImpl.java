package org.example.cosmocats.order.service.impl;

import org.example.cosmocats.common.exception.ValidationException;
import org.example.cosmocats.order.dto.OrderCreateUpdateDto;
import org.example.cosmocats.order.dto.OrderDto;
import org.example.cosmocats.order.entity.OrderEntity;
import org.example.cosmocats.order.exception.OrderNotFoundException;
import org.example.cosmocats.order.exception.ProductsForOrderNotFoundException;
import org.example.cosmocats.order.repository.OrderRepository;
import org.example.cosmocats.product.entity.Product;
import org.example.cosmocats.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("Cat Food");
        product1.setPrice(new BigDecimal("10.00"));

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Space Milk");
        product2.setPrice(new BigDecimal("20.00"));
    }

    @Test
    void findById_ok() {
        OrderEntity entity = new OrderEntity("ORD-1", "Cat One", "NEW");
        entity.setId(1L);
        entity.setCreatedAt(Instant.now());
        entity.setProducts(List.of(product1, product2));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(entity));

        OrderDto result = orderService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.number()).isEqualTo("ORD-1");
        assertThat(result.customerName()).isEqualTo("Cat One");
        assertThat(result.productIds()).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void findById_notFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.findById(999L))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void create_ok() {
        OrderCreateUpdateDto dto = new OrderCreateUpdateDto(
                "ORD-2",
                "New Cat",
                "NEW",
                List.of(1L, 2L)
        );

        when(productRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(product1, product2));

        OrderEntity saved = new OrderEntity("ORD-2", "New Cat", "NEW");
        saved.setId(10L);
        saved.setCreatedAt(Instant.now());
        saved.setProducts(List.of(product1, product2));

        when(orderRepository.save(any(OrderEntity.class))).thenReturn(saved);

        OrderDto result = orderService.create(dto);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.number()).isEqualTo("ORD-2");
        assertThat(result.productIds()).containsExactlyInAnyOrder(1L, 2L);

        ArgumentCaptor<OrderEntity> captor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderRepository).save(captor.capture());
        OrderEntity toSave = captor.getValue();
        assertThat(toSave.getNumber()).isEqualTo("ORD-2");
        assertThat(toSave.getCustomerName()).isEqualTo("New Cat");
    }

    @Test
    void create_productsNotFound_throwsException() {
        OrderCreateUpdateDto dto = new OrderCreateUpdateDto(
                "ORD-3",
                "Cat",
                "NEW",
                List.of(1L, 2L)
        );

        
        when(productRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(product1));

        assertThatThrownBy(() -> orderService.create(dto))
                .isInstanceOf(ProductsForOrderNotFoundException.class);
    }

    @Test
    void create_missingNumber_validationError() {
        OrderCreateUpdateDto dto = new OrderCreateUpdateDto(
                "   ",          
                "Cat",
                "NEW",
                List.of(1L)
        );

        assertThatThrownBy(() -> orderService.create(dto))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void update_notFound() {
        OrderCreateUpdateDto dto = new OrderCreateUpdateDto(
                "ORD-9",
                "Someone",
                "NEW",
                List.of(1L)
        );

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.update(999L, dto))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void delete_ok() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        orderService.delete(1L);

        verify(orderRepository).deleteById(1L);
    }
}
