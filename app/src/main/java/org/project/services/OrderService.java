package org.project.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.Order;
import org.project.models.Product;
import org.project.models.Receiver;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private static JsonRepository<Order> orderRepository = new JsonRepository<>(Order[].class);
    private static JsonRepository<Product> productRepository = new JsonRepository<>(Product[].class);

    public OrderService() {
    }

    public static void add(Order order) {
        Order order1= orderRepository.findOne(order.getId());
        if(order1!=null){
            return;
        }
        Product product = productRepository.findOne(order.getProductId());
        if (product != null) {
            product.setProductCount(product.getProductCount() - order.getQuantity());
            productRepository.update(product);
        }
        orderRepository.save(order);
    }

    public static void update(Order order) {
        Order oldOrder = orderRepository.findOne(order.getId());
        if (oldOrder != null) {
            // Find the product and adjust its quantity
            Product product = productRepository.findOne(order.getProductId());
            if (product != null) {
                int oldQuantity = oldOrder.getQuantity();
                int newQuantity = order.getQuantity();
                if (newQuantity > oldQuantity) {
                    int difference = newQuantity - oldQuantity;
                    product.setProductCount(product.getProductCount() - difference);
                } else if (newQuantity < oldQuantity) {
                    int difference = oldQuantity - newQuantity;
                    product.setProductCount(product.getProductCount() + difference);
                }
                productRepository.update(product);
            }
        }

        orderRepository.update(order);
    }

    public static void delete(Integer orderId) {
        // Find the order
        Order order = orderRepository.findOne(orderId);
        if (order != null) {
            // Find the product and increase its quantity
            Product product = productRepository.findOne(order.getProductId());
            if (product != null) {
                product.setProductCount(product.getProductCount() + order.getQuantity());
                productRepository.update(product);
            }

            // Delete the order
            orderRepository.delete(orderId);
        }
    }

    public static Order getOrderById(Integer id) {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    public static List<Order> getAllOrders2() {
        return orderRepository.findAll();
    }

    public static String getAllOrdersJson() {
        List<Order> orders = getAllOrders();
        return new Gson().toJson(orders);
    }

    public static List<Order> getAllOrders() {
       return orderRepository.findAll();
    }

    public static List<Order> getAllOrdersForCurrentStore() {
        Integer storeId = App.getCurrentStore().getId();
        List<Order> orders = orderRepository.findAll();

        List<Order> storeOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStoreId()!= null && order.getStoreId().equals(storeId)) {
                storeOrders.add(order);
            }
        }
        return storeOrders;
    }

    public static JsonRepository<Order> getOrderRepository() {
        return orderRepository;
    }

    public static void setOrderRepository(JsonRepository<Order> orderRepository) {
        OrderService.orderRepository = orderRepository;
    }
}