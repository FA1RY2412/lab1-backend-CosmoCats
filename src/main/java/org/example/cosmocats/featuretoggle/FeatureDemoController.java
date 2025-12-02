package org.example.cosmocats.featuretoggle;

import org.example.cosmocats.featuretoggle.annotation.FeatureToggle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/demo")
public class FeatureDemoController {

    @GetMapping("/cosmo")
    @FeatureToggle(FeatureToggles.COSMO_CATS)
    public ResponseEntity<?> demoCosmo(@RequestParam(defaultValue = "3") int count) {
        return ResponseEntity.ok(Map.of(
                "msg", "Here are " + count + " cosmo cats"
        ));
    }
}
