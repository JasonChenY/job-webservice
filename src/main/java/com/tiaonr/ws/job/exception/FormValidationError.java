package com.tiaonr.ws.job.exception;

import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author jason.y.chen
 */
public class FormValidationError extends Exception {

    private List<FieldError> fieldErrors;

    public FormValidationError(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
