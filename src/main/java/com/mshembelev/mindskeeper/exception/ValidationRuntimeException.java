package com.mshembelev.mindskeeper.exception;

import org.springframework.validation.BindingResult;

public class ValidationRuntimeException extends RuntimeException {
    private final BindingResult bindingResult;

    public ValidationRuntimeException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
