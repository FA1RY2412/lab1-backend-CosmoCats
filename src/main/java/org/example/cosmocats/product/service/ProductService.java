package org.example.cosmocats.product.service;

import org.example.cosmocats.product.dto.ProductCreateUpdateDto;
import org.example.cosmocats.product.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();
    ProductDto findById(Long id);
    ProductDto create(ProductCreateUpdateDto dto);
    ProductDto update(Long id, ProductCreateUpdateDto dto);
    void delete(Long id);
}
