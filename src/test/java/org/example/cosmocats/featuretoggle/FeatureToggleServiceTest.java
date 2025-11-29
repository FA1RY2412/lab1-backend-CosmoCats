package org.example.cosmocats.featuretoggle;

import org.example.cosmocats.config.FeatureToggleProperties;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FeatureToggleServiceTest {

    @Test
    void checkFeatureToggle_usesInitialPropertiesAndDefaultFalse() {
        // given
        FeatureToggleProperties properties = new FeatureToggleProperties();
        Map<String, Boolean> toggles = new HashMap<>();
        toggles.put("cosmo-cats", true);
        properties.setToggles(toggles);

        FeatureToggleService service = new FeatureToggleService(properties);

        // when / then
        assertTrue(service.checkFeatureToggle("cosmo-cats"),
                "expected cosmo-cats to be enabled from properties");
        assertFalse(service.checkFeatureToggle("unknown-feature"),
                "unknown feature should be disabled by default");
    }

    @Test
    void enableFeatureToggle_turnsFeatureOn() {
        FeatureToggleProperties properties = new FeatureToggleProperties();
        FeatureToggleService service = new FeatureToggleService(properties);

        assertFalse(service.checkFeatureToggle("kitty-products"));

        service.enableFeatureToggle("kitty-products");

        assertTrue(service.checkFeatureToggle("kitty-products"),
                "feature should become enabled after explicit enable");
    }

    @Test
    void disableFeatureToggle_turnsFeatureOff() {
        FeatureToggleProperties properties = new FeatureToggleProperties();
        Map<String, Boolean> toggles = new HashMap<>();
        toggles.put("cosmo-cats", true);
        properties.setToggles(toggles);

        FeatureToggleService service = new FeatureToggleService(properties);
        assertTrue(service.checkFeatureToggle("cosmo-cats"));

        service.disableFeatureToggle("cosmo-cats");

        assertFalse(service.checkFeatureToggle("cosmo-cats"),
                "feature should become disabled after explicit disable");
    }
}
