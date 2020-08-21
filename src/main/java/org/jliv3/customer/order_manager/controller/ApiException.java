package org.jliv3.customer.order_manager.controller;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
