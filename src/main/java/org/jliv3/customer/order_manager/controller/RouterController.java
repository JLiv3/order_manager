package org.jliv3.customer.order_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouterController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/orders")
    public String orders() {
        return index();
    }

    @GetMapping("/users")
    public String users() {
        return "users";
    }

    @GetMapping("/trace")
    public String trace() {
        return "trace";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }
}
