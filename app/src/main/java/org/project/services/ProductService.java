package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Product;

import java.util.List;

public class ProductService {
    private JsonRepository<Product> productRepo=new JsonRepository<>( Product[].class);
    public ProductService() {}
    public boolean add(Product product) {
        Product existingproduct = getProductById(product.getId());
        if (existingproduct != null) {
            return false;
        }
        productRepo.save(product);
        return true;
    }
    public void delete(Integer productId) {productRepo.delete(productId);}
    public void update(Product product) {productRepo.update(product);}
    public Product getProductById(int id) {
        List<Product> products = productRepo.findAll();
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }
    public JsonRepository<Product> getProductRepo() {
        return productRepo;
    }
    public void setProductRepo(JsonRepository<Product> productRepo) {
        this.productRepo = productRepo;
    }
}
