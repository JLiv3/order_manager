package org.jliv3.customer.order_manager.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MySimpleDateFormat {
    public static final DateFormat get() {
        return new SimpleDateFormat("yyyyMMddHHmmss");
    }
}
