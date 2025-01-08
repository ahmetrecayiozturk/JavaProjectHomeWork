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
    // Directory where images will be stored
    private static Path imageStorageDirectory = Paths.get(App.getAppDir().toString(), "images");

    public ImageService() {
        // Create the images directory if it does not exist
        createImagesDirectoryIfNotExist();
    }

    // Method to create the images directory if it does not exist
    private static void createImagesDirectoryIfNotExist() {
        try {
            if (!Files.exists(imageStorageDirectory)) {
                Files.createDirectories(imageStorageDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save an image file to the storage directory
    public static Path saveImage(File imageFile) throws IOException {
        // Get the file extension of the image
        String extension = getFileExtension(imageFile);
        // Generate a unique file name for the image
        String imageFileName = Math.abs(UUID.randomUUID().hashCode()) + extension;
        File targetFile = new File(imageStorageDirectory.toString(), imageFileName);
        Path imageFilePath = imageStorageDirectory.resolve(imageFileName);
        try {
            // Copy the image file to the target directory
            Files.copy(imageFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageFilePath;
    }

    // Method to get the file extension of a file
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            return fileName.substring(dotIndex);
        }
        return "";
    }

    // Method to delete an image associated with a product
    public static boolean deleteImage(Product product) {
        Path imagePath = imageStorageDirectory.resolve(product.getImageUrl());
        try {
            return Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to update an image associated with a product
    public static String updateImage(Product product, File image) throws IOException {
        // Delete the old image
        if (deleteImage(product)) {
            // Save the new image
            saveImage(image);
        }
        return product.getImageUrl();
    }

    // Method to check if an image associated with a product exists
    public static boolean imageExists(Product product) {
        Path imagePath = imageStorageDirectory.resolve(product.getImageUrl());
        return Files.exists(imagePath);
    }

    // Method to open a file chooser dialog to select an image
    public static File chooseImage() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile;
        }
        return null;
    }

    // Method to get the image storage directory
    public static Path getImageStorageDirectory() {
        return imageStorageDirectory;
    }

    // Method to set the image storage directory
    public static void setImageStorageDirectory(Path imageStorageDirectory) {
        ImageService.imageStorageDirectory = imageStorageDirectory;
    }
}