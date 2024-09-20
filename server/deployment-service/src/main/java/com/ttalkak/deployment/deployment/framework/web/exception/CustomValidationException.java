package com.ttalkak.deployment.deployment.framework.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomValidationException extends RuntimeException {
    private final String message;
    private Errors errors;

    public Map<String, String> getErrors() {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return errorMap;
    }
}
