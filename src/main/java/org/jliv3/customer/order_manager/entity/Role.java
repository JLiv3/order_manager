package org.jliv3.customer.order_manager.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Role {
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    public static String[] getRoles() {
        return new String[]{ADMIN, USER};
    }
}
