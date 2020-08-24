package org.jliv3.customer.order_manager.repository;

import org.jliv3.customer.order_manager.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
