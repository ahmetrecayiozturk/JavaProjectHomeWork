package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Product;

import java.util.List;

public class ProductService {
    private JsonRepository<Product> productRepo;
    //product servisi constructoru
    public ProductService(JsonRepository<Product> productRepo) {this.productRepo = productRepo;}
    //product eklenmesi
    public void add(Product product) {productRepo.save(product);}
    //product silinmesi
    public void delete(Integer productId) {productRepo.delete(productId);}
    //product update edilmesi
    public void update(Product product) {productRepo.update(product);}
    //productların idye göre getirilmesi
    public Product getProductById(int id) {
        //önce tüm productları döndürürüz, sonra da id'si eşleneni döndürürüz
        List<Product> products = productRepo.findAll();
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }
    //tüm productların döndürülmesi
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }
    //getter-setter mantığı
    public JsonRepository<Product> getProductRepo() {
        return productRepo;
    }
    //getter-setter mantığı
    public void setProductRepo(JsonRepository<Product> productRepo) {
        this.productRepo = productRepo;
    }
}
