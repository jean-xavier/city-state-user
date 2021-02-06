package com.challenge.citystateuser.api.exceptions;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class ApiErrors {
    private final List<String> errors;

    public ApiErrors(BindingResult result) {
        errors = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
    }

    public List<String> getErrors() {
        return errors;
    }
}
