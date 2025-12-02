package org.example.cosmocats.featuretoggle;

import org.example.cosmocats.config.FeatureToggleProperties;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeatureToggleService {

    private final FeatureToggleProperties properties;

    public boolean isEnabled(String feature) {
        return properties.getToggles().getOrDefault(feature, false);
    }
}

