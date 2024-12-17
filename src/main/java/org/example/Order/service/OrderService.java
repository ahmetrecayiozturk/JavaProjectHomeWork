package org.example.Order.service;

import org.example.Order.model.Order;
import org.example.data.JsonRepository;

import java.util.List;

public class OrderService {
    private JsonRepository<Order> orderRepository;

    public OrderService(JsonRepository<Order> orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addOrder(Order order) {
        orderRepository.save(order);
    }

    public void updateOrder(Order order) {
        orderRepository.update(order);
    }

    public void deleteOrder(String orderId) {
        orderRepository.delete(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}