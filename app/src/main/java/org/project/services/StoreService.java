package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Store;

import java.util.List;

public class StoreService {

    private JsonRepository<Store> storeRepo=new JsonRepository<>( Store[].class);

    public StoreService() {
    }

    public boolean add(Store store) {
        Store existingStore = getStoreByName(store.getName());
        if(existingStore != null){
            return false;
        }
        else{
            storeRepo.save(store);
            return true;
        }
    }

    public void update(Store store) {
        storeRepo.update(store);
    }

    public void delete(Integer storeId){
        storeRepo.delete(storeId);
    }

    public Store getStoreById(int id) {
        List<Store> stores = storeRepo.findAll();
        for (Store store : stores) {
            if (store.getId().equals(id)) {
                return store;
            }
        }
        return null;
    }

    public  Store getStoreByName(String name) {
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

    public JsonRepository<Store> getStoreRepo() {
        return storeRepo;
    }

    public void setStoreRepo(JsonRepository<Store> storeRepo) {
        storeRepo = storeRepo;
    }

}