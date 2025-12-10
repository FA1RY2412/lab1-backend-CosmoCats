package org.example.cosmocats.featuretoggle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DisabledFeatureToggleException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Feature toggle %s is disabled";

  public DisabledFeatureToggleException(String featureToggleName) {
    super(String.format(ERROR_MESSAGE, featureToggleName));
  }
}