package org.project.services;

import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.Cargo;
import org.project.models.Order;
import org.project.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CargoService {
    // Cargo verilerini saklamak için kullanılan bir JSON deposu.
    private static final JsonRepository<Cargo> cargoRepo = new JsonRepository<>(Cargo[].class);
    private static final JsonRepository<Product> productRepo = new JsonRepository<>(Product[].class); // Ürün verileri için JSON deposu.
    private static final JsonRepository<Order> orderRepo = new JsonRepository<>(Order[].class); // Sipariş verileri için JSON deposu.

    // Constructor: Bu sınıfın bir örneği oluşturulduğunda herhangi bir işlem yapmaz.
    public CargoService() {}

    /**
     * Yeni bir kargo ekler.
     * - Eğer aynı ID'ye sahip bir kargo ya da sipariş ID'sine bağlı bir kargo zaten varsa ekleme işlemi başarısız olur.
     *
     * @param cargo Eklenmek istenen kargo nesnesi.
     * @return Ekleme başarılıysa true, aksi takdirde false.
     */
    public static boolean add(Cargo cargo) {
        Cargo cargo1 = getCargoById(cargo.getId()); // ID'ye göre kargo sorgular.
        Cargo cargo2 = getCargoByOrderId(cargo.getOrderId()); // Sipariş ID'sine göre kargo sorgular.
        if (cargo1 != null || cargo2 != null) {
            return false; // Eğer kargo zaten mevcutsa false döndürür.
        }
        cargoRepo.save(cargo); // Kargo bilgilerini JSON deposuna kaydeder.
        return true; // Ekleme başarılı.
    }

    /**
     * Mevcut bir kargo bilgisini günceller.
     *
     * @param cargo Güncellenmek istenen kargo nesnesi.
     */
    public static void update(Cargo cargo) {
        cargoRepo.update(cargo); // Kargo bilgilerini günceller.
    }

    /**
     * Verilen ID'ye sahip kargoyu siler.
     *
     * @param cargoId Silinmek istenen kargonun ID'si.
     */
    public static void delete(Integer cargoId) {
        cargoRepo.delete(cargoId); // Belirtilen ID'ye sahip kargoyu JSON deposundan siler.
    }

    /**
     * Belirtilen ID'ye sahip kargo bilgisini döndürür.
     *
     * @param id Aranacak kargonun ID'si.
     * @return Kargo nesnesi ya da bulunamazsa null.
     */
    public static Cargo getCargoById(Integer id) {
        List<Cargo> cargos = cargoRepo.findAll(); // Tüm kargoları JSON deposundan çeker.
        for (Cargo cargo : cargos) {
            if (cargo.getId().equals(id)) {
                return cargo; // ID'si eşleşen kargoyu döndürür.
            }
        }
        return null; // Bulunamazsa null döndürür.
    }

    /**
     * Belirtilen sipariş ID'sine bağlı kargo bilgisini döndürür.
     *
     * @param orderId Aranacak siparişin ID'si.
     * @return Kargo nesnesi ya da bulunamazsa null.
     */
    public static Cargo getCargoByOrderId(Integer orderId) {
        List<Cargo> cargos = cargoRepo.findAll(); // Tüm kargoları JSON deposundan çeker.
        for (Cargo cargo : cargos) {
            if (cargo.getOrderId().equals(orderId)) {
                return cargo; // Sipariş ID'si eşleşen kargoyu döndürür.
            }
        }
        return null; // Bulunamazsa null döndürür.
    }

    /**
     * Belirtilen kargoyu "iade edildi" olarak işaretler ve ürün stoğunu günceller.
     *
     * @param cargoId İade edilecek kargonun ID'si.
     */
    public static void returnCargo(Integer cargoId) {
        Cargo cargo = getCargoById(cargoId); // Kargo bilgilerini ID'ye göre alır.
        if (cargo != null && !cargo.isReturned()) { // Eğer kargo iade edilmemişse işlem yapar.
            cargo.setReturned(true); // Kargoyu iade edildi olarak işaretler.
            update(cargo); // Güncel kargo bilgilerini kaydeder.

            Order order = orderRepo.findOne(cargo.getOrderId()); // İlgili siparişi bulur.
            if (order != null) {
                Product product = productRepo.findOne(order.getProductId()); // Siparişle ilişkili ürünü bulur.
                if (product != null) {
                    product.setProductCount(product.getProductCount() + order.getQuantity()); // Ürün stoğunu iade edilen miktar kadar artırır.
                    productRepo.update(product); // Güncellenen ürün bilgisini kaydeder.
                }
            }
        }
    }

    /**
     * Tüm kargoları döndürür.
     *
     * @return Tüm kargoların listesi.
     */
    public static List<Cargo> getAllCargos() {
        return cargoRepo.findAll(); // Tüm kargoları JSON deposundan çeker ve döndürür.
    }

    /**
     * Mevcut mağaza için kargoları döndürür.
     *
     * @return Mevcut mağaza ile ilişkilendirilmiş kargoların listesi.
     */
    public static List<Cargo> getAllCargosForCurrentStore() {
        Integer storeId = App.getCurrentStore().getId(); // Mevcut mağazanın ID'sini alır.
        List<Cargo> cargos = cargoRepo.findAll(); // Tüm kargoları JSON deposundan çeker.
        List<Cargo> storeCargos = new ArrayList<>(); // Mağazaya ait kargoları saklamak için bir liste oluşturur.
        for (Cargo cargo : cargos) {
            if (cargo.getStoreId() != null && cargo.getStoreId().equals(storeId)) {
                storeCargos.add(cargo); // Eğer kargo mağaza ID'siyle eşleşiyorsa listeye ekler.
            }
        }
        return storeCargos; // Mevcut mağaza ile ilişkilendirilmiş kargoları döndürür.
    }

    /**
     * Tüm kargoları "teslim edildi" olarak işaretler.
     */
    public static void isDelivered() {
        getAllCargos().forEach(order -> {
            if (!order.isDelivered()) {
                order.setDelivered(true); // Teslim edilmemiş kargoları teslim edildi olarak işaretler.
            }
        });
    }

    /**
     * Tüm kargoları "teslim edilmedi" olarak işaretler.
     */
    public static void isNotDelivered() {
        getAllCargos().forEach(order -> {
            if (order.isDelivered()) {
                order.setDelivered(false); // Teslim edilmiş kargoları teslim edilmedi olarak işaretler.
            }
        });
    }

    /**
     * Belirtilen ID'ye sahip kargoyu "teslim edildi" olarak işaretler.
     *
     * @param id Teslim edilecek kargonun ID'si.
     */
    public static void markAsDeliveredById(Integer id) {
        Cargo cargo = getCargoById(id); // Kargo bilgilerini ID'ye göre alır.
        if (cargo != null && !cargo.isDelivered()) { // Eğer kargo teslim edilmemişse işlem yapar.
            cargo.setDelivered(true); // Kargoyu teslim edildi olarak işaretler.
            update(cargo); // Güncel bilgileri kaydeder.
        }
    }

    /**
     * Belirtilen ID'ye sahip kargoyu "teslim edilmedi" olarak işaretler.
     *
     * @param id Teslim edilmemiş olarak işaretlenecek kargonun ID'si.
     */
    public static void markAsNotDeliveredById(Integer id) {
        Cargo cargo = getCargoById(id); // Kargo bilgilerini ID'ye göre alır.
        if (cargo != null && cargo.isDelivered()) { // Eğer kargo teslim edilmişse işlem yapar.
            cargo.setDelivered(false); // Kargoyu teslim edilmedi olarak işaretler.
            update(cargo); // Güncel bilgileri kaydeder.
        }
    }
}
