package org.jliv3.customer.order_manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(UserExistedException.class)
    public ResponseEntity<ApiError> handlerUserExistedException(UserExistedException existedException, WebRequest webRequest) {
        return new ResponseEntity<>(new ApiError(new Date(), existedException.getMessage(), webRequest.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
