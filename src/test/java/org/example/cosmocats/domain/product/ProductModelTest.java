package org.example.cosmocats.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductModelTest {

    @Test
    void constructorAndGetters_workCorrectly() {
        Long id = 42L;
        String name = "Cosmic Tuna";
        String description = "Best tuna in the galaxy";
        Double price = 9.99;

        ProductModel model = new ProductModel(id, name, description, price);

        assertEquals(id, model.getId());
        assertEquals(name, model.getName());
        assertEquals(description, model.getDescription());
        assertEquals(price, model.getPrice());
    }
}
