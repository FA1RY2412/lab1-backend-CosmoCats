package org.example.cosmocats.order.service;

import org.example.cosmocats.order.dto.OrderCreateUpdateDto;
import org.example.cosmocats.order.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> findAll();
    OrderDto findById(Long id);
    OrderDto findByNumber(String number);
    OrderDto create(OrderCreateUpdateDto dto);
    OrderDto update(Long id, OrderCreateUpdateDto dto);
    void delete(Long id);
}
