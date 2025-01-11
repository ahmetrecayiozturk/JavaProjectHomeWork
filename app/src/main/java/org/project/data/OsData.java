package org.project.data;

import org.project.App;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OsData {

    /**
     * Retrieves the path to the user-specific application data directory.
     * This method constructs the path based on the operating system.
     *
     * - For Windows, it uses the "AppData/Roaming" folder.
     * - For macOS and Linux-based systems, it uses the home directory and appends ".myapp".
     *
     * If the operating system is not supported, it throws an UnsupportedOperationException.
     *
     * @return The path to the user-specific application data directory.
     * @throws UnsupportedOperationException If the operating system is not supported.
     */
    public static Path getUserDataPath() {
        String userHome = System.getProperty("user.home"); // Get the user's home directory
        String os = System.getProperty("os.name").toLowerCase(); // Get the operating system name in lowercase

        Path directoryPath; // Variable to hold the directory path

        // Construct the path based on the operating system
        if (os.contains("win")) {
            directoryPath = Paths.get(userHome, "AppData", "Roaming", ".myapp"); // For Windows
        } else if (os.contains("mac")) {
            directoryPath = Paths.get(userHome, ".myapp"); // For macOS
        } else if (os.contains("nix") || os.contains("nux")) {
            directoryPath = Paths.get(userHome, ".myapp"); // For Linux-based systems
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + os); // Unsupported OS
        }

        return directoryPath; // Return the constructed path
    }

    /**
     * Creates the directory for application data if it does not already exist.
     * This method checks if the directory exists at the specified path, and if not, it creates the directory.
     * Any IOException encountered during the process is caught and logged to the console.
     */
    public static void createDirectoryIfNotExist() {
        try {
            // Check if the directory at the path returned by App.getAppDir() exists
            if (!Files.exists(App.getAppDir())) {
                Files.createDirectories(App.getAppDir()); // Create the directory if it doesn't exist
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception if an IOException occurs
        }
    }
}
