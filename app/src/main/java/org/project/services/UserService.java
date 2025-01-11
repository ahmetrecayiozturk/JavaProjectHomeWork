package org.project.services;

import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.User;

import java.util.List;

public class UserService {
    private static final JsonRepository<User> userRepository = new JsonRepository<>(User[].class);

    public UserService() {}

    // The addUser method checks whether the user to be added already exists using the getUserByEmail() method.
    // If the user does not exist, it is saved.
    public static boolean addUser(User user) {
        User user4 = getUserByEmail(user.getEmail());
        if (user4 != null) {
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

    // This method handles user authentication. First, the userRepository object is created in this service,
    // and then all users are retrieved. A loop iterates through these users, and if the email and password
    // provided match any user's email and password in the retrieved list, the user is successfully logged in.
    public static User authenticate(String email, String password) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                App.setCurrentUser(user);
                return user;
            }
        }
        return null;
    }

    // This method retrieves the user whose email matches the provided email parameter from the list of all users.
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
