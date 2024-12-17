package org.example.User.service;

import org.example.models.User;
import org.example.data.JsonRepository;

import java.util.List;

public class UserService {
    private JsonRepository<User> userRepository;

    public UserService(JsonRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public void deleteUser(String userId) {
        userRepository.delete(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User authenticate(String username,/*String surname,*/ String password) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}