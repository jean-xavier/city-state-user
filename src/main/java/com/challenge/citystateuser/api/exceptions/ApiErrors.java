package com.challenge.citystateuser.api.exceptions;

import com.challenge.citystateuser.domain.exceptions.BusinessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ApiErrors {
    private final List<String> errors;

    public ApiErrors(BindingResult result) {
        errors = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
    }

    public ApiErrors(BusinessException e) {
        errors = Collections.singletonList(e.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
