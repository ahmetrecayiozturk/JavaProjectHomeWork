package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Cargo;
import org.project.models.Order;

import java.util.List;

public class OrderService {
    private JsonRepository<Order> orderRepository;
    public OrderService(JsonRepository<Order> orderRepository) {
        this.orderRepository = orderRepository;
    }
    //emir eklenmesi
    public void addOrder(Order order){
        orderRepository.save(order);
    }
    //emir update edilmesi
    public void updateOrder(Order order){
        orderRepository.update(order);
    }
    //emir silinmesi
    public void deleteOrder(Integer orderId){
        orderRepository.delete(orderId);
    }
    //id'ye göre emir getirme
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
    //tüm emirlerin döndürülmesi
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    //getter-setter mantıkları
    public JsonRepository<Order> getOrderRepository() {
        return orderRepository;
    }
    //getter-setter mantıkları
    public void setOrderRepository(JsonRepository<Order> orderRepository) {
        this.orderRepository = orderRepository;
    }
}
