package com.hb.blog.error;

import java.util.Objects;

public class ErrorModel {
    private String fieldName;
    private String rejectedValue;
    private String message;

    public ErrorModel() {
    }

    public ErrorModel(String fieldName, String rejectedValue, String message) {
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(String rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorModel{" +
                "fieldName='" + fieldName + '\'' +
                ", rejectedValue='" + rejectedValue + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorModel that = (ErrorModel) o;
        return Objects.equals(fieldName, that.fieldName) && Objects.equals(rejectedValue, that.rejectedValue) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, rejectedValue, message);
    }
}
