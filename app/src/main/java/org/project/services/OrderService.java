package org.project.services;

import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.Order;
import org.project.models.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    // JsonRepository, JSON verilerini okuma, yazma ve güncelleme işlemleri için kullanılır.
    // Order türündeki verileri yöneten bir repository.
    private static final JsonRepository<Order> orderRepository = new JsonRepository<>(Order[].class);
    // Product türündeki verileri yöneten bir repository.
    private static final JsonRepository<Product> productRepository = new JsonRepository<>(Product[].class);

    // Boş constructor; hizmet sınıfının varsayılan bir örneğini oluşturmak için kullanılır.
    public OrderService() {
    }

    /**
     * Yeni bir siparişi sisteme ekler.
     * - Eğer aynı ID'ye sahip bir sipariş zaten varsa işlem yapılmaz.
     * - Ürünün mevcut stok miktarını sipariş miktarına göre azaltır.
     * - Siparişi JSON deposuna kaydeder.
     *  order parametresi Eklenecek sipariş nesnesi.
     */
    public static void add(Order order) {
        // Sipariş ID'sine göre mevcut bir sipariş olup olmadığını kontrol eder.
        Order order1 = orderRepository.findOne(order.getId());
        if (order1 != null) {
            return; // Eğer sipariş zaten varsa işlemi durdur.
        }
        // Siparişte belirtilen ürünü bulur.
        Product product = productRepository.findOne(order.getProductId());
        if (product != null) {
            // Ürün stoğunu sipariş miktarına göre azaltır.
            product.setProductCount(product.getProductCount() - order.getQuantity());
            productRepository.update(product); // Stoğu günceller.
        }
        orderRepository.save(order); // Yeni siparişi kaydeder.
    }

    /**
     * Var olan bir siparişi günceller.
     * - Sipariş ID'sine göre eski siparişi bulur.
     * - Eğer sipariş miktarı değişmişse, ürün stoğunu buna göre günceller.
     * - Siparişi JSON deposunda günceller.
     *  order parametresi Güncellenecek sipariş nesnesi.
     */
    public static void update(Order order) {
        // Güncellenmek istenen siparişi mevcut verilerden bulur.
        Order oldOrder = orderRepository.findOne(order.getId());
        if (oldOrder != null) {
            // Siparişin ait olduğu ürünü bulur.
            Product product = productRepository.findOne(order.getProductId());
            if (product != null) {
                // Eski ve yeni sipariş miktarları arasındaki farkı hesaplar.
                int oldQuantity = oldOrder.getQuantity();
                int newQuantity = order.getQuantity();
                if (newQuantity > oldQuantity) {
                    int difference = newQuantity - oldQuantity; // Artış miktarını bulur.
                    product.setProductCount(product.getProductCount() - difference);
                } else if (newQuantity < oldQuantity) {
                    int difference = oldQuantity - newQuantity; // Azalma miktarını bulur.
                    product.setProductCount(product.getProductCount() + difference);
                }
                productRepository.update(product); // Ürün stoğunu günceller.
            }
        }
        orderRepository.update(order); // Siparişi günceller.
    }

    /**
     * Belirtilen ID'ye sahip bir siparişi siler.
     * - Siparişin ait olduğu ürünün stoğunu geri yükler.
     * - Siparişi JSON deposundan kaldırır.
     * orderId parametresi Silinecek siparişin ID'si.
     */
    public static void delete(Integer orderId) {
        // Silinmek istenen siparişi bulur.
        Order order = orderRepository.findOne(orderId);
        if (order != null) {
            // Siparişin ait olduğu ürünü bulur.
            Product product = productRepository.findOne(order.getProductId());
            if (product != null) {
                // Ürün stoğunu sipariş miktarı kadar geri yükler.
                product.setProductCount(product.getProductCount() + order.getQuantity());
                productRepository.update(product); // Güncel stoğu kaydeder.
            }
            orderRepository.delete(orderId); // Siparişi JSON deposundan siler.
        }
    }

    /**
     * Belirtilen ID'ye sahip bir siparişi getirir.
     * id parametresi aranan siparişin ID'si.
     * ID'ye sahip sipariş döndürülür veya bulunamazsa null döndürülür.
     */
    public static Order getOrderById(Integer id) {
        List<Order> orders = orderRepository.findAll(); // Tüm siparişleri getirir.
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return order; // Eğer ID eşleşirse, siparişi döndür.
            }
        }
        return null; // Hiçbir sipariş eşleşmezse null döndür.
    }

    /**
     * Tüm siparişlerin bir listesini döndürür.
     */
    public static List<Order> getAllOrders() {
        return orderRepository.findAll(); // JSON deposundaki tüm siparişleri döndürür.
    }

    /**
     * Geçerli mağazaya ait tüm siparişleri döndürür.
     * - Mevcut mağazanın ID'sine göre filtreleme yapar.
     * eçerli mağazaya ait siparişlerin bir listesini return eder.
     */
    public static List<Order> getAllOrdersForCurrentStore() {
        // Mevcut mağazanın ID'sini alır.
        Integer storeId = App.getCurrentStore().getId();
        List<Order> orders = orderRepository.findAll(); // Tüm siparişleri getirir.

        List<Order> storeOrders = new ArrayList<>();
        // Siparişlerin mağaza ID'sine göre filtrelenmesi.
        for (Order order : orders) {
            if (order.getStoreId().equals(storeId)) {
                storeOrders.add(order);
            }
        }
        return storeOrders; // Geçerli mağazaya ait siparişleri döndürür.
    }
}
