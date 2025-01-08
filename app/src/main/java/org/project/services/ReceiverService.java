package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Receiver;

import java.util.List;

public class ReceiverService {
    // Creating a receiver repository using the JsonRepository class
    private static JsonRepository<Receiver> receiverRepo = new JsonRepository<>(Receiver[].class);

    public ReceiverService() {
    }

    // Method to add a new receiver
    public static boolean add(Receiver receiver) {
        // Check if a receiver with the same email already exists
        Receiver existingReceiver = getReceiverByEmail(receiver.getEmail());
        if (existingReceiver != null) {
            // If a receiver with the same email exists, return false
            return false;
        } else {
            // If no receiver with the same email exists, save the new receiver to the repository
            receiverRepo.save(receiver);
            return true;
        }
    }

    // Method to update an existing receiver
    // This method uses the update functionality provided by the JsonRepository class
    public static void update(Receiver receiver) {
        receiverRepo.update(receiver);
    }

    // Method to delete a receiver by its ID
    // This method uses the delete functionality provided by the JsonRepository class
    public static void delete(Integer receiverId) {
        receiverRepo.delete(receiverId);
    }

    // Method to find a receiver by its ID
    public static Receiver findReceiverById(Integer id) {
        List<Receiver> receivers = receiverRepo.findAll();
        for (Receiver receiver : receivers) {
            if (receiver.getId().equals(id)) {
                return receiver;
            }
        }
        return null;
    }

    // Method to get a receiver by its email
    public static Receiver getReceiverByEmail(String email) {
        List<Receiver> receivers = receiverRepo.findAll();
        for (Receiver receiver : receivers) {
            if (receiver.getEmail().equals(email)) {
                return receiver;
            }
        }
        return null;
    }

    // Method to get all receivers
    public static List<Receiver> getAllReceivers() {
        return receiverRepo.findAll();
    }

    // Method to get the receiver repository
    public static JsonRepository<Receiver> getReceiverRepo() {
        return receiverRepo;
    }

    // Method to set the receiver repository
    public static void setReceiverRepo(JsonRepository<Receiver> receiverRepo) {
        ReceiverService.receiverRepo = receiverRepo;
    }
}