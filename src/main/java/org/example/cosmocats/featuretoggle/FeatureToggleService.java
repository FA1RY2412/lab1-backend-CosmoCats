package org.example.cosmocats.featuretoggle;

import org.example.cosmocats.config.FeatureToggleProperties;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class FeatureToggleService {

  private final ConcurrentHashMap<String, Boolean> featureToggleMap;

  public FeatureToggleService(FeatureToggleProperties featureToggleProperties) {
    featureToggleMap = new ConcurrentHashMap<>(featureToggleProperties.getToggles());
  }

  public boolean checkFeatureToggle(String featureName) {
    return featureToggleMap.getOrDefault(featureName, false);
  }

  public void enableFeatureToggle(String featureName) {
    featureToggleMap.put(featureName, true);
  }

  public void disableFeatureToggle(String featureName) {
    featureToggleMap.put(featureName, false);
  }
}