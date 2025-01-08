package org.project.services;

import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.Cargo;
import org.project.models.Order;
import org.project.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CargoService {
    // Creating repositories for cargos, products, and orders using the JsonRepository class
    private static JsonRepository<Cargo> cargoRepo = new JsonRepository<>(Cargo[].class);
    private static JsonRepository<Product> productRepo = new JsonRepository<>(Product[].class);
    private static JsonRepository<Order> orderRepo = new JsonRepository<>(Order[].class);

    public CargoService() {
    }

    // Method to add a new cargo
    public static boolean add(Cargo cargo) {
        // Check if a cargo with the same ID or order ID already exists
        Cargo existingCargoById = getCargoById(cargo.getId());
        Cargo existingCargoByOrderId = getCargoByOrderId(cargo.getOrderId());
        if (existingCargoById != null || existingCargoByOrderId != null) {
            return false;
        }
        // Save the new cargo to the repository
        cargoRepo.save(cargo);
        return true;
    }

    // Method to update an existing cargo
    public static void update(Cargo cargo) {
        cargoRepo.update(cargo);
    }

    // Method to delete a cargo by its ID
    public static void delete(Integer cargoId) {
        cargoRepo.delete(cargoId);
    }

    // Method to get a cargo by its ID
    public static Cargo getCargoById(Integer id) {
        List<Cargo> cargos = cargoRepo.findAll();
        for (Cargo cargo : cargos) {
            if (cargo.getId().equals(id)) {
                return cargo;
            }
        }
        return null;
    }

    // Method to get a cargo by its order ID
    public static Cargo getCargoByOrderId(Integer orderId) {
        List<Cargo> cargos = cargoRepo.findAll();
        for (Cargo cargo : cargos) {
            if (cargo.getOrderId().equals(orderId)) {
                return cargo;
            }
        }
        return null;
    }

    // Method to return a cargo by its ID
    public static void returnCargo(Integer cargoId) {
        Cargo cargo = getCargoById(cargoId);
        if (cargo != null && !cargo.isReturned()) {
            cargo.setReturned(true);
            update(cargo);

            Order order = orderRepo.findOne(cargo.getOrderId());
            if (order != null) {
                Product product = productRepo.findOne(order.getProductId());
                if (product != null) {
                    product.setProductCount(product.getProductCount() + order.getQuantity());
                    productRepo.update(product);
                }
            }
        }
    }

    // Method to get all cargos
    public static List<Cargo> getAllCargos() {
        return cargoRepo.findAll();
    }

    // Method to get all cargos for the current store
    public static List<Cargo> getAllCargosForCurrentStore() {
        Integer storeId = App.getCurrentStore().getId();
        List<Cargo> cargos = cargoRepo.findAll();
        List<Cargo> storeCargos = new ArrayList<>();
        for (Cargo cargo : cargos) {
            if (cargo.getStoreId() != null && cargo.getStoreId().equals(storeId)) {
                storeCargos.add(cargo);
            }
        }
        return storeCargos;
    }

    // Method to mark all cargos as delivered
    public static void isDelivered() {
        getAllCargos().forEach(cargo -> {
            if (!cargo.isDelivered()) {
                cargo.setDelivered(true);
                update(cargo);
            }
        });
    }

    // Method to mark all cargos as not delivered
    public static void isNotDelivered() {
        getAllCargos().forEach(cargo -> {
            if (cargo.isDelivered()) {
                cargo.setDelivered(false);
                update(cargo);
            }
        });
    }

    // Method to mark a cargo as delivered by its ID
    public static void markAsDeliveredById(Integer id) {
        Cargo cargo = getCargoById(id);
        if (cargo != null && !cargo.isDelivered()) {
            cargo.setDelivered(true);
            update(cargo);
        }
    }

    // Method to mark a cargo as not delivered by its ID
    public static void markAsNotDeliveredById(Integer id) {
        // Find the cargo by its ID
        Cargo cargo = getCargoById(id);
        // If the cargo is found and is delivered, mark it as not delivered
        if (cargo != null && cargo.isDelivered()) {
            cargo.setDelivered(false);
            update(cargo);
        }
    }

    // Method to get the cargo repository
    public static JsonRepository<Cargo> getCargoRepo() {
        return cargoRepo;
    }

    // Method to set the cargo repository
    public static void setCargoRepo(JsonRepository<Cargo> cargoRepo) {
        CargoService.cargoRepo = cargoRepo;
    }
}