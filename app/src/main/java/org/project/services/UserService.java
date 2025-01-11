package org.project.services;

import org.project.App;
import org.project.data.JsonRepository;
import org.project.models.User;

import java.util.List;

public class UserService {
    private static final JsonRepository<User> userRepository=new JsonRepository<>( User[].class);

    public UserService() {}
    //eklenecek parametrae olarak girilen user halihazırda kayıtlı mı onun kontrol edilmesi getUserByEmail() methodu ile yapılır
    //eğer user yok ise kaydedilir.
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
    //Burada kullanıcı auth işlemi yapılmaktadır, önce bu service de userRepository nesnesi oluşturuyoruz ve ardından da tüm
    //da tüm kullanıcılar döndürülüyor. Bu kullanıcılar içerisinde bir for işlemi yapılıyor ve o kullanıcının
    //emaili ve şifresine döndürülen kullanıcılar içerisindeki herhangi bir kullanıcının emaili ve şifresi eşitleniyor ise
    //kullanıcı giriş yapabilmiş oluyor
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
    //Ddöndürülen tüm kullanıcılar arasından girilen email parametresi eşlenen kullanıcı döndürülüyor
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