package org.project.services;

import com.google.gson.Gson;
import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.Order;
import org.project.models.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    // Creating repositories for orders and products using the JsonRepository class
    private static JsonRepository<Order> orderRepository = new JsonRepository<>(Order[].class);
    private static JsonRepository<Product> productRepository = new JsonRepository<>(Product[].class);

    public OrderService() {
    }

    // Method to add a new order
    public static void add(Order order) {
        // Check if an order with the same ID already exists
        Order existingOrder = orderRepository.findOne(order.getId());
        if (existingOrder != null) {
            return;
        }
        // Find the product associated with the order and update its quantity
        Product product = productRepository.findOne(order.getProductId());
        if (product != null) {
            product.setProductCount(product.getProductCount() - order.getQuantity());
            productRepository.update(product);
        }
        // Save the new order to the repository
        orderRepository.save(order);
    }

    // Method to update an existing order
    public static void update(Order order) {
        // Find the old order by its ID
        Order oldOrder = orderRepository.findOne(order.getId());
        if (oldOrder != null) {
            // Find the product associated with the order and adjust its quantity
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
        // Update the order in the repository
        orderRepository.update(order);
    }

    // Method to delete an order by its ID
    public static void delete(Integer orderId) {
        // Find the order by its ID
        Order order = orderRepository.findOne(orderId);
        if (order != null) {
            // Find the product associated with the order and increase its quantity
            Product product = productRepository.findOne(order.getProductId());
            if (product != null) {
                product.setProductCount(product.getProductCount() + order.getQuantity());
                productRepository.update(product);
            }
            // Delete the order from the repository
            orderRepository.delete(orderId);
        }
    }

    // Method to get an order by its ID
    public static Order getOrderById(Integer id) {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    // Method to get all orders (alternative method)
    public static List<Order> getAllOrders2() {
        return orderRepository.findAll();
    }

    // Method to get all orders as a JSON string
    public static String getAllOrdersJson() {
        List<Order> orders = getAllOrders();
        return new Gson().toJson(orders);
    }

    // Method to get all orders
    public static List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Method to get all orders for the current store
    public static List<Order> getAllOrdersForCurrentStore() {
        Integer storeId = App.getCurrentStore().getId();
        List<Order> orders = orderRepository.findAll();

        List<Order> storeOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStoreId() != null && order.getStoreId().equals(storeId)) {
                storeOrders.add(order);
            }
        }
        return storeOrders;
    }

    // Method to get the order repository
    public static JsonRepository<Order> getOrderRepository() {
        return orderRepository;
    }

    // Method to set the order repository
    public static void setOrderRepository(JsonRepository<Order> orderRepository) {
        OrderService.orderRepository = orderRepository;
    }
}