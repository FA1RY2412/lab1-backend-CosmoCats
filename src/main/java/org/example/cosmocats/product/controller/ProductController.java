package org.example.cosmocats.product.controller;

import org.example.cosmocats.product.dto.ProductCreateUpdateDto;
import org.example.cosmocats.product.dto.ProductDto;
import org.example.cosmocats.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductDto getOne(@PathVariable("id") Long id) {
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@Validated @RequestBody ProductCreateUpdateDto dto) {
        return productService.create(dto);
    }

    @PutMapping("/{id}")
    public ProductDto update(@PathVariable("id") Long id, @Validated @RequestBody ProductCreateUpdateDto dto) {
        return productService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        productService.delete(id); // ідемпотентно
    }
}
