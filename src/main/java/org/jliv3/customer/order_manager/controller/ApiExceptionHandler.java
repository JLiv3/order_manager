package org.jliv3.customer.order_manager.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handlerApiException(ApiException apiException, WebRequest webRequest) {
        return new ResponseEntity<>(new ApiError(new Date(), apiException.getMessage(), webRequest.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handlerConstraintViolationException(ConstraintViolationException constraintViolationException, WebRequest webRequest) {
        return  new ResponseEntity<>(new ApiError(new Date(), "Duplicate image.", webRequest.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
