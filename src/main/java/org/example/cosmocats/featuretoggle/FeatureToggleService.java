package org.example.cosmocats.featuretoggle;

import lombok.RequiredArgsConstructor;
import org.example.cosmocats.config.FeatureToggleProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureToggleService {

    private final FeatureToggleProperties properties;

    public boolean isEnabled(String featureName) {
        Boolean value = properties.getToggles().get(featureName);
        return Boolean.TRUE.equals(value);
    }
}
