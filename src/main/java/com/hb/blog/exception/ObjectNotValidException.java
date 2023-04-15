package com.hb.blog.exception;

import com.hb.blog.error.ErrorModel;

import java.util.List;

public class ObjectNotValidException extends RuntimeException {
    private List<ErrorModel> errors;
    public ObjectNotValidException(String message) {
        super(message);
    }

    public ObjectNotValidException(String message, List<ErrorModel> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ErrorModel> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorModel> errors) {
        this.errors = errors;
    }
}
