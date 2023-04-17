package com.hb.blog.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hb.blog.constant.DatabaseErrorCode;
import com.hb.blog.error.ErrorModel;
import com.hb.blog.error.ErrorResponse;
import com.hb.blog.exception.*;
import com.hb.blog.payload.response.BadRequestErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import static com.hb.blog.util.Converter.camelCaseToSnakeCase;
import static com.hb.blog.util.Converter.objectFromCamelCaseToSnakeCase;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(NotFoundException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GoneException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ErrorResponse handleException(GoneException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.GONE);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleException(ConflictException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ObjectNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErrorResponse handleException(ObjectNotValidException e) {
        return new BadRequestErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), e.getErrors());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleException(ResourceAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErrorResponse handleException(MethodArgumentNotValidException e) throws JsonProcessingException {
        List<ErrorModel> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorModel(camelCaseToSnakeCase(err.getField()), Objects.requireNonNull(err.getRejectedValue()).toString(), Objects.requireNonNull(err.getDefaultMessage())))
                .collect(Collectors.toList());

        return new BadRequestErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), errors);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> databaseHandlerException(SQLException e) {
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
            default -> {
                // Unknown error
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("Unknown Error, please contact our support team ", HttpStatus.INTERNAL_SERVER_ERROR));
            }
        }
    }
}
