package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Receiver;

import java.util.List;

public class ReceiverService {

    private static final JsonRepository<Receiver> receiverRepo=new JsonRepository<>( Receiver[].class);

    public ReceiverService() {

    }

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

    public static void update(Receiver receiver) {
        receiverRepo.update(receiver);
    }

    public static void delete(Integer receiverId) {
        receiverRepo.delete(receiverId);
    }

    public static Receiver findReceiverById(Integer id) {
        List<Receiver> receivers = receiverRepo.findAll();
        for (Receiver receiver : receivers) {
            if (receiver.getId().equals(id)) {
                return receiver;
            }
        }
        return null;
    }

    public static Receiver getReceiverByEmail(String email) {
        List<Receiver> receivers = receiverRepo.findAll();
        for (Receiver receiver : receivers) {
            if (receiver.getEmail().equals(email)) {
                return receiver;
            }
        }
        return null;
    }

    public static List<Receiver> getAllReceivers() {
        return receiverRepo.findAll();
    }

}
