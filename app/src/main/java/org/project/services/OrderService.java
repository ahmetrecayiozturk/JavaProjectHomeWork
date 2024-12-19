package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Order;

public class OrderService {
    public JsonRepository<Order> orderRepository;
    public OrderService(JsonRepository<Order> orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addOrder(Order order){
        orderRepository.save(order);
    }
    public void updateOrder(Order order){
        orderRepository.update(order);
    }
    public void deleteOrder(Integer orderId){
        orderRepository.delete(orderId);
    }
}
