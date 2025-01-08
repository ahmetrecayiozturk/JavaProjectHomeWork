package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Store;

import java.util.List;

public class StoreService {
    // Creating a store repository using the JsonRepository class
    private static JsonRepository<Store> storeRepo = new JsonRepository<>(Store[].class);

    public StoreService() {
    }

    // Method to add a new store
    public static boolean add(Store store) {
        // Check if a store with the same name already exists
        Store existingStore = getStoreByName(store.getName());
        if (existingStore != null) {
            // If a store with the same name exists, return false
            return false;
        } else {
            // If no store with the same name exists, save the new store to the repository
            storeRepo.save(store);
            return true;
        }
    }

    // Method to update an existing store
    // This method uses the update functionality provided by the JsonRepository class
    public static void update(Store store) {
        storeRepo.update(store);
    }

    // Method to delete a store by its ID
    // This method uses the delete functionality provided by the JsonRepository class
    public static void delete(Integer storeId) {
        storeRepo.delete(storeId);
    }

    // Method to get a store by its ID
    public static Store getStoreById(int id) {
        List<Store> stores = storeRepo.findAll();
        for (Store store : stores) {
            if (store.getId().equals(id)) {
                return store;
            }
        }
        return null;
    }

    // Method to get a store by its name
    public static Store getStoreByName(String name) {
        List<Store> stores = storeRepo.findAll();
        for (Store store : stores) {
            if (store.getName().equals(name)) {
                return store;
            }
        }
        return null;
    }

    // Method to get all stores
    public static List<Store> getAllStores() {
        return storeRepo.findAll();
    }

    // Method to get the store repository
    public static JsonRepository<Store> getStoreRepo() {
        return storeRepo;
    }

    // Method to set the store repository
    public static void setStoreRepo(JsonRepository<Store> storeRepo) {
        StoreService.storeRepo = storeRepo;
    }
}