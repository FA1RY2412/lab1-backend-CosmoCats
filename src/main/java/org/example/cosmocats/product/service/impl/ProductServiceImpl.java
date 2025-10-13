package org.example.cosmocats.product.service.impl;

import org.example.cosmocats.product.dto.ProductCreateUpdateDto;
import org.example.cosmocats.product.dto.ProductDto;
import org.example.cosmocats.product.entity.Product;
import org.example.cosmocats.product.repository.ProductRepository;
import org.example.cosmocats.product.service.ProductService;
import org.example.cosmocats.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private ProductDto toDto(Product p) {
        return new ProductDto(p.getId(), p.getName(), p.getDescription(), p.getPrice());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return productRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product id=" + id + " not found"));
        return toDto(p);
    }

    @Override
    public ProductDto create(ProductCreateUpdateDto dto) {
        Product p = new Product(dto.getName(), dto.getDescription(), dto.getPrice());
        Product saved = productRepository.save(p);
        return toDto(saved);
    }

    @Override
    public ProductDto update(Long id, ProductCreateUpdateDto dto) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product id=" + id + " not found"));
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        return toDto(productRepository.save(p));
    }

    @Override
    public void delete(Long id) {
        // Idempotent DELETE: multiple calls yield the same state; ignore if not present
        try {
            productRepository.deleteById(id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            // ignore: resource already absent
        }
    }
}
