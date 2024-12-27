package org.project.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OsData {
    public static String getUserDataPath() {
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

        try {
            Files.createDirectories(directoryPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return directoryPath.toString();
    }
}
