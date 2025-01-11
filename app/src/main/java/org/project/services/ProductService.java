package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    //JsonRepository ile Product modelini parametre olarak kullanarak bir JsonRepository nesnesi oluşturuyoruz
    private static final JsonRepository<Product> productRepo=new JsonRepository<>( Product[].class);

    public ProductService() {}
    //Önce productun var olup olmadığı kontrol edilir, kontrol edilme getProductById() methodu ile yapılır,
    //ardondan bu product eğer yok ise eklenir.
    public static boolean add(Product product) {
        Product existingproduct = getProductById(product.getId());
        if (existingproduct != null) {
            return false;
        }
        productRepo.save(product);
        return true;
    }
    //productların silinmesi
    public static void delete(Integer productId) {productRepo.delete(productId);}
    //productların update edilmesi
    public static void update(Product product) {productRepo.update(product);}
    //JsonRepository ile Product modelini parametre olarak kullanarak bir JsonRepository nesnesi oluşturulmuştu,
    //bu oluşturulan nesne ile tüm productslar alınır ve bir listeye atanır, for içerisinde bu liste dönülür,
    //ardından bu liste içindeki productlardan herhangi bir product'un id'si ile parametre olarak girilen id eşlenir ise
    //bu product döndürülür
    public static Product getProductById(int id) {
        List<Product> products = productRepo.findAll();
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }
    //tüm productların döndürülmesi
    public static List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public static void updateProduct(Product product) {
        productRepo.update(product);
    }
    //Product modelini parametre olarak kullanılarak oluşturulmuş JsonRepository nesnesi kullanılarak
    //tüm product'lar döndürülür ve bir listede tutulur, ardından bu product'ların storeid'leri parametre olarak girilen
    //storeId'ye eşit ise bu product bir storeProduct arraylist'ine eklenir ve bu liste döndürülür
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
