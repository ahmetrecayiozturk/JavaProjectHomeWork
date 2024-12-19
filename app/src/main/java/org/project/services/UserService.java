package org.project.services;

import org.project.data.JsonRepository;
import org.project.models.User;

import java.util.List;

public class UserService {
    private static JsonRepository<User> userRepository;

    public UserService(JsonRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    public static boolean addUser(User user) {
        User user4=getUserByEmail(user.getEmail());
        if(user4 != null) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public static void updateUser(User user) {
        userRepository.update(user);
    }

    public static void deleteUser(Integer userId) {
        userRepository.delete(userId);
    }

    public static List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public static User authenticate(String email, String password) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    public static User getUserByEmail(String email) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}