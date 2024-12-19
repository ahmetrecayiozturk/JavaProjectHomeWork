package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Receiver;
import org.project.models.User;

import java.util.List;

public class ReceiverService {

    public JsonRepository<Receiver> receiverRepo;
    public ReceiverService(JsonRepository<Receiver> receiverRepo) {
        this.receiverRepo = receiverRepo;
    }

    public void add(Receiver receiver) {

    }
    public void update(Receiver receiver) {

    }
    public void delete(Receiver receiver) {

    }
    public Receiver getReceiver(int id) {
        return null;
    }
    public List<Receiver> getReceivers() {
        return null;
    }
}
