package org.example.cosmocats.product.service.impl;

import org.example.cosmocats.common.exception.ValidationException;
import org.example.cosmocats.product.dto.ProductCreateUpdateDto;
import org.example.cosmocats.product.dto.ProductDto;
import org.example.cosmocats.product.entity.Product;
import org.example.cosmocats.product.repository.ProductRepository;
import org.example.cosmocats.product.service.ProductService;
import org.example.cosmocats.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return productRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product id=%d not found".formatted(id)));
        return toDto(p);
    }

    @Override
    public ProductDto create(ProductCreateUpdateDto dto) {
        validate(dto);
        Product p = new Product(dto.getName(), dto.getDescription(), dto.getPrice());
        Product saved = productRepository.save(p);
        return toDto(saved);
    }

    @Override
    public ProductDto update(Long id, ProductCreateUpdateDto dto) {
        validate(dto);
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product id=%d not found".formatted(id)));
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        return toDto(productRepository.save(p));
    }

    @Override
    public void delete(Long id) {
        
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    private void validate(ProductCreateUpdateDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ValidationException("name is required");
        }
        if (dto.getPrice() == null) {
            throw new ValidationException("price is required");
        }
        if (dto.getPrice() <= 0) {
            throw new ValidationException("price must be > 0");
        }
    }
}
