package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    // Creating a JsonRepository object using the Product model as a parameter.
    private static final JsonRepository<Product> productRepo = new JsonRepository<>(Product[].class);

    public ProductService() {}

    // First, it is checked whether the product exists. The check is performed using the getProductById() method.
    // If the product does not exist, it is added.
    public static boolean add(Product product) {
        Product existingProduct = getProductById(product.getId());
        if (existingProduct != null) {
            return false;
        }
        productRepo.save(product);
        return true;
    }

    // Deleting products.
    public static void delete(Integer productId) { productRepo.delete(productId); }

    // Updating products.
    public static void update(Product product) { productRepo.update(product); }

    // A JsonRepository object was created using the Product model as a parameter.
    // Using this created object, all products are retrieved and assigned to a list. This list is iterated in a for loop,
    // and if the ID of any product in the list matches the ID entered as a parameter, that product is returned.
    public static Product getProductById(int id) {
        List<Product> products = productRepo.findAll();
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    // Returning all products.
    public static List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public static void updateProduct(Product product) {
        productRepo.update(product);
    }

    // Using the JsonRepository object created with the Product model as a parameter,
    // all products are retrieved and assigned to a list. Then, the storeId of each product
    // is compared with the storeId entered as a parameter. If they match, the product is added
    // to a storeProducts ArrayList, and this list is returned.
    public static List<Product> getAllStoreProducts(int storeId) {
        List<Product> products = productRepo.findAll();
        List<Product> storeProducts = new ArrayList<Product>();
        for (Product product : products) {
            if (product.getStoreId() == storeId) {
                storeProducts.add(product);
            }
        }
        return storeProducts;
    }

}
