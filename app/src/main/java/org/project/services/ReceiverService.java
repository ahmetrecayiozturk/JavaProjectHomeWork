package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.Receiver;

import java.util.List;

public class ReceiverService {
    //JsonRepository ile Receiver modelini parametre olarak kullanarak bir JsonRepository nesnesi oluşturuyoruz
    private static final JsonRepository<Receiver> receiverRepo=new JsonRepository<>( Receiver[].class);

    public ReceiverService() {

    }

    //servis başladığında oluşturulan jsonRepository nesnesi ile bir receiverRepo oluşturulmuştur,
    //bu receiverRepo kullanılarak bu receiver var olan bir receiver mi kontrol ediyoruz, ardından da eğer var ise eklemiyoruz,
    //yok ise ekliyoruz
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

    //servis başladığında oluşturulan jsonRepository nesnesi ile bir receiverRepo oluşturulmuştur,
    //oluşturulan bu receiverRepo ile tüm receiverler döndürülür ve id parametresi ile receiverler içerisindeki herhangi bir
    //reeiverin id si ile bu id parametresi eşleniyorsa o receiver döndürülüyor
    public static Receiver findReceiverById(Integer id) {
        List<Receiver> receivers = receiverRepo.findAll();
        for (Receiver receiver : receivers) {
            if (receiver.getId().equals(id)) {
                return receiver;
            }
        }
        return null;
    }

    //servis başladığında oluşturulan jsonRepository nesnesi ile bir receiverRepo oluşturulmuştur,
    //oluşturulan bu receiverRepo ile tüm receiverler döndürülür ve email parametresi ile receiverler içerisindeki herhangi bir
    //reeiverin email'i ile bu email parametresi eşleniyorsa o receiver döndürülüyor
    public static Receiver getReceiverByEmail(String email) {
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

}
