package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Receiver;

import java.util.List;

public class ReceiverService {

    private JsonRepository<Receiver> receiverRepo;
    //receiver servisinin constructoru
    public ReceiverService(JsonRepository<Receiver> receiverRepo) {
        this.receiverRepo = receiverRepo;
    }
    //receiver eklenmesi
    public boolean addReceiver(Receiver receiver) {
        List<Receiver> receivers = receiverRepo.findAll();
        if(receivers.contains(receiver)){
            return false;
        }
        else{
            receiverRepo.save(receiver);
            return true;
        }
    }
    //farklı bir şekilde receiver ekleme
    public boolean addReceiverDifferen(Receiver receiver) {
        Receiver existingReceiver = getReceiverById(receiver.getId());
        if(existingReceiver != null){
            return false;
        }
        else{
            receiverRepo.save(receiver);
            return true;
        }
    }
    //receiver güncellenmesi
    public void updateReceiver(Receiver receiver) {
        receiverRepo.update(receiver);
    }
    //receiver silinmesi
    public void deleteReceiver(Integer receiverId) {
        receiverRepo.delete(receiverId);
    }
    //id ye göre receiverin getirilmesi
    public Receiver getReceiverById(Integer id) {
        //önce tüm receiverleri döndürürüz sonra da id si eşlenen receiver döndürülür
        List<Receiver> receivers = receiverRepo.findAll();
        for (Receiver receiver : receivers) {
            if (receiver.getId().equals(id)) {
                return receiver;
            }
        }
        return null;
    }
    //tüm receiverlerin döndürülmesi
    public List<Receiver> getAllReceivers() {
        return receiverRepo.findAll();
    }
    
    //getter-setter mantığı
    public JsonRepository<Receiver> getReceiverRepo() {
        return receiverRepo;
    }
    //getter-setter mantığı
    public void setReceiverRepo(JsonRepository<Receiver> receiverRepo) {
        this.receiverRepo = receiverRepo;
    }
}
/*
package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Cargo;
import org.project.models.Product;
import org.project.models.Receiver;
import org.project.models.User;

import java.util.List;

public class ReceiverService {

    public JsonRepository<Receiver> receiverRepo;

    public ReceiverService(JsonRepository<Receiver> receiverRepo) {
        this.receiverRepo = receiverRepo;
    }

    public Receiver getReceiver(int id) {
        return null;
    }

    public List<Receiver> getReceivers() {
        return null;
    }

    public Receiver getReceiverById(Integer id) {
        List<Receiver> receivers = receiverRepo.findAll();
        for (Receiver receiver : receivers) {
            if (receiver.getId().equals(id)) {
                return receiver;
            }
        }
        return null;
    }
    public List<Receiver> getAllReceivers() {
        return receiverRepo.findAll();
    }
    public void deleteReceiverById(Integer id) {
        receiverRepo.delete(id);
    }
    public void updateReceiver(Receiver receiver) {
        receiverRepo.update(receiver);
    }
    public void addReceiver(Receiver receiver) {
        receiverRepo.save(receiver);
    }

    public JsonRepository<Receiver> getReceiverRepo () {
        return receiverRepo;
    }
    public void setReceiverRepo (JsonRepository < Receiver > receiverRepo) {
        this.receiverRepo = receiverRepo;
    }
}
*/