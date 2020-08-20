package org.jliv3.customer.order_manager.repository;

import org.jliv3.customer.order_manager.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Set<User> findAll();
}
