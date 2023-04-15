package com.hb.blog.handler;

import com.hb.blog.constant.DatabaseErrorCode;
import com.hb.blog.error.ErrorModel;
import com.hb.blog.error.ErrorResponse;
import com.hb.blog.exception.*;
import com.hb.blog.payload.response.BadRequestErrorResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hb.blog.util.Converter.camelCaseToSnakeCase;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /*@ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleException(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }*/

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

    /*@ExceptionHandler(ObjectNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseOld handleException(ObjectNotValidException e){
        return new ErrorResponseOld(e.getMessage(),HttpStatus.BAD_REQUEST);
    }*/
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
    public BadRequestErrorResponse handleException(MethodArgumentNotValidException e) {
        List<ErrorModel> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorModel(camelCaseToSnakeCase(err.getField()), err.getRejectedValue().toString(), Objects.requireNonNull(err.getDefaultMessage())))
                .collect(Collectors.toList());

        return new BadRequestErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), errors);
    }

/*    @ExceptionHandler(value = SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErrorResponse handleException(SQLException e){
        List<ErrorModel> errors = new ArrayList<>();
        errors.add(new ErrorModel(e.getMessage(),e.getSQLState(),e.getCause().toString()));
        System.out.println(e);
        System.out.println("Hello World");
        return new BadRequestErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(),errors);
    }*/

    /*@ExceptionHandler(value = DataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErrorResponse handleException(DataAccessException e){
        List<ErrorModel> errors = new ArrayList<>();
        errors.add(new ErrorModel(e.getMessage(),e.getCause().toString(),e.getCause().toString()));

        Throwable cause = e.getCause();
        if (cause instanceof SQLException) {
            SQLException sqlException = (SQLException) cause;
//            String constraintName = sqlException.getServerErrorMessage().getConstraint();
            String constraintName = sqlException.getSQLState().toString();
            int errorCode = sqlException.getErrorCode();
            if (errorCode == 23505) {
                // Handle unique constraint violation error
                throw new DuplicateKeyException("Duplicate value for unique constraint " + constraintName, e);
            }
        }
        return new BadRequestErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(),errors);
    }*/
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> databaseHandlerException(SQLException e) {
        ErrorModel errorModel = new ErrorModel();
        switch (e.getSQLState()) {
            case DatabaseErrorCode.DUPLICATED: {
                // Get the index of the start and end of the column name(s) in the error message
                int startIndex = e.getMessage().indexOf("(") + 1;
                int endIndex = e.getMessage().indexOf(")", startIndex);
                String columnName = e.getMessage().substring(startIndex, endIndex);
                // Extract the column name(s) from the error message
                errorModel.setFieldName(columnName);
                // Customize the error message with the column name(s)
                errorModel.setMessage(String.format("A duplicate key value was found for column(s): %s.", columnName));
                // Duplicate key error
                // errorMessage = "A record with the same value already exists.";
                String patternString = "\\(username\\)=\\((.*?)\\)";
                // Create a pattern object and a matcher object
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(e.getMessage());
                // Extract the username from the matched substring
                if (matcher.find()) {
                    errorModel.setRejectedValue(matcher.group(1));
                }
                break;
            }
            case DatabaseErrorCode.SERVER: {
                // Connection error
                errorModel.setMessage("Unable to connect to the database.");
                break;
            }
        }
        System.out.println(e.getSQLState());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorModel);
    }
}
