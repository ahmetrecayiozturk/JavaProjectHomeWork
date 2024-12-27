package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Receiver;

import java.util.List;

public class ReceiverService {

    private static JsonRepository<Receiver> receiverRepo;

    //receiver servisinin constructoru
    public ReceiverService(JsonRepository<Receiver> receiverRepo) {
        this.receiverRepo = receiverRepo;
    }

    //receiver eklenmesi
    public static boolean add(Receiver receiver) {
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
    public static void update(Receiver receiver) {
        receiverRepo.update(receiver);
    }

    //receiver silinmesi
    public static void delete(Integer receiverId) {
        receiverRepo.delete(receiverId);
    }

    //id ye göre receiverin getirilmesi
    public static Receiver getReceiverById(Integer id) {
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
    public static Receiver getReceiverByEmail(String email) {
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
    public static List<Receiver> getAllReceivers() {
        return receiverRepo.findAll();
    }
    
    //getter-setter mantığı
    public static JsonRepository<Receiver> getReceiverRepo() {
        return receiverRepo;
    }

    //getter-setter mantığı
    public static void setReceiverRepo(JsonRepository<Receiver> receiverRepo) {
        receiverRepo = receiverRepo;
    }
}
