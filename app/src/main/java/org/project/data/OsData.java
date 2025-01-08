package org.project.data;

import org.project.App;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OsData {
    // Method to get the user data path based on the operating system
    public static Path getUserDataPath() {
        // Get the user's home directory
        String userHome = System.getProperty("user.home");

        // Get the operating system name
        String os = System.getProperty("os.name").toLowerCase();

        // Initialize the directory path variable
        Path directoryPath;

        // Determine the directory path based on the operating system
        if (os.contains("win")) {
            // For Windows, use the AppData\Roaming directory
            directoryPath = Paths.get(userHome, "AppData", "Roaming", ".myapp");
        } else if (os.contains("mac")) {
            // For macOS, use the user's home directory
            directoryPath = Paths.get(userHome, ".myapp");
        } else if (os.contains("nix") || os.contains("nux")) {
            // For Unix/Linux, use the user's home directory
            directoryPath = Paths.get(userHome, ".myapp");
        } else {
            // If the operating system is not supported, throw an exception
            throw new UnsupportedOperationException("Unsupported operating system: " + os);
        }

        // Return the determined directory path
        return directoryPath;
    }

    // Method to create the application directory if it does not exist
    public static void createDirectoryIfNotExist() {
        try {
            // Check if the application directory exists
            if (!Files.exists(App.getAppDir())) {
                // If it does not exist, create the directory
                Files.createDirectories(App.getAppDir());
            }
        } catch (IOException e) {
            // Print the stack trace if an IOException occurs
            e.printStackTrace();
        }
    }
}