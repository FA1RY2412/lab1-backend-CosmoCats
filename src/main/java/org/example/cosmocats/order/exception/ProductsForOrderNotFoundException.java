package org.example.cosmocats.order.exception;

import org.example.cosmocats.common.exception.ValidationException;

import java.util.List;

public class ProductsForOrderNotFoundException extends ValidationException {

    public ProductsForOrderNotFoundException(List<Long> ids) {
        super("Some products not found for ids: " + ids);
    }
}
