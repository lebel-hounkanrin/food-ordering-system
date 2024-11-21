package com.food.ordering.system.application.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus
    public ErrorDTO handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorDTO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Unexpected error").build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus
    public ErrorDTO handleValidationException(ValidationException e) {
        ErrorDTO errorDTO;
        if(e instanceof ConstraintViolationException) {
            String violations = extractViolationsFromException((ConstraintViolationException) e);
            log.error(violations, e);
            errorDTO = ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(violations)
                    .build();
        } else {
            log.error(e.getMessage(), e);
            errorDTO = ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.getReasonPhrase()).message(e.getMessage()).build();
        }
        return errorDTO;
    }

    private String extractViolationsFromException(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));
    }
}
