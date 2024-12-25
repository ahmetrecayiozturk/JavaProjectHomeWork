package org.project;

import org.project.data.JsonRepository;
import org.project.data.OsData;
import org.project.frames.Entry;
import org.project.frames.Home;
import org.project.models.*;
import org.project.services.*;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import static org.project.data.JsonRepository.initializeJsonFile;

public class App {
    public static void main(String[] args) throws IOException {
        //User Servis
        initializeJsonFile(OsData.getUserDataPath("users.json"));
        UserService userService = new UserService(new JsonRepository<>(OsData.getUserDataPath("users.json"), User[].class));
        //UserService.createUserFile();
        //Order Service
        initializeJsonFile(OsData.getUserDataPath("orders.json"));
        OrderService orderService = new OrderService(new JsonRepository<>(OsData.getUserDataPath("orders.json"), Order[].class));

        //ReceiverService
        initializeJsonFile(OsData.getUserDataPath("receivers.json"));
        ReceiverService receiverService = new ReceiverService(new JsonRepository<>(OsData.getUserDataPath("receivers.json"), Receiver[].class));

        // StoreService
        initializeJsonFile(OsData.getUserDataPath("stores.json"));
        StoreService storeService = new StoreService(new JsonRepository<>(OsData.getUserDataPath("stores.json"), Store[].class));

        // Image Service
        String appDataPath = System.getenv("APPDATA");
        String appStorageDirectory = Paths.get(appDataPath, ".myapp").toString();
        ImageService imageService = new ImageService(appStorageDirectory);

        // ProductService
        initializeJsonFile(OsData.getUserDataPath("products.json"));
        ProductService productService= new ProductService(new JsonRepository<>(OsData.getUserDataPath("products.json"), Product[].class),imageService);

        // CargoService
        initializeJsonFile(OsData.getUserDataPath("cargos.json"));
        CargoService cargoService= new CargoService(new JsonRepository<>(OsData.getUserDataPath("cargos.json"), Cargo[].class));

/*
        // Create Sample Data
        User user = new User("email","pass");
        userService.addUser(user);
        Receiver receiver = new Receiver("email","password","name","surname","address");
        receiverService.add(receiver);
        Product  product = new Product("name","descp","id","url",12.1);
        productService.add(product);
        Order<Receiver, Product> order = new Order<>(receiver, product);
        orderService.add(order);
        Cargo<Order> cargo = new Cargo<>(false,order);
        cargoService.add(cargo);
*/

        // Launch UI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Entry();
            }

        });
    }
}
