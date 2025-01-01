package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Cargo;
import org.project.models.Order;

import java.util.List;

public class OrderService {
    private JsonRepository<Order> orderRepository=new JsonRepository<>( Order[].class);

    public OrderService() {

    }

    public void add(Order order){
        orderRepository.save(order);
    }

    public void update(Order order){
        orderRepository.update(order);
    }

    public void delete(Integer orderId){
        orderRepository.delete(orderId);
    }

    public Order getOrderById(Integer id) {
        //tüm emirler önce bulunur ve sonra da id'si eşlenen emir döndürülür
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public JsonRepository<Order> getOrderRepository() {
        return orderRepository;
    }

    public void setOrderRepository(JsonRepository<Order> orderRepository) {
        orderRepository = orderRepository;
    }
}
