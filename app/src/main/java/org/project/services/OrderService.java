package org.project.services;

import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.Order;
import org.project.models.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    // JsonRepository is used for reading, writing, and updating JSON data.
    // A repository that manages data of type Order.
    private static final JsonRepository<Order> orderRepository = new JsonRepository<>(Order[].class);
    // A repository that manages data of type Product.
    private static final JsonRepository<Product> productRepository = new JsonRepository<>(Product[].class);

    // Empty constructor; used to create a default instance of the service class.
    public OrderService() {
    }

    /**
     * Adds a new order to the system.
     * - If an order with the same ID already exists, no action is taken.
     * - Reduces the product's available stock quantity based on the order quantity.
     * - Saves the order to the JSON repository.
     * @param order The order object to be added.
     */
    public static void add(Order order) {
        // Checks if an order with the given ID already exists.
        Order order1 = orderRepository.findOne(order.getId());
        if (order1 != null) {
            return; // If the order already exists, stop the operation.
        }
        // Finds the product specified in the order.
        Product product = productRepository.findOne(order.getProductId());
        if (product != null) {
            // Reduces the product stock based on the order quantity.
            product.setProductCount(product.getProductCount() - order.getQuantity());
            productRepository.update(product); // Updates the stock.
        }
        orderRepository.save(order); // Saves the new order.
    }

    /**
     * Updates an existing order.
     * - Finds the old order by its ID.
     * - If the order quantity has changed, updates the product stock accordingly.
     * - Updates the order in the JSON repository.
     * @param order The order object to be updated.
     */
    public static void update(Order order) {
        // Finds the order to be updated in the existing data.
        Order oldOrder = orderRepository.findOne(order.getId());
        if (oldOrder != null) {
            // Finds the product associated with the order.
            Product product = productRepository.findOne(order.getProductId());
            if (product != null) {
                // Calculates the difference between the old and new order quantities.
                int oldQuantity = oldOrder.getQuantity();
                int newQuantity = order.getQuantity();
                if (newQuantity > oldQuantity) {
                    int difference = newQuantity - oldQuantity; // Finds the increase amount.
                    product.setProductCount(product.getProductCount() - difference);
                } else if (newQuantity < oldQuantity) {
                    int difference = oldQuantity - newQuantity; // Finds the decrease amount.
                    product.setProductCount(product.getProductCount() + difference);
                }
                productRepository.update(product); // Updates the product stock.
            }
        }
        orderRepository.update(order); // Updates the order.
    }

    /**
     * Deletes an order with the specified ID.
     * - Restores the stock of the product associated with the order.
     * - Removes the order from the JSON repository.
     * @param orderId The ID of the order to be deleted.
     */
    public static void delete(Integer orderId) {
        // Finds the order to be deleted.
        Order order = orderRepository.findOne(orderId);
        if (order != null) {
            // Finds the product associated with the order.
            Product product = productRepository.findOne(order.getProductId());
            if (product != null) {
                // Restores the product stock by the order quantity.
                product.setProductCount(product.getProductCount() + order.getQuantity());
                productRepository.update(product); // Saves the updated stock.
            }
            orderRepository.delete(orderId); // Removes the order from the JSON repository.
        }
    }

    /**
     * Retrieves an order with the specified ID.
     * @param id The ID of the order to search for.
     * @return The order with the specified ID, or null if not found.
     */
    public static Order getOrderById(Integer id) {
        List<Order> orders = orderRepository.findAll(); // Retrieves all orders.
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return order; // Returns the order if the ID matches.
            }
        }
        return null; // Returns null if no order matches.
    }

    /**
     * Returns a list of all orders.
     */
    public static List<Order> getAllOrders() {
        return orderRepository.findAll(); // Returns all orders in the JSON repository.
    }

    /**
     * Returns all orders for the current store.
     * - Filters by the current store's ID.
     * @return A list of orders belonging to the current store.
     */
    public static List<Order> getAllOrdersForCurrentStore() {
        // Retrieves the current store's ID.
        Integer storeId = App.getCurrentStore().getId();
        List<Order> orders = orderRepository.findAll(); // Retrieves all orders.

        List<Order> storeOrders = new ArrayList<>();
        // Filters the orders by store ID.
        for (Order order : orders) {
            if (order.getStoreId().equals(storeId)) {
                storeOrders.add(order);
            }
        }
        return storeOrders; // Returns orders belonging to the current store.
    }
}
