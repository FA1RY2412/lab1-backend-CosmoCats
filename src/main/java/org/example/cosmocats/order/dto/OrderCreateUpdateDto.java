package org.example.cosmocats.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderCreateUpdateDto {

    @NotBlank
    private String number;

    private String customerName;

    private String status;

    @NotNull
    private List<Long> productIds;

    public OrderCreateUpdateDto() {
    }

    public OrderCreateUpdateDto(String number, String customerName, String status, List<Long> productIds) {
        this.number = number;
        this.customerName = customerName;
        this.status = status;
        this.productIds = productIds;
    }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
}
