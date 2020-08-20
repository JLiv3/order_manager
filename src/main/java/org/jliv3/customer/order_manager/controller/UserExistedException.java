package org.jliv3.customer.order_manager.controller;

public class UserExistedException extends RuntimeException {
    public UserExistedException(String message) {
        super(message);
    }
}
