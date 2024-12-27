package org.project.services;

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
    private static Path imageStorageDirectory;

    public ImageService(String appStorageDirectory) {
        imageStorageDirectory = Paths.get(appStorageDirectory, "images");
        createImagesDirectoryIfNotExist();
    }

    private void createImagesDirectoryIfNotExist() {
        try {
            if (!Files.exists(imageStorageDirectory)) {
                Files.createDirectories(imageStorageDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path saveImage(File imageFile) throws IOException {
        String extension = getFileExtension(imageFile);
        String imageFileName = Math.abs(UUID.randomUUID().hashCode()) + extension;
        File targetFile = new File(imageStorageDirectory.toString(), imageFileName);
        Path imageFilePath = imageStorageDirectory.resolve(imageFileName);
        try {
            Files.copy(imageFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageFilePath;
    }
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            return fileName.substring(dotIndex);
        }
        return "";
    }

    public boolean deleteImage(Product product) {
        Path imagePath = imageStorageDirectory.resolve(product.getImageUrl());
        try {
            return Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String updateImage(Product product, File image) throws IOException {
        if (deleteImage(product)) {
            saveImage(image);
        }
        return product.getImageUrl();
    }

    public boolean imageExists(Product product) {
        Path imagePath = imageStorageDirectory.resolve(product.getImageUrl());
        return Files.exists(imagePath);
    }

    public static File chooseImage() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile;
        }
        return null;
    }
}




