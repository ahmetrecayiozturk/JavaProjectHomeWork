package org.project.services;

import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.Cargo;
import org.project.models.Order;
import org.project.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CargoService {
    // A JSON repository used to store cargo data.
    private static final JsonRepository<Cargo> cargoRepo = new JsonRepository<>(Cargo[].class);
    private static final JsonRepository<Product> productRepo = new JsonRepository<>(Product[].class); // JSON repository for product data.
    private static final JsonRepository<Order> orderRepo = new JsonRepository<>(Order[].class); // JSON repository for order data.

    // Constructor: Does not perform any action when an instance of this class is created.
    public CargoService() {}

    /**
     * Adds a new cargo.
     * - If a cargo with the same ID or a cargo linked to an order ID already exists, the addition fails.
     * cargo parameter: the cargo object to be added.
     * Returns true if addition is successful, otherwise false.
     */
    public static boolean add(Cargo cargo) {
        Cargo cargo1 = getCargoById(cargo.getId()); // Queries the cargo by ID.
        Cargo cargo2 = getCargoByOrderId(cargo.getOrderId()); // Queries the cargo by order ID.
        if (cargo1 != null || cargo2 != null) {
            return false; // Returns false if cargo already exists.
        }
        cargoRepo.save(cargo); // Saves the cargo data to the JSON repository.
        return true; // Addition is successful.
    }

    /**
     * Updates an existing cargo.
     * @param cargo The cargo object to be updated.
     */
    public static void update(Cargo cargo) {
        cargoRepo.update(cargo); // Updates the cargo data.
    }

    /**
     * Deletes a cargo by the given ID.
     *
     * @param cargoId The ID of the cargo to be deleted.
     */
    public static void delete(Integer cargoId) {
        cargoRepo.delete(cargoId); // Deletes the cargo with the given ID from the JSON repository.
    }

    /**
     * Returns the cargo data for the given ID.
     *
     * @param id The ID of the cargo to be searched.
     * @return Cargo object or null if not found.
     */
    public static Cargo getCargoById(Integer id) {
        List<Cargo> cargos = cargoRepo.findAll(); // Retrieves all cargos from the JSON repository.
        for (Cargo cargo : cargos) {
            if (cargo.getId().equals(id)) {
                return cargo; // Returns the cargo with the matching ID.
            }
        }
        return null; // Returns null if not found.
    }

    /**
     * Returns the cargo data for the given order ID.
     *
     * @param orderId The ID of the order to be searched.
     * @return Cargo object or null if not found.
     */
    public static Cargo getCargoByOrderId(Integer orderId) {
        List<Cargo> cargos = cargoRepo.findAll(); // Retrieves all cargos from the JSON repository.
        for (Cargo cargo : cargos) {
            if (cargo.getOrderId().equals(orderId)) {
                return cargo; // Returns the cargo with the matching order ID.
            }
        }
        return null; // Returns null if not found.
    }

    /**
     * Marks the given cargo as "returned" and updates the product stock.
     *
     * @param cargoId The ID of the cargo to be returned.
     */
    public static void returnCargo(Integer cargoId) {
        Cargo cargo = getCargoById(cargoId); // Retrieves cargo by ID.
        if (cargo != null && !cargo.isReturned()) { // If the cargo has not been returned, process the return.
            cargo.setReturned(true); // Marks the cargo as returned.
            update(cargo); // Saves the updated cargo data.

            Order order = orderRepo.findOne(cargo.getOrderId()); // Finds the related order.
            if (order != null) {
                Product product = productRepo.findOne(order.getProductId()); // Finds the related product.
                if (product != null) {
                    product.setProductCount(product.getProductCount() + order.getQuantity()); // Increases the product stock by the returned quantity.
                    productRepo.update(product); // Saves the updated product data.
                }
            }
        }
    }

    /**
     * Returns all cargos.
     *
     * @return A list of all cargos.
     */
    public static List<Cargo> getAllCargos() {
        return cargoRepo.findAll(); // Retrieves all cargos from the JSON repository and returns them.
    }

    /**
     * Returns cargos for the current store.
     *
     * @return A list of cargos associated with the current store.
     */
    public static List<Cargo> getAllCargosForCurrentStore() {
        Integer storeId = App.getCurrentStore().getId(); // Retrieves the ID of the current store.
        List<Cargo> cargos = cargoRepo.findAll(); // Retrieves all cargos from the JSON repository.
        List<Cargo> storeCargos = new ArrayList<>(); // Creates a list to store cargos associated with the store.
        for (Cargo cargo : cargos) {
            if (cargo.getStoreId() != null && cargo.getStoreId().equals(storeId)) {
                storeCargos.add(cargo); // Adds cargos with a matching store ID to the list.
            }
        }
        return storeCargos; // Returns cargos associated with the current store.
    }

    /**
     * Marks all cargos as "delivered".
     */
    public static void isDelivered() {
        getAllCargos().forEach(order -> {
            if (!order.isDelivered()) {
                order.setDelivered(true); // Marks undelivered cargos as delivered.
            }
        });
    }

    /**
     * Marks all cargos as "not delivered".
     */
    public static void isNotDelivered() {
        getAllCargos().forEach(order -> {
            if (order.isDelivered()) {
                order.setDelivered(false); // Marks delivered cargos as not delivered.
            }
        });
    }

    /**
     * Marks the cargo with the given ID as "delivered".
     *
     * @param id The ID of the cargo to be marked as delivered.
     */
    public static void markAsDeliveredById(Integer id) {
        Cargo cargo = getCargoById(id); // Retrieves cargo by ID.
        if (cargo != null && !cargo.isDelivered()) { // If the cargo has not been delivered, process the delivery.
            cargo.setDelivered(true); // Marks the cargo as delivered.
            update(cargo); // Saves the updated cargo data.
        }
    }

    /**
     * Marks the cargo with the given ID as "not delivered".
     *
     * @param id The ID of the cargo to be marked as not delivered.
     */
    public static void markAsNotDeliveredById(Integer id) {
        Cargo cargo = getCargoById(id); // Retrieves cargo by ID.
        if (cargo != null && cargo.isDelivered()) { // If the cargo has been delivered, process the change.
            cargo.setDelivered(false); // Marks the cargo as not delivered.
            update(cargo); // Saves the updated cargo data.
        }
    }
}
