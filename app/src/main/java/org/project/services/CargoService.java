package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Cargo;

import java.util.List;

public class CargoService {
    private JsonRepository<Cargo> cargoRepo;
    //kargo servisinin constructoru
    public CargoService(JsonRepository<Cargo> cargoRepo) {
        this.cargoRepo = cargoRepo;
    }

    //kargonun eklenmesi
    public void add(Cargo cargo){
        cargoRepo.save(cargo);
    }

    //kargonun update edilmesi
    public void update(Cargo cargo){
        cargoRepo.update(cargo);
    }

    //kargonun silinmesi
    public void delete(Integer cargoId){
        cargoRepo.delete(cargoId);
    }

    //id ye göre kargoların getirilmesi
    public Cargo getCargoById(Integer id) {
        List<Cargo> cargos = cargoRepo.findAll();
        for (Cargo cargo : cargos) {
            if (cargo.getId().equals(id)) {
                return cargo;
            }
        }
        return null;
    }

    //tüm kargoların getirilmesi
    public List<Cargo> getAllCargos() {
        return cargoRepo.findAll();
    }

    //tüm kargoların gönderildi olarak işaretlenmesi
    public void isDelivered(){
        this.getAllCargos().forEach(order -> {
            if(!order.isDelivered()){
                order.setDelivered(true);
            }
        });
    }

    //tüm kargoların yanlış gönderilmesi durumunda hepsini not delivered etme
    public void isNotDelivered(){
        this.getAllCargos().forEach(order -> {
            if(order.isDelivered()){
                order.setDelivered(false);
            }
        });
    }

    //kargoyu null checkten sonta eğer delivered edilmemişse delivered etme
    public void markAsDeliveredById(Integer id) {
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
    public void markAsNotDeliveredById(Integer id) {
        //id ye göre kargonun bulunması
        Cargo cargo = getCargoById(id);
        //kargoyu null checkten sonta eğer delivered edilmişse notdelivered etme
        if (cargo != null && cargo.isDelivered()) {
            cargo.setDelivered(false);
            update(cargo);
        }
    }

    //getter-setter mantıkları
    public JsonRepository<Cargo> getCargoRepo() {
        return cargoRepo;
    }

    //getter-setter mantıkları
    public void setCargoRepo(JsonRepository<Cargo> cargoRepo) {
        this.cargoRepo = cargoRepo;
    }


}
