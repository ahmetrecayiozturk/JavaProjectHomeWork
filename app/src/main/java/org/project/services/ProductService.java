package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Product;

public class ProductService {
    public JsonRepository<Product> productRepo;
    public ProductService(JsonRepository<Product> productRepo) {
        this.productRepo = productRepo;
    }
}
