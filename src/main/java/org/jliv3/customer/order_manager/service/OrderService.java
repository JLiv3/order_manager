package org.jliv3.customer.order_manager.service;

import org.jliv3.customer.order_manager.entity.Order;
import org.jliv3.customer.order_manager.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> findByCreateBy(String createBy) {
        return orderRepository.findByCreateBy(createBy);
    }

    public Optional<Order> findById(Integer id) {
        return orderRepository.findById(id);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public void delete(Order order) {
        orderRepository.delete(order);
    }
}


