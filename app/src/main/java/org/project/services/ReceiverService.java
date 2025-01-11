package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Receiver;

import java.util.List;

public class ReceiverService {
    // Using JsonRepository, we create a JsonRepository object with the Receiver model as a parameter.
    private static final JsonRepository<Receiver> receiverRepo = new JsonRepository<>(Receiver[].class);

    public ReceiverService() {

    }

    // With the JsonRepository object created when the service starts, we create a receiverRepo.
    // Using this receiverRepo, we check if the receiver already exists. If it exists, we do not add it;
    // if it does not exist, we add it.
    public static boolean add(Receiver receiver) {
        Receiver existingReceiver = getReceiverByEmail(receiver.getEmail());
        if (existingReceiver != null) {
            return false;
        } else {
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

    // With the JsonRepository object created when the service starts, we create a receiverRepo.
    // Using this receiverRepo, all receivers are returned. If the ID parameter matches the ID of any receiver
    // in the list of receivers, that receiver is returned.
    public static Receiver findReceiverById(Integer id) {
        List<Receiver> receivers = receiverRepo.findAll();
        for (Receiver receiver : receivers) {
            if (receiver.getId().equals(id)) {
                return receiver;
            }
        }
        return null;
    }

    // With the JsonRepository object created when the service starts, we create a receiverRepo.
    // Using this receiverRepo, all receivers are returned. If the email parameter matches the email of any receiver
    // in the list of receivers, that receiver is returned.
    public static Receiver getReceiverByEmail(String email) {
        List<Receiver> receivers = receiverRepo.findAll();
        for (Receiver receiver : receivers) {
            if (receiver.getEmail().equals(email)) {
                return receiver;
            }
        }
        return null;
    }

    // Returning all receivers
    public static List<Receiver> getAllReceivers() {
        return receiverRepo.findAll();
    }

}
