package org.jliv3.customer.order_manager;

import org.jliv3.customer.order_manager.entity.Role;
import org.jliv3.customer.order_manager.entity.User;
import org.jliv3.customer.order_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class OrderManagerApplication {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(OrderManagerApplication.class, args);
    }

    @PostConstruct
    @Transactional
    void builtIn() {
        if (!userRepository.findByUsername("admin").isPresent()) {
            userRepository.save(User.builder().username("admin").password(passwordEncoder.encode("admin")).role(Role.ADMIN).build());
        }
    }
}
