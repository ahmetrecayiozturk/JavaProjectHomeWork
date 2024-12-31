package org.project.data;

import org.project.App;
import org.project.services.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OsData {
    public static Path getUserDataPath() {
        String userHome = System.getProperty("user.home");

        String os = System.getProperty("os.name").toLowerCase();

        Path directoryPath;

        if (os.contains("win")) {
            directoryPath = Paths.get(userHome, "AppData", "Roaming", ".myapp");
        } else if (os.contains("mac")) {
            directoryPath = Paths.get(userHome, ".myapp");
        } else if (os.contains("nix") || os.contains("nux")) {
            directoryPath = Paths.get(userHome, ".myapp");
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + os);
        }

        return directoryPath;
    }
    public static void createDirectoryIfNotExist() {
        try {
            if (!Files.exists(App.getAppDir())) {
                Files.createDirectories(App.getAppDir());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
