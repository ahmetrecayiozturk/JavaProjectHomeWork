package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private static JsonRepository<Product> productRepo=new JsonRepository<>( Product[].class);
    public ProductService() {}
    public static boolean add(Product product) {
        Product existingproduct = getProductById(product.getId());
        if (existingproduct != null) {
            return false;
        }
        productRepo.save(product);
        return true;
    }
    public static void delete(Integer productId) {productRepo.delete(productId);}
    public static void update(Product product) {productRepo.update(product);}
    public static Product getProductById(int id) {
        List<Product> products = productRepo.findAll();
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }
    public static List<Product> getAllProducts() {
        return productRepo.findAll();
    }
    public static JsonRepository<Product> getProductRepo() {
        return productRepo;
    }
    public static void setProductRepo(JsonRepository<Product> productRepo) {
        ProductService.productRepo = productRepo;
    }
    public static void updateProduct(Product product) {
        productRepo.update(product);
    }
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
