package org.example.cosmocats.order.service.impl;

import org.example.cosmocats.common.exception.ResourceNotFoundException;
import org.example.cosmocats.common.exception.ValidationException;
import org.example.cosmocats.order.dto.OrderCreateUpdateDto;
import org.example.cosmocats.order.dto.OrderDto;
import org.example.cosmocats.order.entity.Order;
import org.example.cosmocats.order.repository.OrderRepository;
import org.example.cosmocats.order.service.OrderService;
import org.example.cosmocats.product.entity.Product;
import org.example.cosmocats.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private OrderDto toDto(Order o) {
        List<Long> productIds = o.getProducts().stream()
                .map(Product::getId)
                .toList();
        return new OrderDto(o.getId(), o.getNumber(), o.getCustomerName(), o.getStatus(), o.getCreatedAt(), productIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order id=%d not found".formatted(id)));
        return toDto(o);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findByNumber(String number) {
        Order o = orderRepository.findByNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Order number=%s not found".formatted(number)));
        return toDto(o);
    }

    @Override
    public OrderDto create(OrderCreateUpdateDto dto) {
        validate(dto);
        Order o = new Order(dto.getNumber(), dto.getCustomerName(), dto.getStatus());
        List<Product> products = productRepository.findAllById(dto.getProductIds());
        if (products.size() != dto.getProductIds().size()) {
            throw new ValidationException("Some products not found for ids: " + dto.getProductIds());
        }
        o.setProducts(products);
        return toDto(orderRepository.save(o));
    }

    @Override
    public OrderDto update(Long id, OrderCreateUpdateDto dto) {
        validate(dto);
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order id=%d not found".formatted(id)));
        o.setNumber(dto.getNumber());
        o.setCustomerName(dto.getCustomerName());
        o.setStatus(dto.getStatus());

        List<Product> products = productRepository.findAllById(dto.getProductIds());
        if (products.size() != dto.getProductIds().size()) {
            throw new ValidationException("Some products not found for ids: " + dto.getProductIds());
        }
        o.setProducts(products);

        return toDto(orderRepository.save(o));
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
