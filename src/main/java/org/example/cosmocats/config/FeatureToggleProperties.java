package org.example.cosmocats.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "feature-toggle")
public class FeatureToggleProperties {

    private final Map<String, Boolean> toggles;

    public FeatureToggleProperties(Map<String, Boolean> toggles) {
        this.toggles = toggles;
    }

    public Map<String, Boolean> getToggles() {
        return toggles;
    }
}

