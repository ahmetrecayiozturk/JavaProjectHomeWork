package org.project.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.data.JsonRepository;
import org.project.models.Order;
import org.project.models.Product;
import org.project.models.Receiver;

import java.lang.reflect.Type;
import java.util.List;

public class OrderService {
    private static JsonRepository<Order> orderRepository = new JsonRepository<>(Order[].class);
    private static JsonRepository<Product> productRepository = new JsonRepository<>(Product[].class);

    public OrderService() {
    }

    public void add(Order<Receiver, Product> order) {
        // Save the order
        orderRepository.save(order);

        // Find the product and decrease its quantity
        Product product = productRepository.findOne(order.getEntity2().getId());
        if (product != null) {
            product.setProductCount(product.getProductCount() - order.getQuantity());
            productRepository.update(product);
        }
    }

    public static void update(Order<Receiver, Product> order) {
        // Find the old order
        Order<Receiver, Product> oldOrder = orderRepository.findOne(order.getId());
        if (oldOrder != null) {
            // Find the product and adjust its quantity
            Product product = productRepository.findOne(order.getEntity2().getId());
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

        // Update the order
        orderRepository.update(order);
    }

    public static void delete(Integer orderId) {
        // Find the order
        Order<Receiver, Product> order = orderRepository.findOne(orderId);
        if (order != null) {
            // Find the product and increase its quantity
            Product product = productRepository.findOne(order.getEntity2().getId());
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
        Type orderListType = new TypeToken<List<Order>>() {}.getType();
        return new Gson().fromJson(orderRepository.findAllJson(), orderListType);
    }

    public static JsonRepository<Order> getOrderRepository() {
        return orderRepository;
    }

    public static void setOrderRepository(JsonRepository<Order> orderRepository) {
        OrderService.orderRepository = orderRepository;
    }
}
/*
package org.project.services;

import com.google.gson.Gson;
import org.project.data.JsonRepository;
import org.project.models.Order;
import org.project.models.Product;
import org.project.models.Receiver;

import java.util.List;

public class OrderService {
    private static JsonRepository<Order> orderRepository = new JsonRepository<>(Order[].class);
    private static JsonRepository<Product> productRepository = new JsonRepository<>(Product[].class);
    public OrderService() {
    }

    public static void add1(Order order) {
        orderRepository.save(order);
    }
    public void add(Order<Receiver, Product> order) {
        // Save the order
        orderRepository.save(order);

        // Find the product and decrease its quantity
        Product product = productRepository.findOne(order.getEntity2().getId());
        if (product != null) {
            product.setProductCount(product.getProductCount() - order.getQuantity());
            productRepository.update(product);
        }
    }

    public static void update(Order order) {
        orderRepository.update(order);
    }

    public static void delete(Integer orderId) {
        orderRepository.delete(orderId);
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

    public static List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public static String getAllOrdersJson() {
        List<Order> orders = getAllOrders();
        return new Gson().toJson(orders);
    }

    public static JsonRepository<Order> getOrderRepository() {
        return orderRepository;
    }

    public static void setOrderRepository(JsonRepository<Order> orderRepository) {
        OrderService.orderRepository = orderRepository;
    }
}
*/