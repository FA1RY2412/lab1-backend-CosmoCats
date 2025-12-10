package org.example.cosmocats.featuretoggle;

import org.example.cosmocats.config.FeatureToggleProperties;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FeatureToggleServiceTest {

    @Test
    void isEnabled_usesPropertiesAndDefaultsToFalse() {
        // given
        FeatureToggleProperties properties = new FeatureToggleProperties(
                Map.of(
                        "cosmo-cats", true,
                        "kitty-products", false
                )
        );

        FeatureToggleService service = new FeatureToggleService(properties);

        // when / then
        assertTrue(service.isEnabled("cosmo-cats"),
                "cosmo-cats має бути увімкнений");
        assertFalse(service.isEnabled("kitty-products"),
                "kitty-products вимкнений у конфігурації");
        assertFalse(service.isEnabled("unknown-feature"),
                "невідомі фічі мають вважатися вимкненими");
    }
}
