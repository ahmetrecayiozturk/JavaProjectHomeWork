package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Store;

import java.util.List;

public class StoreService {

    private static JsonRepository<Store> storeRepo;

    public StoreService(JsonRepository<Store> storeRepo) {
        this.storeRepo = storeRepo;
    }

    //mağaza eklenmesi
    public boolean add(Store store) {
        //mağaza id'si ile önceden bir kayıt yapılmış mı kontrol edilmesi
        Store existingStore = getStoreByName(store.getName());
        if(existingStore != null){
            return false;
        }
        else{
            storeRepo.save(store);
            return true;
        }
    }

    //store update edilmesi
    public void update(Store store) {
        storeRepo.update(store);
    }

    //store delete edilmesi
    public void delete(Integer storeId){
        storeRepo.delete(storeId);
    }

    //id'ye göre mağazanın getirilmesi
    public Store getStoreById(int id) {
        List<Store> stores = storeRepo.findAll();
        for (Store store : stores) {
            if (store.getId().equals(id)) {
                return store;
            }
        }
        return null;
    }

    //id'ye göre mağazanın getirilmesi
    public Store getStoreByName(String name) {
        List<Store> stores = storeRepo.findAll();
        for (Store store : stores) {
            if (store.getName().equals(name)) {
                return store;
            }
        }
        return null;
    }
    public List<Store> getAllStores(){
        return storeRepo.findAll();
    }

    //getter-setter mantığı
    public JsonRepository<Store> getStoreRepo() {
        return storeRepo;
    }

    //getter-setter mantığı
    public void setStoreRepo(JsonRepository<Store> storeRepo) {
        this.storeRepo = storeRepo;
    }
}