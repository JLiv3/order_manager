package org.jliv3.customer.order_manager.repository;

import org.jliv3.customer.order_manager.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCreateBy(String createBy);
}
