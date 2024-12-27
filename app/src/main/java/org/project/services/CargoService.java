package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Cargo;

import java.util.List;

public class CargoService {
    private static JsonRepository<Cargo> cargoRepo;
    //kargo servisinin constructoru
    public CargoService(JsonRepository<Cargo> cargoRepo) {
        cargoRepo = cargoRepo;
    }

    //kargonun eklenmesi
    public static void add(Cargo cargo){
        cargoRepo.save(cargo);
    }

    //kargonun update edilmesi
    public static void update(Cargo cargo){
        cargoRepo.update(cargo);
    }

    //kargonun silinmesi
    public static void delete(Integer cargoId){
        cargoRepo.delete(cargoId);
    }

    //id ye göre kargoların getirilmesi
    public static Cargo getCargoById(Integer id) {
        List<Cargo> cargos = cargoRepo.findAll();
        for (Cargo cargo : cargos) {
            if (cargo.getId().equals(id)) {
                return cargo;
            }
        }
        return null;
    }

    //tüm kargoların getirilmesi
    public static List<Cargo> getAllCargos() {
        return cargoRepo.findAll();
    }

    //tüm kargoların gönderildi olarak işaretlenmesi
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

    //getter-setter mantıkları
    public static JsonRepository<Cargo> getCargoRepo() {
        return cargoRepo;
    }

    //getter-setter mantıkları
    public static void setCargoRepo(JsonRepository<Cargo> cargoRepo) {
        cargoRepo = cargoRepo;
    }


}
