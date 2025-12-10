package org.example.cosmocats.order.exception;

import org.example.cosmocats.common.exception.ResourceNotFoundException;

public class OrderNotFoundException extends ResourceNotFoundException {

    public OrderNotFoundException(Long id) {
        super("Order id=%d not found".formatted(id));
    }

    public OrderNotFoundException(String number) {
        super("Order number=%s not found".formatted(number));
    }
}
