package org.example.cosmocats.order.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private Long id;
    private String number;
    private String customerName;
    private String status;
    private Instant createdAt;
    // на domain-рівні зберігаємо тільки id продуктів
    private List<Long> productIds = new ArrayList<>();

    public Order() {
    }

    public Order(Long id,
                 String number,
                 String customerName,
                 String status,
                 Instant createdAt,
                 List<Long> productIds) {
        this.id = id;
        this.number = number;
        this.customerName = customerName;
        this.status = status;
        this.createdAt = createdAt;
        if (productIds != null) {
            this.productIds = new ArrayList<>(productIds);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds != null ? new ArrayList<>(productIds) : new ArrayList<>();
    }
}
