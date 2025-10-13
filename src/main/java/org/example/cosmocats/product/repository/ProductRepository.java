package org.example.cosmocats.product.repository;

import org.example.cosmocats.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> { }
