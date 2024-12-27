package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Cargo;
import org.project.models.Order;

import java.util.List;

public class OrderService {
    private static  JsonRepository<Order> orderRepository;

    public OrderService(JsonRepository<Order> orderRepository) {
        this.orderRepository = orderRepository;
    }

    //emir eklenmesi
    public static void add(Order order){
        orderRepository.save(order);
    }

    //emir update edilmesi
    public static void update(Order order){
        orderRepository.update(order);
    }

    //emir silinmesi
    public static void delete(Integer orderId){
        orderRepository.delete(orderId);
    }

    //id'ye göre emir getirme
    public static Order getOrderById(Integer id) {
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
    public static List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    //getter-setter mantıkları
    public static JsonRepository<Order> getOrderRepository() {
        return orderRepository;
    }

    //getter-setter mantıkları
    public static void setOrderRepository(JsonRepository<Order> orderRepository) {
        orderRepository = orderRepository;
    }
}
