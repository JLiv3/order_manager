package org.jliv3.customer.order_manager.controller;

import org.jliv3.customer.order_manager.entity.User;
import org.jliv3.customer.order_manager.entity.UserDTO;
import org.jliv3.customer.order_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {
    public static final String USERNAME_EXISTED_MESSAGE = "Tên người dùng đã tồn tại, hãy chọn tên người dùng khác.";
    public static final String USER_ID_NOT_FOUND_MESSAGE = "Không tìm thấy người dùng: ";
    public static final String CANT_DELETE_YOUR_SELF_MESSAGE = "Không thể xóa chính bạn.";
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
        if (optionalUser.isPresent()) {
            throw new ApiException(USERNAME_EXISTED_MESSAGE);
        } else {
            return new ResponseEntity<>(userRepository.save(User.builder().username(userDTO.getUsername()).password(passwordEncoder.encode(userDTO.getPassword())).role(userDTO.getRole()).build()), HttpStatus.OK);
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new ApiException(USER_ID_NOT_FOUND_MESSAGE + userDTO.getId()));
        if (!user.getUsername().equals(userDTO.getUsername()) && userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ApiException(USERNAME_EXISTED_MESSAGE);
        } else {
            user.setUsername(userDTO.getUsername());
            if (!userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            user.setRole(userDTO.getRole());
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        }

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_ID_NOT_FOUND_MESSAGE + userId));
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(user.getUsername())) {
            throw new ApiException(CANT_DELETE_YOUR_SELF_MESSAGE);
        } else {
            userRepository.delete(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }
}

