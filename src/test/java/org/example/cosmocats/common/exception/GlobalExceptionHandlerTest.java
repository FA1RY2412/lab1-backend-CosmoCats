package org.example.cosmocats.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_returns404() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("Order not found");

        ProblemDetail pd = handler.handleNotFound(ex);

        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getTitle()).isEqualTo("Not Found");
        assertThat(pd.getDetail()).isEqualTo("Order not found");
        assertThat(pd.getType().toString()).endsWith("#not-found");
    }

    @Test
    void handleValidation_returns400() {
        ValidationException ex =
                new ValidationException("Bad request data");

        ProblemDetail pd = handler.handleValidation(ex);

        assertThat(pd.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(pd.getTitle()).isEqualTo("Validation failed");
        assertThat(pd.getDetail()).isEqualTo("Bad request data");
        assertThat(pd.getType().toString()).endsWith("#validation");
    }

    @Test
    void handleConflict_returns409() {
        ConflictException ex =
                new ConflictException("Order already exists");

        ProblemDetail pd = handler.handleConflict(ex);

        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getTitle()).isEqualTo("Conflict");
        assertThat(pd.getDetail()).isEqualTo("Order already exists");
        assertThat(pd.getType().toString()).endsWith("#conflict");
    }

    @Test
    void handleBeanValidation_returns400AndFieldsMap() {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "order");
        bindingResult.addError(new FieldError(
                "order", "number", "must not be blank"
        ));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ProblemDetail pd = handler.handleBeanValidation(ex);

        assertThat(pd.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(pd.getTitle()).isEqualTo("Validation failed");
        assertThat(pd.getDetail()).isEqualTo("Request validation errors");
        assertThat(pd.getType().toString()).endsWith("#validation");

        @SuppressWarnings("unchecked")
        Map<String, String> fields =
                (Map<String, String>) pd.getProperties().get("fields");

        assertThat(fields)
                .containsEntry("number", "must not be blank");
    }
}
