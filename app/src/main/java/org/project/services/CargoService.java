package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Cargo;
import org.project.models.Order;
import org.project.models.Product;

import java.util.List;

public class CargoService {
    private static JsonRepository<Cargo> cargoRepo = new JsonRepository<>(Cargo[].class);
    private static JsonRepository<Product> productRepo = new JsonRepository<>(Product[].class);
    private static JsonRepository<Order> orderRepo = new JsonRepository<>(Order[].class);

    public CargoService() {
    }

    public static void add(Cargo cargo) {
        cargoRepo.save(cargo);
    }

    public static void update(Cargo cargo) {
        cargoRepo.update(cargo);
    }

    public static void delete(Integer cargoId) {
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

    public static List<Cargo> getAllCargos() {
        return cargoRepo.findAll();
    }

    public static void isDelivered() {
        getAllCargos().forEach(order -> {
            if (!order.isDelivered()) {
                order.setDelivered(true);
                update(order);
            }
        });
    }

    public static void isNotDelivered() {
        getAllCargos().forEach(order -> {
            if (order.isDelivered()) {
                order.setDelivered(false);
                update(order);
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
        Cargo cargo = getCargoById(id);
        if (cargo != null && cargo.isDelivered()) {
            cargo.setDelivered(false);
            update(cargo);
        }
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
}
/*
package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Cargo;

import java.util.List;

public class CargoService {
    private static JsonRepository<Cargo> cargoRepo=new JsonRepository<>( Cargo[].class);
    public CargoService() {
    }

    public static void add(Cargo cargo){
        cargoRepo.save(cargo);
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


    public static List<Cargo> getAllCargos() {
        return cargoRepo.findAll();
    }

    public static void isDelivered(){
        getAllCargos().forEach(order -> {
            if(!order.isDelivered()){
                order.setDelivered(true);
            }
        });
    }

    //tüm kargoların yanlış gönderilmesi durumunda hepsini not delivered etme
    public static void isNotDelivered(){
        getAllCargos().forEach(order -> {
            if(order.isDelivered()){
                order.setDelivered(false);
            }
        });
    }

    //kargoyu null checkten sonta eğer delivered edilmemişse delivered etme
    public static void markAsDeliveredById(Integer id) {
        //id ye göre kargonun bulunması
        Cargo cargo = getCargoById(id);
        //kargoyu null checkten sonta eğer delivered edilmemişse delivered etme
        if (cargo != null && !cargo.isDelivered()) {
            cargo.setDelivered(true);
            update(cargo);
        }
    }

    //kargoyu null checkten sonta eğer delivered edilmişse notdelivered etme, aksaklık olursa ya da kargonun
    //yanlış teslimi olaylarında kullanılır
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
*/