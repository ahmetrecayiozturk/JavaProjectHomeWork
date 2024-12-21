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
        Store existingStore = getStoreById(store.getId());
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
    public Store getStoreById(Integer id) {
        List<Store> stores = storeRepo.findAll();
        for (Store store : stores) {
            if (store.getId().equals(id)) {
                return store;
            }
        }
        return null;
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
/*
package org.project.services;

import org.project.data.Identifiable;
import org.project.data.JsonRepository;
import org.project.models.Store;
import org.project.models.User;
public class StoreService {
    private static JsonRepository<Store> storeRepo;

    public <T extends Identifiable> StoreService(JsonRepository<T> tJsonRepository) {
        this.storeRepo = storeRepo;
    }

    public static void setStoreRepo(JsonRepository<Store> storeRepo) {
        StoreService.storeRepo = storeRepo;
    }

    public static void add(Store store) {
        storeRepo.save(store);
    }

    public static JsonRepository<Store> getStoreRepo() {
        return storeRepo;
    }
}
*/
/*
package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Store;
import org.project.models.User;

public class StoreService {
    private static JsonRepository<Store> storeRepo;
    public StoreService(JsonRepository<Store> storeRepo) {
        this.storeRepo = storeRepo;
    }

    public void add(Store store) {
        storeRepo.save(store);
    }
    public JsonRepository<Store> getStoreRepo() {
        return storeRepo;
    }
    public void setStoreRepo(JsonRepository<Store> storeRepo) {
        this.storeRepo = storeRepo;
    }
}
*/