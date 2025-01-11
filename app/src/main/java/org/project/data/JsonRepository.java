package org.project.data;

import com.google.gson.Gson;
import org.project.App;
import org.project.models.interfaces.Identifiable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonRepository<T extends Identifiable> {
    private String filePath; // Path to the JSON file where data is stored
    private Class<T[]> type; // Type of the class that represents the elements of the repository

    /**
     * Constructor for the JsonRepository class.
     * Initializes the file path based on the class type and creates the JSON file if it doesn't exist.
     *
     * @param type The type of elements (T[]) to store in the repository.
     */
    public JsonRepository(Class<T[]> type) {
        this.filePath = App.getAppDir().toString()+"/"+type.getComponentType().getSimpleName()+".json";
        initializeJsonFile(); // Ensure the JSON file exists
        this.type = type; // Store the class type for later use
    }

    /**
     * Save a new item into the repository (JSON file).
     * This method first checks for duplicates by comparing the ID of the new item with existing ones.
     * If a duplicate ID is found, it throws an IllegalArgumentException.
     * If no duplicates are found, the item is added to the list and written to the file.
     *
     * @param item The item to save in the repository.
     */
    public void save(T item) {
        List<T> items = findAll(); // Retrieve all existing items
        if (items == null) {
            items = new ArrayList<>(); // Initialize the list if no items exist
        }
        // Check if the item already exists in the repository based on its ID
        for (T item2 : items) {
            if (item2.getId().equals(item.getId())) {
                throw new IllegalArgumentException("Duplicate item id: " + item.getId());
            }
        }
        items.add(item); // Add the new item to the list
        writeToFile(items); // Write the updated list back to the file
    }

    /**
     * Update an existing item in the repository.
     * This method searches for the item with the same ID, removes it from the list, and replaces it with the updated item.
     * The updated list is then written back to the file.
     *
     * @param item The item with updated information.
     */
    public void update(T item) {
        List<T> items = findAll(); // Retrieve all existing items
        if (items == null) {
            items = new ArrayList<>(); // Initialize the list if no items exist
        }
        // Find and update the item with the same ID
        for(T item1 : items){
            if (item1.getId().equals(item.getId())) {
                items.remove(item1); // Remove the old item
                items.add(item); // Add the updated item
                break;
            }
        }
        writeToFile(items); // Write the updated list back to the file
    }

    /**
     * Delete an item from the repository by its ID.
     * The method searches for the item with the given ID and removes it from the list.
     * If no item with the given ID is found, an IllegalArgumentException is thrown.
     *
     * @param id The ID of the item to delete.
     */
    public void delete(Integer id) {
        List<T> items = findAll(); // Retrieve all existing items
        if (items == null) {
            items = new ArrayList<>(); // Initialize the list if no items exist
        }
        // Find and remove the item with the given ID
        for (T item : items) {
            if (item.getId().equals(id)) {
                items.remove(item); // Remove the item from the list
                writeToFile(items); // Write the updated list back to the file
                return;
            }
        }
        throw new IllegalArgumentException("Item not found: " + id); // Throw an exception if the item is not found
    }

    /**
     * Find a single item by its ID.
     * The method searches for an item in the repository by its ID. If found, it returns the item.
     * If no item is found with the given ID, it returns null.
     *
     * @param id The ID of the item to find.
     * @return The item with the given ID, or null if not found.
     */
    public T findOne(Integer id) {
        List<T> items = findAll(); // Retrieve all existing items
        // Search for the item with the given ID
        for (T item : items) {
            if (item.getId().equals(id)) {
                return item; // Return the item if found
            }
        }
        return null; // Return null if the item is not found
    }

    /**
     * Retrieve all items from the repository.
     * This method reads the JSON file, deserializes it into an array of items, and returns it as a list.
     * If the file is empty or an error occurs during reading, an empty list is returned.
     *
     * @return A list of all items in the repository.
     */
    public List<T> findAll() {
        try (FileReader reader = new FileReader(filePath)) {
            T[] array = new Gson().fromJson(reader, type); // Deserialize the JSON data into an array
            if (array != null) {
                return new ArrayList<>(Arrays.asList(array)); // Return the items as a list
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log any IOException that occurs
        }
        return new ArrayList<>(); // Return an empty list if no items are found or if an error occurs
    }

    /**
     * Initialize the JSON file if it doesn't already exist.
     * This method checks if the JSON file exists at the specified path. If it doesn't exist,
     * it creates a new file and writes an empty JSON array ("[]") to it.
     */
    public void initializeJsonFile() {
        File file = new File(this.filePath);
        if (!file.exists()) {
            try {
                file.createNewFile(); // Create the file if it doesn't exist
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]"); // Write an empty JSON array to the file
                }
            } catch (IOException e) {
                e.printStackTrace(); // Log any IOException that occurs during file creation
            }
        }
    }

    /**
     * Write a list of items to the JSON file.
     * This method serializes the list of items into JSON format and writes it to the specified file.
     *
     * @param items The list of items to write to the file.
     */
    private void writeToFile(List<T> items) {
        try (FileWriter writer = new FileWriter(filePath)) {
            new Gson().toJson(items.toArray(), writer); // Serialize the items list to JSON and write to the file
        } catch (IOException e) {
            e.printStackTrace(); // Log any IOException that occurs during file writing
        }
    }
}
