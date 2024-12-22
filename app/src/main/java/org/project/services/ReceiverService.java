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
    public boolean add(Receiver receiver) {
        Receiver existingReceiver = getReceiverByEmail(receiver.getEmail());
        if(existingReceiver != null){
            return false;
        }
        else{
            receiverRepo.save(receiver);
            return true;
        }
    }

    //receiver güncellenmesi
    public void update(Receiver receiver) {
        receiverRepo.update(receiver);
    }

    //receiver silinmesi
    public void delete(Integer receiverId) {
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

    //emaile ye göre receiverin getirilmesi
    public Receiver getReceiverByEmail(String email) {
        //önce tüm receiverleri döndürürüz sonra da id si eşlenen receiver döndürülür
        List<Receiver> receivers = receiverRepo.findAll();
        for (Receiver receiver : receivers) {
            if (receiver.getEmail().equals(email)) {
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
