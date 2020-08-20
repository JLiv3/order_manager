package org.jliv3.customer.order_manager.security;

import org.jliv3.customer.order_manager.entity.User;
import org.jliv3.customer.order_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optional = userRepository.findByUsername(username);
        if (!optional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return org.springframework.security.core.userdetails.User.withUsername(optional.get().getUsername()).password(optional.get().getPassword()).roles(optional.get().getRole()).build();
    }
}
