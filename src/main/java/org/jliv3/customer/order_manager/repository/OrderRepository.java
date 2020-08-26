package org.jliv3.customer.order_manager.repository;

import org.jliv3.customer.order_manager.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCreateBy(String createBy);
}
