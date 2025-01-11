package org.project.services;

import org.project.App;
import org.project.models.Product;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ImageService {
    // Used to create a subfolder named "images" in the application directory.
    private static Path imageStorageDirectory = Paths.get(App.getAppDir().toString(), "images");

    // Constructor: Checks if the image directory exists when an instance of this class is created.
    public ImageService() {
        createImagesDirectoryIfNotExist(); // Create the image directory.
    }

    /**
     * Checks whether the directory where images will be stored exists.
     * If the directory does not exist, it creates a new directory.
     */
    private static void createImagesDirectoryIfNotExist() {
        try {
            if (!Files.exists(imageStorageDirectory)) {
                Files.createDirectories(imageStorageDirectory); // Creates a new directory.
            }
        } catch (IOException e) {
            e.printStackTrace(); // Prints error details to the console in case of failure.
        }
    }

    /**
     * Saves the given image file.
     * - Retrieves the file extension.
     * - Creates a unique file name and moves the file to the target directory.
     *
     * @param imageFile The image file to be saved.
     * @return The file path of the saved image.
     * @throws IOException Thrown if there is an error during file processing.
     */
    public static Path saveImage(File imageFile) throws IOException {
        String extension = getFileExtension(imageFile); // Retrieves the file extension.
        String imageFileName = Math.abs(UUID.randomUUID().hashCode()) + extension; // Creates a unique file name.
        File targetFile = new File(imageStorageDirectory.toString(), imageFileName); // Defines the target file path.
        Path imageFilePath = imageStorageDirectory.resolve(imageFileName); // Retrieves the full path of the target file.
        try {
            Files.copy(imageFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING); // Copies the file.
        } catch (IOException e) {
            e.printStackTrace(); // Prints error details to the console in case of failure.
            return null;
        }
        return imageFilePath; // Returns the path of the saved file.
    }

    /**
     * Retrieves the file extension of a file.
     * - Returns the part after the last dot in the file name.
     *
     * @param file The file whose extension is to be retrieved.
     * @return The file extension (e.g., ".jpg").
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName(); // Retrieves the file name.
        int dotIndex = fileName.lastIndexOf("."); // Finds the last dot in the file name.
        if (dotIndex > 0) {
            return fileName.substring(dotIndex); // Returns the extension.
        }
        return ""; // Returns an empty string if no extension is found.
    }

    /**
     * Deletes the image associated with a given product.
     *
     * @param product The product whose image will be deleted.
     * @return True if the image was successfully deleted, otherwise false.
     */
    public static boolean deleteImage(Product product) {
        Path imagePath = imageStorageDirectory.resolve(product.getImageUrl()); // Defines the full path of the image.
        try {
            return Files.deleteIfExists(imagePath); // Deletes the image file (if it exists).
        } catch (IOException e) {
            e.printStackTrace(); // Prints error details to the console in case of failure.
            return false;
        }
    }

    /**
     * Updates the image of a given product.
     * - Deletes the old image.
     * - Saves the new image.
     *
     * @param product The product whose image will be updated.
     * @param image The new image file.
     * @return The file path of the updated image.
     * @throws IOException Thrown if there is an error during file processing.
     */
    public static String updateImage(Product product, File image) throws IOException {
        if (deleteImage(product)) { // Deletes the old image first.
            saveImage(image); // Saves the new image.
        }
        return product.getImageUrl(); // Returns the updated image path of the product.
    }

    /**
     * Checks if an image exists for the given product.
     *
     * @param product The product to check for an image.
     * @return True if the image exists, otherwise false.
     */
    public static boolean imageExists(Product product) {
        Path imagePath = imageStorageDirectory.resolve(product.getImageUrl()); // Defines the full path of the image.
        return Files.exists(imagePath); // Checks if the image exists.
    }

    /**
     * Allows the user to choose an image.
     * - Opens a file selection dialog.
     *
     * @return The file selected by the user, or null if no selection is made.
     * @throws IOException Thrown if there is an error during file processing.
     */
    public static File chooseImage() throws IOException {
        JFileChooser fileChooser = new JFileChooser(); // Creates a file chooser.
        int returnValue = fileChooser.showOpenDialog(null); // Asks the user to select a file.
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile(); // Retrieves the file selected by the user.
            return selectedFile;
        }
        return null; // Returns null if the user does not make a selection.
    }

    /**
     * Returns the directory where images are stored.
     *
     * @return The path of the image storage directory.
     */
    public static Path getImageStorageDirectory() {
        return imageStorageDirectory; // Returns the path of the image storage directory.
    }

    /**
     * Sets the directory where images will be stored.
     *
     * @param imageStorageDirectory The new image storage directory.
     */
    public static void setImageStorageDirectory(Path imageStorageDirectory) {
        ImageService.imageStorageDirectory = imageStorageDirectory; // Sets the new directory path.
    }
}
