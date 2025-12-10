package org.example.cosmocats.order.controller;

import jakarta.validation.Valid;
import org.example.cosmocats.order.dto.OrderCreateUpdateDto;
import org.example.cosmocats.order.dto.OrderDto;
import org.example.cosmocats.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderDto> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/by-number/{number}")
    public OrderDto getByNumber(@PathVariable String number) {
        return service.findByNumber(number);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Valid @RequestBody OrderCreateUpdateDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public OrderDto update(@PathVariable Long id, @Valid @RequestBody OrderCreateUpdateDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
