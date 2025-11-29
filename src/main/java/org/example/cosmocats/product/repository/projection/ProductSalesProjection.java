package org.example.cosmocats.product.repository.projection;

public interface ProductSalesProjection {
    Long getProductId();
    String getProductName();
    Long getOrdersCount();
}
