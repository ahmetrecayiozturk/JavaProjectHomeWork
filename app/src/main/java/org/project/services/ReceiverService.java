package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Receiver;

import java.util.List;

public class ReceiverService {

    private JsonRepository<Receiver> receiverRepo=new JsonRepository<>( Receiver[].class);

    public ReceiverService() {

    }

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

    public void update(Receiver receiver) {
        receiverRepo.update(receiver);
    }

    public void delete(Integer receiverId) {
        receiverRepo.delete(receiverId);
    }

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

    public List<Receiver> getAllReceivers() {
        return receiverRepo.findAll();
    }

    public JsonRepository<Receiver> getReceiverRepo() {
        return receiverRepo;
    }

    public void setReceiverRepo(JsonRepository<Receiver> receiverRepo) {
        receiverRepo = receiverRepo;
    }
}
