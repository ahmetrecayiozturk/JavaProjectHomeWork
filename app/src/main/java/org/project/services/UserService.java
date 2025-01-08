package org.project.services;

import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.Store;
import org.project.models.User;

import java.util.List;

public class UserService {
    //we are creating an user repository with JsonRepository class
    private static JsonRepository<User> userRepository=new JsonRepository<>( User[].class);

    public UserService() {}
    //creating user
    public static boolean addUser(User user) {
        //we check user if null
        User user4=getUserByEmail(user.getEmail());
        if(user4 != null) {
            return false;
        }
        //if not null, we save user to user repository
        userRepository.save(user);
        return true;
    }
    //update user due to JsonRepository Class
    public static void updateUser(User user) {
        userRepository.update(user);
    }

    //delete user due to JsonRepository Class
    public static void deleteUser(Integer userId) {
        userRepository.delete(userId);
    }

    //get all user due to JsonRepository Class
    public static List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //auth user due to JsonRepository Class
    public static User authenticate(String email, String password) {
        //we take all the users and put them all in a list
        List<User> users = userRepository.findAll();
        //we are filtering user by email
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                //we set user as a current user
                App.setCurrentUser(user);
                return user;
            }
        }
        return null;
    }

    //get user by email due to JsonRepository Class
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