package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Product;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProductService {
    private JsonRepository<Product> productRepo;
    private ImageService imageService;

    // ProductService constructor
    public ProductService(JsonRepository<Product> productRepo, ImageService imageService) {
        this.productRepo = productRepo;
        this.imageService = imageService;
    }

    // Add product
    public void add(Product product, File imageFile) {
        try {
            if (imageFile != null) {
                imageService.saveImage(product, imageFile);
            }
            productRepo.save(product);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Delete product
    public void delete(Integer productId) {
        Product product = getProductById(productId);
        if (product != null) {
            imageService.deleteImage(product);
            productRepo.delete(productId);
        }
    }

    // Update product
    public void update(Product product, File imageFile) {
        try {
            if (imageFile != null) {
                imageService.updateImage(product, imageFile);
            }
            productRepo.update(product);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get product by ID
    public Product getProductById(int id) {
        List<Product> products = productRepo.findAll();
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // Getter and setter for productRepo
    public JsonRepository<Product> getProductRepo() {
        return productRepo;
    }

    public void setProductRepo(JsonRepository<Product> productRepo) {
        this.productRepo = productRepo;
    }
}
/*
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
*/