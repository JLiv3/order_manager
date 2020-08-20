package org.jliv3.customer.order_manager.controller;

import org.jliv3.customer.order_manager.entity.User;
import org.jliv3.customer.order_manager.entity.UserDTO;
import org.jliv3.customer.order_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {
    public static final String USERNAME_EXISTED_MESSAGE = "Username already exist, choose different one.";
    public static final String USERID_NOT_FOUND_MESSAGE = "User not found: ";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<Set<User>> getUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getUsername());
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(userRepository.save(User.builder().username(userDTO.getUsername()).password(passwordEncoder.encode(userDTO.getPassword())).role(userDTO.getRole()).build()), HttpStatus.OK);
        } else {
            throw new UserExistedException(USERNAME_EXISTED_MESSAGE);
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent()) {
            if (!userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                optionalUser.get().setUsername(userDTO.getUsername());
                if (!userDTO.getPassword().isEmpty()) {
                    optionalUser.get().setPassword(passwordEncoder.encode(userDTO.getPassword()));
                }
                optionalUser.get().setRole(userDTO.getRole());
                return new ResponseEntity<>(userRepository.save(optionalUser.get()), HttpStatus.OK);
            } else {
                throw new UserExistedException(USERNAME_EXISTED_MESSAGE);
            }
        } else {
            throw new UsernameNotFoundException(USERID_NOT_FOUND_MESSAGE + userDTO.getId());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException(USERID_NOT_FOUND_MESSAGE + userId);
        }
    }
}

