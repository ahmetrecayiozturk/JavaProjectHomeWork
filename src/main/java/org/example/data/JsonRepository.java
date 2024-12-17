package org.example.data;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonRepository<T> {
    private String filePath;
    private Class<T[]> type;

    public JsonRepository(String filePath, Class<T[]> type) {
        this.filePath = filePath;
        this.type = type;
    }

    public void save(T item) {
        List<T> items = findAll();
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        writeToFile(items);
    }

    public void update(T item) {
        List<T> items = findAll();
        if (items == null) {
            items = new ArrayList<>();
        }
        // Update logic here
        writeToFile(items);
    }

    public void delete(String id) {
        List<T> items = findAll();
        if (items == null) {
            items = new ArrayList<>();
        }
        // Delete logic here
        writeToFile(items);
    }

    public List<T> findAll() {
        try (FileReader reader = new FileReader(filePath)) {
            T[] array = new Gson().fromJson(reader, type);
            if (array != null) {
                return new ArrayList<>(Arrays.asList(array));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void writeToFile(List<T> items) {
        try (FileWriter writer = new FileWriter(filePath)) {
            new Gson().toJson(items.toArray(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/*
package org.example.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonRepository<T> {
    private String filePath;
    private Type type;

    public JsonRepository(String filePath, Type type) {
        this.filePath = filePath;
        this.type = type;
    }

    public void save(T item) {
        List<T> items = findAll();
        items.add(item);
        writeToFile(items);
    }

    public void update(T item) {
        List<T> items = findAll();
        // Update logic here
        writeToFile(items);
    }

    public void delete(String id) {
        List<T> items = findAll();
        // Delete logic here
        writeToFile(items);
    }

    public List<T> findAll() {
        try (FileReader reader = new FileReader(filePath)) {
            return new Gson().fromJson(reader, type);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void writeToFile(List<T> items) {
        try (FileWriter writer = new FileWriter(filePath)) {
            new Gson().toJson(items, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/