package org.example.cosmocats.order.service.impl;

import org.example.cosmocats.common.exception.ResourceNotFoundException;
import org.example.cosmocats.common.exception.ValidationException;
import org.example.cosmocats.order.dto.OrderCreateUpdateDto;
import org.example.cosmocats.order.dto.OrderDto;
import org.example.cosmocats.order.domain.Order;
import org.example.cosmocats.order.entity.OrderEntity;
import org.example.cosmocats.order.repository.OrderRepository;
import org.example.cosmocats.order.service.OrderService;
import org.example.cosmocats.product.entity.Product;
import org.example.cosmocats.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    

    private Order toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        Order domain = new Order();
        domain.setId(entity.getId());
        domain.setNumber(entity.getNumber());
        domain.setCustomerName(entity.getCustomerName());
        domain.setStatus(entity.getStatus());
        domain.setCreatedAt(entity.getCreatedAt());

        List<Long> productIds = entity.getProducts().stream()
                .map(Product::getId)
                .toList();
        domain.setProductIds(productIds);

        return domain;
    }

    

    private OrderEntity toEntity(Order domain, List<Product> products) {
        if (domain == null) {
            return null;
        }

        OrderEntity entity = new OrderEntity();
        entity.setId(domain.getId());
        entity.setNumber(domain.getNumber());
        entity.setCustomerName(domain.getCustomerName());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setProducts(products);

        return entity;
    }

    

    private OrderDto toDto(Order o) {
        return new OrderDto(
                o.getId(),
                o.getNumber(),
                o.getCustomerName(),
                o.getStatus(),
                o.getCreatedAt(),
                o.getProductIds()
        );
    }

   

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream()
                .map(this::toDomain)
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order id=%d not found".formatted(id)
                ));

        return toDto(toDomain(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findByNumber(String number) {
        OrderEntity entity = orderRepository.findByNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order number=%s not found".formatted(number)
                ));

        return toDto(toDomain(entity));
    }

    @Override
    public OrderDto create(OrderCreateUpdateDto dto) {
        validate(dto);

        Order domain = new Order();
        domain.setNumber(dto.getNumber());
        domain.setCustomerName(dto.getCustomerName());
        domain.setStatus(dto.getStatus());
        domain.setCreatedAt(Instant.now());
        domain.setProductIds(dto.getProductIds());

        List<Product> products = productRepository.findAllById(domain.getProductIds());
        if (products.size() != domain.getProductIds().size()) {
            throw new ValidationException("Some products not found for ids: " + domain.getProductIds());
        }

        OrderEntity saved = orderRepository.save(toEntity(domain, products));
        return toDto(toDomain(saved));
    }

    @Override
    public OrderDto update(Long id, OrderCreateUpdateDto dto) {
        validate(dto);

        OrderEntity existing = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order id=%d not found".formatted(id)
                ));

        Order domain = toDomain(existing);
        domain.setNumber(dto.getNumber());
        domain.setCustomerName(dto.getCustomerName());
        domain.setStatus(dto.getStatus());
        domain.setProductIds(dto.getProductIds());

        List<Product> products = productRepository.findAllById(domain.getProductIds());
        if (products.size() != domain.getProductIds().size()) {
            throw new ValidationException("Some products not found for ids: " + domain.getProductIds());
        }

        OrderEntity saved = orderRepository.save(toEntity(domain, products));
        return toDto(toDomain(saved));
    }

    @Override
    public void delete(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        }
    }

    private void validate(OrderCreateUpdateDto dto) {
        if (dto.getNumber() == null || dto.getNumber().isBlank()) {
            throw new ValidationException("number is required");
        }
        if (dto.getProductIds() == null || dto.getProductIds().isEmpty()) {
            throw new ValidationException("productIds is required");
        }
    }
}
