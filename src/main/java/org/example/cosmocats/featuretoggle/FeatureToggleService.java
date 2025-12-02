package org.example.cosmocats.featuretoggle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeatureToggleService {

    private final FeatureToggleProperties properties;

    public boolean isEnabled(String featureName) {
        Map<String, Boolean> toggles = properties.getToggles();
        return toggles.getOrDefault(featureName, false);
    }
}
