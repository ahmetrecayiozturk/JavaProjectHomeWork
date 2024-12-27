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
    public static boolean add(Store store) {
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
    public static void update(Store store) {
        storeRepo.update(store);
    }

    //store delete edilmesi
    public static void delete(Integer storeId){
        storeRepo.delete(storeId);
    }

    //id'ye göre mağazanın getirilmesi
    public static Store getStoreById(int id) {
        List<Store> stores = storeRepo.findAll();
        for (Store store : stores) {
            if (store.getId().equals(id)) {
                return store;
            }
        }
        return null;
    }

    //id'ye göre mağazanın getirilmesi
    public static  Store getStoreByName(String name) {
        List<Store> stores = storeRepo.findAll();
        for (Store store : stores) {
            if (store.getName().equals(name)) {
                return store;
            }
        }
        return null;
    }
    public static List<Store> getAllStores(){
        return storeRepo.findAll();
    }

    //getter-setter mantığı
    public static JsonRepository<Store> getStoreRepo() {
        return storeRepo;
    }

    //getter-setter mantığı
    public static void setStoreRepo(JsonRepository<Store> storeRepo) {
        storeRepo = storeRepo;
    }
}