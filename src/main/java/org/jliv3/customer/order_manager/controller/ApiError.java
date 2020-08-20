package org.jliv3.customer.order_manager.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ApiError {
    private Date timestamp;
    private String message;
    private String details;
}