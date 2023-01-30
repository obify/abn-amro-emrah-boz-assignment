package com.abnamro.emrahboz.assignment.retail.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceAlreadyExistsException.class})
    public ResponseEntity<ExceptionResponse> resourceAlreadyExists(ResourceAlreadyExistsException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode("CONFLICT");
        response.setErrorMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        log.error("ResourceAlreadyExists exception occured. Exception Message : {}," +
                " Exception : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ExceptionResponse> resourceNotFound(ResourceNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode("NOT_FOUND");
        response.setErrorMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        log.error("ResourceNotFoundException exception occured. Exception Message : {}," +
                " Exception : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ResourceViolationException.class})
    public ResponseEntity<ExceptionResponse> resourceViolation(ResourceViolationException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode("FORBIDDEN");
        response.setErrorMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        log.error("ResourceViolationException exception occured. Exception Message : {}," +
                " Exception : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> customException(Exception ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode("INTERNAL_SERVER_ERROR");
        response.setErrorMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        log.error("Unhandled exception occured. Exception Message : {}," +
                " Exception : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
