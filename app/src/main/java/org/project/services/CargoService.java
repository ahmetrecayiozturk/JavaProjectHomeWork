
package org.project.services;

import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.Cargo;
import org.project.models.Order;
import org.project.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CargoService {
    private static JsonRepository<Cargo> cargoRepo = new JsonRepository<>(Cargo[].class);
    private static JsonRepository<Product> productRepo = new JsonRepository<>(Product[].class);
    private static JsonRepository<Order> orderRepo = new JsonRepository<>(Order[].class);    public CargoService() {
    }
    public static boolean add(Cargo cargo){
        Cargo cargo1=getCargoById(cargo.getId());
        Cargo cargo2=getCargoByOrderId(cargo.getOrderId());
        if (cargo1 != null||cargo2!=null) {
            return false;
        }
        cargoRepo.save(cargo);
        return true;
    }

    public static void update(Cargo cargo){
        cargoRepo.update(cargo);
    }

    public static void delete(Integer cargoId){
        cargoRepo.delete(cargoId);
    }

    public static Cargo getCargoById(Integer id) {
        List<Cargo> cargos = cargoRepo.findAll();
        for (Cargo cargo : cargos) {
            if (cargo.getId().equals(id)) {
                return cargo;
            }
        }
        return null;
    }
    public static Cargo getCargoByOrderId(Integer orderId) {
        List<Cargo> cargos = cargoRepo.findAll();
        for (Cargo cargo : cargos) {
            if (cargo.getOrderId().equals(orderId)) {
                return cargo;
            }
        }
        return null;
    }
    public static void returnCargo(Integer cargoId) {
        Cargo cargo = getCargoById(cargoId);
        if (cargo != null && !cargo.isReturned()) {
            cargo.setReturned(true);
            update(cargo);

            Order order = orderRepo.findOne(cargo.getOrderId());
            if (order != null) {
                Product product = productRepo.findOne(order.getProductId());
                if (product != null) {
                    product.setProductCount(product.getProductCount() + order.getQuantity());
                    productRepo.update(product);
                }
            }
        }
    }

    public static List<Cargo> getAllCargos() {
        return cargoRepo.findAll();
    }

    public static List<Cargo> getAllCargosForCurrentStore(){
        Integer storeId= App.getCurrentStore().getId();
        List<Cargo> cargos = cargoRepo.findAll();
        List<Cargo> storeCargos = new ArrayList<Cargo>();
        for (Cargo cargo : cargos) {
            if (cargo.getStoreId()!=null && cargo.getStoreId().equals(storeId)) {
                storeCargos.add(cargo);
            }
        }
        return storeCargos;
    }

    public static void isDelivered(){
        getAllCargos().forEach(order -> {
            if(!order.isDelivered()){
                order.setDelivered(true);
            }
        });
    }

    public static void isNotDelivered(){
        getAllCargos().forEach(order -> {
            if(order.isDelivered()){
                order.setDelivered(false);
            }
        });
    }
    public static void markAsDeliveredById(Integer id) {
        Cargo cargo = getCargoById(id);
        if (cargo != null && !cargo.isDelivered()) {
            cargo.setDelivered(true);
            update(cargo);
        }
    }

    public static void markAsNotDeliveredById(Integer id) {
        //id ye göre kargonun bulunması
        Cargo cargo = getCargoById(id);
        //kargoyu null checkten sonta eğer delivered edilmişse notdelivered etme
        if (cargo != null && cargo.isDelivered()) {
            cargo.setDelivered(false);
            update(cargo);
        }
    }

    public static JsonRepository<Cargo> getCargoRepo() {
        return cargoRepo;
    }

    public static void setCargoRepo(JsonRepository<Cargo> cargoRepo) {
        cargoRepo = cargoRepo;
    }

}
