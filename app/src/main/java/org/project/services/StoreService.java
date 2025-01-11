package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Store;

import java.util.List;

public class StoreService {

    private static final JsonRepository<Store> storeRepo=new JsonRepository<>( Store[].class);

    public StoreService() {
    }

    public static boolean add(Store store) {
        Store existingStore = getStoreByName(store.getName());
        if(existingStore != null){
            return false;
        }
        else{
            storeRepo.save(store);
            return true;
        }
    }

    public static void update(Store store) {
        storeRepo.update(store);
    }

    public static void delete(Integer storeId){
        storeRepo.delete(storeId);
    }
    //Store parametresi kullanılarak JsonRepository nesnesi oluşturulur, bu oluşturulmuş nesne kullanılarak tüm
    //store'ler bir listede tutulur ve bu liste for ile dönülür, bu liste içerisindeki herhangi bir store'nin id'si ile
    //methodda girilen id parametresi eşleniyorsa o store döndürülür
    public static Store getStoreById(int id) {
        List<Store> stores = storeRepo.findAll();
        for (Store store : stores) {
            if (store.getId().equals(id)) {
                return store;
            }
        }
        return null;
    }

    public static Store getStoreByName(String name) {
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
}