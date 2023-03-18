package com.zemoso.figma.growthcapital.exceptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zemoso.figma.growthcapital.util.ErrorObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorObject> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());

        errorObject.setMessage(ex.getMessage());

        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorObject> handleMethodArgumentMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());

        errorObject.setMessage(ex.getMessage());

        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemExistsException.class)
    public ResponseEntity<ErrorObject> handleItemExistsException(ItemExistsException ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.CONFLICT.value());

        errorObject.setMessage(ex.getMessage());

        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleGeneralException(Exception ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        errorObject.setMessage(ex.getMessage());

        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        body.put("statusCode", HttpStatus.BAD_REQUEST.value());

        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage()).toList();

        body.put("messages", errors);

        body.put("timestamp", new Date());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}