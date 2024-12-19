package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Store;
import org.project.models.User;

public class StoreService {
    public JsonRepository<Store> storeRepo;
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
