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
    FeatureToggleService featureToggleService;

    @InjectMocks
    FeatureToggleAspect aspect;

    private FeatureToggle cosmoCatsAnnotation() {
        return new FeatureToggle() {
            @Override
            public FeatureToggles value() {
                return FeatureToggles.COSMO_CATS;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return FeatureToggle.class;
            }
        };
    }

    @Test
    void whenFeatureEnabled_allowsMethodExecution() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        FeatureToggle annotation = cosmoCatsAnnotation();

        when(featureToggleService.isEnabled("cosmo-cats")).thenReturn(true);
        when(joinPoint.proceed()).thenReturn("OK");

        Object result = aspect.aroundFeatureToggle(joinPoint, annotation);

        assertEquals("OK", result);
        verify(featureToggleService).isEnabled("cosmo-cats");
        verify(joinPoint).proceed();
    }

    @Test
    void whenFeatureDisabled_throwsExceptionAndDoesNotProceed() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        FeatureToggle annotation = cosmoCatsAnnotation();

        when(featureToggleService.isEnabled("cosmo-cats")).thenReturn(false);

        DisabledFeatureToggleException ex = assertThrows(
                DisabledFeatureToggleException.class,
                () -> aspect.aroundFeatureToggle(joinPoint, annotation)
        );

        org.assertj.core.api.Assertions.assertThat(ex.getMessage())
                .contains("cosmo-cats");

        verify(featureToggleService).isEnabled("cosmo-cats");
        verify(joinPoint, never()).proceed();
    }
}
