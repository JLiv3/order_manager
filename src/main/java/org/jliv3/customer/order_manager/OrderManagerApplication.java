package org.jliv3.customer.order_manager;

import org.jliv3.customer.order_manager.entity.Role;
import org.jliv3.customer.order_manager.entity.User;
import org.jliv3.customer.order_manager.repository.RoleRepository;
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
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(OrderManagerApplication.class, args);
    }

    @PostConstruct
    @Transactional
    void builtIn() {
        Role roleAdmin = new Role();
        roleAdmin.setRolename("ADMIN");
        roleAdmin.setDecription("Admin role with full power");
        roleRepository.save(roleAdmin);
        Role roleUser = new Role();
        roleUser.setRolename("USER");
        roleUser.setDecription("User role with limit power");
        roleRepository.save(roleUser);
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole(roleAdmin);
        userRepository.save(admin);
    }
}
