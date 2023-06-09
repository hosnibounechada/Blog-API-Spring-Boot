package com.hb.blog.handler;

import com.hb.blog.constant.DatabaseErrorCode;
import com.hb.blog.error.ErrorModel;
import com.hb.blog.error.ErrorResponse;
import com.hb.blog.exception.*;
import com.hb.blog.payload.response.BadRequestErrorResponse;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hb.blog.util.StringUtils.toSnakeCase;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GoneException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ErrorResponse handleGoneException(GoneException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.GONE);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ObjectNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErrorResponse handleObjectNotValidException(ObjectNotValidException e) {
        return new BadRequestErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), e.getErrors());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorModel> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorModel(toSnakeCase(err.getField()), Objects.requireNonNull(err.getRejectedValue()).toString(), Objects.requireNonNull(err.getDefaultMessage())))
                .collect(Collectors.toList());

        return new BadRequestErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), errors);
    }
    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErrorResponse handleMethodArgumentNotValidException(PropertyReferenceException e) {
        ErrorModel errorModel = new ErrorModel(e.getPropertyName(),e.getPropertyName(),e.getMessage());
        List<ErrorModel> errors = new ArrayList<>();
        errors.add(errorModel);
        return new BadRequestErrorResponse("Parameters validation failed", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
       List<ErrorModel> errors = new ArrayList<>();
        var violations = e.getConstraintViolations();
        violations.forEach(violation -> {
            String errorMessage = violation.getMessage();
            String invalidValue = violation.getInvalidValue().toString();
            String parameterName = toSnakeCase(violation.getPropertyPath().toString().substring(violation.getPropertyPath().toString().indexOf(".") + 1));
            errors.add(new ErrorModel(parameterName, invalidValue, errorMessage));
        });
       return new BadRequestErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), errors);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return new ErrorResponse(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Handling this exception is based on the Database provider
    // This exception handler is meant to handle exceptions thrown by the database provider, specifically PostgreSQL
    // Any other database provider will cause the exception to response with Unknown field and value and message
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> handleSQLException(SQLException e) {
        switch (e.getSQLState()) {
            case DatabaseErrorCode.DUPLICATED -> {
                List<ErrorModel> errors = new ArrayList<>();
                // Regular expression pattern to match the key-value pair
                Pattern pattern = Pattern.compile("Key \\((.*?)\\)=\\((.*?)\\)");

                // Create a matcher object for the input string
                Matcher matcher = pattern.matcher(e.getMessage());

                // Check if a match is found
                if (matcher.find()) {
                    // Extract the key and value from the matched string
                    String key = matcher.group(1);
                    String value = matcher.group(2);

                    // Print the extracted key and value
                    ErrorModel errorModel = new ErrorModel(key, value, "Value already exist");
                    errors.add(errorModel);
                } else {
                    ErrorModel errorModel = new ErrorModel("Unknown field", "Unknown value", "Unknown message");
                    errors.add(errorModel);
                }
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new BadRequestErrorResponse("Already exists", HttpStatus.CONFLICT, HttpStatus.CONFLICT.toString(), errors));
            }
            case DatabaseErrorCode.SERVER -> {
                // Connection error
                return ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(new ErrorResponse("Service in temporary unavailable", HttpStatus.SERVICE_UNAVAILABLE));
            }
            case DatabaseErrorCode.CONSTRAINTS_VIOLATION -> {
                return ResponseEntity
                        .status(HttpStatus.LOCKED)
                        .body(new ErrorResponse("Constraint violation", HttpStatus.LOCKED));
            }
            default -> {
                // Unknown error
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("Unknown Error, please contact our support team ", HttpStatus.INTERNAL_SERVER_ERROR));
            }
        }
    }
}
