package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

//burada orderin entity1i receiver ya da store olacaktır, entity2 ise product olacaktır
public class Order<T1,T2> implements Identifiable {
    private Integer id;
    private T1 entity1;
    private T2 entity2;
    public Order(T1 entity1,T2 entity2, Integer id) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.entity1 = entity1;
        this.entity2 = entity2;
    }
    public Integer getId() {return this.id;}
    public void setId(Integer id) {this.id = id;}
    public T1 getEntity1() {
        return entity1;
    }
    public void setEntity1(T1 entity) {
        this.entity1 = entity;
    }
    public T2 getEntity2() {return entity2;}
    public void setEntity2(T2 entity2) {this.entity2 = entity2;}
}
