package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    // Creating a product repository using the JsonRepository class
    private static JsonRepository<Product> productRepo = new JsonRepository<>(Product[].class);

    public ProductService() {}

    // Method to add a new product
    public static boolean add(Product product) {
        // Check if a product with the same ID already exists
        Product existingProduct = getProductById(product.getId());
        if (existingProduct != null) {
            // If a product with the same ID exists, return false
            return false;
        }
        // If no product with the same ID exists, save the new product to the repository
        productRepo.save(product);
        return true;
    }

    // Method to delete a product by its ID
    // This method uses the delete functionality provided by the JsonRepository class
    public static void delete(Integer productId) {
        productRepo.delete(productId);
    }

    // Method to update an existing product
    // This method uses the update functionality provided by the JsonRepository class
    public static void update(Product product) {
        productRepo.update(product);
    }

    // Method to get a product by its ID
    public static Product getProductById(int id) {
        List<Product> products = productRepo.findAll();
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    // Method to get all products
    public static List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // Method to get the product repository
    public static JsonRepository<Product> getProductRepo() {
        return productRepo;
    }

    // Method to set the product repository
    public static void setProductRepo(JsonRepository<Product> productRepo) {
        ProductService.productRepo = productRepo;
    }

    // Method to update a product
    // This method uses the update functionality provided by the JsonRepository class
    public static void updateProduct(Product product) {
        productRepo.update(product);
    }

    // Method to get all products for a specific store by store ID
    public static List<Product> getAllStoreProducts(int storeId) {
        List<Product> products = productRepo.findAll();
        List<Product> storeProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getStoreId() == storeId) {
                storeProducts.add(product);
            }
        }
        return storeProducts;
    }
}