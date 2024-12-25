package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class Order<T1, T2> implements Identifiable {
    private Integer id;
    private T1 entity1;
    private T2 entity2;

    public Order(T1 entity1, T2 entity2) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T1 getEntity1() {
        return entity1;
    }

    public void setEntity1(T1 entity) {
        this.entity1 = entity;
    }

    public T2 getEntity2() {
        return entity2;
    }

    public void setEntity2(T2 entity2) {
        this.entity2 = entity2;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", entity1=" + entity1 +
                ", entity2=" + entity2 +
                '}';
    }
}

/*
package org.project.models;

import org.project.data.Identifiable;
import org.project.models.*;
import java.util.UUID;

public class Order implements Identifiable {
    private Integer id;
    private Receiver entity1;
    private Product entity2;

    public Order(Receiver entity1, Product entity2) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getEntityName() {
        return entity1.getName();
    }

    public void setId(int id) {
        this.id = id;
    }

    public Receiver getEntity1() {
        return entity1;
    }

    public void setEntity1(Receiver entity1) {
        this.entity1 = entity1;
    }

    public Product getEntity2() {
        return entity2;
    }

    public void setEntity2(Product entity2) {
        this.entity2 = entity2;
    }

    public String getEntity1Name() {
        return entity1.getName();
    }

    public String getEntity2Name() {
        return entity2.getName();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", receiver=" + entity1.getName() +
                ", product=" + entity2.getName() +
                '}';
    }
}
 */
/*
package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

//burada orderin entity1i receiver ya da store olacaktır, entity2 ise product olacaktır
public class Order<T1,T2> implements Identifiable {
    private Integer id;
    private T1 entity1;
    private T2 entity2;
    public Order(T1 entity1,T2 entity2) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.entity1 = entity1;
        this.entity2 = entity2;
    }
    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public T1 getEntity1() {
        return entity1;
    }

    public void setEntity1(T1 entity) {
        this.entity1 = entity;
    }

    public T2 getEntity2() {return entity2;}

    public void setEntity2(T2 entity2) {this.entity2 = entity2;}
}
*/