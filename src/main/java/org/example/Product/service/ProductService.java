package org.example.Product.service;

import org.example.models.Product;
import org.example.data.JsonRepository;

import java.util.List;

public class ProductService {
    private JsonRepository<Product> productRepository;

    public ProductService(JsonRepository<Product> productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void updateProduct(Product product) {
        productRepository.update(product);
    }

    public void deleteProduct(String productId) {
        productRepository.delete(productId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}