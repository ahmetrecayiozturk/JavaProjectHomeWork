package org.project.models;

import org.project.App;
import org.project.models.interfaces.Identifiable;
import java.util.UUID;

public abstract class StoreItem implements Identifiable {
    protected Integer id;
    protected Integer storeId;
    protected StoreItem() {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.storeId = App.getCurrentStore().getId();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }
} 