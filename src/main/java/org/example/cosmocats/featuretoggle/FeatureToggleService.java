package org.example.cosmocats.featuretoggle;

import org.example.cosmocats.config.FeatureToggleProperties;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FeatureToggleService {

    private final Map<String, Boolean> featureToggleMap;

    public FeatureToggleService(FeatureToggleProperties featureToggleProperties) {
        this.featureToggleMap = Map.copyOf(featureToggleProperties.getToggles());
    }

    public boolean checkFeatureToggle(String featureName) {
        return featureToggleMap.getOrDefault(featureName, false);
    }

    public boolean checkFeatureToggle(FeatureToggles toggle) {
        return checkFeatureToggle(toggle.getFeatureName());
    }
}
