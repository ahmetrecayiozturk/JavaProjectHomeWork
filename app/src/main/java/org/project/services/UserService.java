package org.project.services;

import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.Store;
import org.project.models.User;

import java.util.List;

public class UserService {
    private JsonRepository<User> userRepository=new JsonRepository<>( User[].class);

    public UserService() {}

    public boolean addUser(User user) {
        User user4=getUserByEmail(user.getEmail());
        if(user4 != null) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public void deleteUser(Integer userId) {
        userRepository.delete(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User authenticate(String email, String password) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                App.setCurrentUser(user);
                return user;
            }
        }
        return null;
    }
    public User getUserByEmail(String email) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}