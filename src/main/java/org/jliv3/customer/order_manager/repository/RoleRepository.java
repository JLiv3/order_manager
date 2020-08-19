package org.jliv3.customer.order_manager.repository;

import org.jliv3.customer.order_manager.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}

