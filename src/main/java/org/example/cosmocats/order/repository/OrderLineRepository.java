package org.example.cosmocats.order.repository;

import org.example.cosmocats.product.projection.ProductSalesProjection;
import org.example.cosmocats.order.entity.OrderLine;
import org.example.cosmocats.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

    @Query("""
        SELECT p.name AS productName, SUM(ol.quantity) AS salesCount
        FROM OrderLine ol
        JOIN ol.product p
        GROUP BY p.name
        ORDER BY salesCount DESC
    """)
    List<ProductSalesProjection> findTopSellingProducts();
}
