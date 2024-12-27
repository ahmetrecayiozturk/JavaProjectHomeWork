package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Product;

import java.util.List;

public class ProductService {
    private static JsonRepository<Product> productRepo;
    public ProductService(JsonRepository<Product> productRepo) {this.productRepo = productRepo;}
    public static boolean add(Product product) {
        Product existingproduct = getProductById(product.getId());
        if (existingproduct != null) {
            return false;
        }
        productRepo.save(product);
        return true;
    }
    public static void delete(Integer productId) {productRepo.delete(productId);}
    public void update(Product product) {productRepo.update(product);}
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
    public JsonRepository<Product> getProductRepo() {
        return productRepo;
    }
    public void setProductRepo(JsonRepository<Product> productRepo) {
        this.productRepo = productRepo;
    }
}
