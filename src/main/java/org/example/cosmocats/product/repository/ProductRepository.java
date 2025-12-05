package org.example.cosmocats.product.repository;

import org.example.cosmocats.product.entity.Product;
import org.example.cosmocats.product.repository.projection.ProductSalesProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        select p.id as productId,
               p.name as productName,
               count(o.id) as ordersCount
        from OrderEntity o
            join o.products p
        group by p.id, p.name
        order by count(o.id) desc
        """)
    List<ProductSalesProjection> findTopSellingProducts(Pageable pageable);
}
