package org.project.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.data.JsonRepository;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.models.Cargo;

import java.lang.reflect.Type;
import java.util.List;

public class OrderService {
    private static JsonRepository<Order> orderRepo = new JsonRepository<>(Order[].class);
    private static JsonRepository<Product> productRepo = new JsonRepository<>(Product[].class);
    private static JsonRepository<Cargo> cargoRepo = new JsonRepository<>(Cargo[].class);
    private static Gson gson = new Gson();

    public OrderService() {
    }

    public static void add(Order order) {
        orderRepo.save(order);
    }

    public static void update(Order order) {
        orderRepo.update(order);
    }

    public static void delete(Integer orderId) {
        // Find the order
        Order order = orderRepo.findOne(orderId);
        if (order != null) {
            // Find the product and increase its quantity
            Product product = productRepo.findOne(order.getProductId());
            if (product != null) {
                product.setProductCount(product.getProductCount() + order.getQuantity());
                productRepo.update(product);
            }

            // Delete the order
            orderRepo.delete(orderId);
        }
    }

    public static void sendToCargo(Integer orderId) {
        // Find the order
        Order order = orderRepo.findOne(orderId);
        if (order != null) {
            // Create a new cargo entry
            Cargo cargo = new Cargo(false,false, orderId);
            cargoRepo.save(cargo);

            // Update the order status
            order.setStatus("Kargoya Verildi");
            orderRepo.update(order);
        }
    }

    public static Order getOrderById(Integer id) {
        List<Order> orders = getAllOrders();
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    public static List<Order> getAllOrders() {
        Type orderListType = new TypeToken<List<Order>>() {}.getType();
        return gson.fromJson(orderRepo.findAllJson(), orderListType);
    }

    public static String getAllOrdersJson() {
        return orderRepo.findAllJson();
    }
}
/*
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

    public static void add(Order order) {
        // Save the order
        orderRepository.save(order);

        Product product = productRepository.findOne(order.getProductId());
        if (product != null) {
            product.setProductCount(product.getProductCount() - order.getQuantity());
            productRepository.update(product);
        }
    }

    public static void update(Order order) {
        // Find the old order
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

        // Update the order
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
*/