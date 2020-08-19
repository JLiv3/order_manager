package org.jliv3.customer.order_manager.security;

import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Primary
public class SimplePaswordEncoder extends BCryptPasswordEncoder {

}
