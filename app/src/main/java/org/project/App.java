package org.project;

import org.project.data.OsData;
import org.project.frames.entry.EntryFrame;
import org.project.frames.home.HomeFrame;
import org.project.models.*;
import org.project.services.*;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;

public class App {
    private static final Path appDir = OsData.getUserDataPath();
    private static User currentUser;
    private static Store currentStore;
    private static HomeFrame homeFrame;
    private static EntryFrame entryFrame;

    public static void main(String[] args) throws IOException {
        initializeAppStorage();
        initializeServices();
        startEntryFrame();
    }
    public static void initializeAppStorage(){
        OsData.createDirectoryIfNotExist();
    }
    public static void initializeServices(){
        new UserService();
        new ProductService();
        new ImageService();
        new CargoService();
        new ReceiverService();
        new OrderService();
        new StoreService();
    }
    public static void startEntryFrame() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                entryFrame=new EntryFrame();
            }
        });
    }
    public static Path getAppDir() {
        return appDir;
    }

    public static Store getCurrentStore() {
        return currentStore;
    }

    public static void setCurrentStore(Store currentStore) {
        App.currentStore = currentStore;
    }

    public static void switchToHomeFrame() {
        currentStore=StoreService.getStoreById(currentUser.getStoreId());
        entryFrame.dispose();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                homeFrame= new HomeFrame();
            }
        });
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }

    public static HomeFrame getHomeFrame() {
        return homeFrame;
    }

    public static void setHomeFrame(HomeFrame homeFrame) {
        App.homeFrame = homeFrame;
    }

    public static EntryFrame getEntryFrame() {
        return entryFrame;
    }

    public static void setEntryFrame(EntryFrame entryFrame) {
        App.entryFrame = entryFrame;
    }
}





