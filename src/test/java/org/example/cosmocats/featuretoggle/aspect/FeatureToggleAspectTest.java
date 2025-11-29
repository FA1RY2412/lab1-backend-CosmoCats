package org.example.cosmocats.featuretoggle.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.example.cosmocats.featuretoggle.FeatureToggleService;
import org.example.cosmocats.featuretoggle.FeatureToggles;
import org.example.cosmocats.featuretoggle.annotation.FeatureToggle;
import org.example.cosmocats.featuretoggle.exception.DisabledFeatureToggleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureToggleAspectTest {

    @Mock
    private FeatureToggleService featureToggleService;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private FeatureToggle featureToggle;

    @InjectMocks
    private FeatureToggleAspect aspect;

    @Test
    void aroundFeatureToggle_whenEnabled_proceeds() throws Throwable {
        
        when(featureToggle.value()).thenReturn(FeatureToggles.COSMO_CATS);
        when(featureToggleService.checkFeatureToggle("cosmo-cats")).thenReturn(true);
        when(joinPoint.proceed()).thenReturn("ok");

        
        Object result = aspect.aroundFeatureToggle(joinPoint, featureToggle);

        
        assertEquals("ok", result);
        verify(featureToggleService).checkFeatureToggle("cosmo-cats");
        verify(joinPoint).proceed();
    }

    @Test
    void aroundFeatureToggle_whenDisabled_throwsException() throws Throwable {
        
        when(featureToggle.value()).thenReturn(FeatureToggles.COSMO_CATS);
        when(featureToggleService.checkFeatureToggle("cosmo-cats")).thenReturn(false);

        
        DisabledFeatureToggleException ex = assertThrows(
                DisabledFeatureToggleException.class,
                () -> aspect.aroundFeatureToggle(joinPoint, featureToggle)
        );

        org.assertj.core.api.Assertions.assertThat(ex.getMessage())
                .contains("cosmo-cats");
        verify(featureToggleService).checkFeatureToggle("cosmo-cats");
        verify(joinPoint, never()).proceed();
    }

}
