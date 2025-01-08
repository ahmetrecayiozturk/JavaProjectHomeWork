package org.project.data;

import com.google.gson.Gson;
import org.project.App;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonRepository<T extends Identifiable> {
    // Path to the JSON file where data will be stored
    private String filePath;
    // Type of the data to be stored in the JSON file
    private Class<T[]> type;

    // Constructor to initialize the JsonRepository with the specified type
    public JsonRepository(Class<T[]> type) {
        // Set the file path based on the type of data
        this.filePath = App.getAppDir().toString() + "/" + type.getComponentType().getSimpleName() + ".json";
        // Initialize the JSON file if it does not exist
        initializeJsonFile();
        // Set the type of data
        this.type = type;
    }

    // Method to save a new item to the repository
    public void save(T item) {
        // Retrieve all items from the repository
        List<T> items = findAll();
        if (items == null) {
            items = new ArrayList<>();
        }
        // Check for duplicate item IDs
        for (T existingItem : items) {
            if (existingItem.getId().equals(item.getId())) {
                throw new IllegalArgumentException("Duplicate item id: " + item.getId());
            }
        }
        // Add the new item to the list
        items.add(item);
        // Write the updated list to the JSON file
        writeToFile(items);
    }

    // Method to update an existing item in the repository
    public void update(T item) {
        // Retrieve all items from the repository
        List<T> items = findAll();
        if (items == null) {
            items = new ArrayList<>();
        }
        // Find the item to be updated and replace it with the new item
        for (T existingItem : items) {
            if (existingItem.getId().equals(item.getId())) {
                items.remove(existingItem);
                items.add(item);
                break;
            }
        }
        // Write the updated list to the JSON file
        writeToFile(items);
    }

    // Method to delete an item from the repository by its ID
    public void delete(Integer id) {
        // Retrieve all items from the repository
        List<T> items = findAll();
        if (items == null) {
            items = new ArrayList<>();
        }
        // Find the item to be deleted and remove it from the list
        for (T item : items) {
            if (item.getId().equals(id)) {
                items.remove(item);
                // Write the updated list to the JSON file
                writeToFile(items);
                return;
            }
        }
        // If the item is not found, throw an exception
        throw new IllegalArgumentException("Item not found: " + id);
    }

    // Method to find an item in the repository by its ID
    public T findOne(Integer id) {
        // Retrieve all items from the repository
        List<T> items = findAll();
        // Iterate through the list of items to find a match
        for (T item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        // If no match is found, return null
        return null;
    }

    // Method to retrieve all items from the repository
    public List<T> findAll() {
        try (FileReader reader = new FileReader(filePath)) {
            // Read the JSON file and convert it to an array of items
            T[] array = new Gson().fromJson(reader, type);
            if (array != null) {
                // Convert the array to a list and return it
                return new ArrayList<>(Arrays.asList(array));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // If an error occurs or the file is empty, return an empty list
        return new ArrayList<>();
    }

    // Method to initialize the JSON file if it does not exist
    public void initializeJsonFile() {
        File file = new File(this.filePath);
        if (!file.exists()) {
            try {
                // Create a new file
                file.createNewFile();
                // Write an empty JSON array to the file
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to write a list of items to the JSON file
    private void writeToFile(List<T> items) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Convert the list of items to a JSON array and write it to the file
            new Gson().toJson(items.toArray(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}