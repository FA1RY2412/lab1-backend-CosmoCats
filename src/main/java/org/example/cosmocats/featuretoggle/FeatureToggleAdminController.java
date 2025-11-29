package org.example.cosmocats.featuretoggle;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/features")
public class FeatureToggleAdminController {

    private final FeatureToggleService service;

    public FeatureToggleAdminController(FeatureToggleService service) {
        this.service = service;
    }

    @PostMapping("/enable")
    public ResponseEntity<?> enable(@RequestParam String feature) {
        service.enableFeatureToggle(feature);
        return ResponseEntity.ok(Map.of("feature", feature, "enabled", true));
    }

    @PostMapping("/disable")
    public ResponseEntity<?> disable(@RequestParam String feature) {
        service.disableFeatureToggle(feature);
        return ResponseEntity.ok(Map.of("feature", feature, "enabled", false));
    }

    @GetMapping("/status")
    public ResponseEntity<?> status(@RequestParam String feature) {
        boolean enabled = service.checkFeatureToggle(feature);
        return ResponseEntity.ok(Map.of("feature", feature, "enabled", enabled));
    }
}
