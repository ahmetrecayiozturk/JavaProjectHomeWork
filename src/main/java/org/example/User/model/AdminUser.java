package org.example.User.model;
import org.example.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminUser extends User {
	//buraya adminin ek özellikleri gelecek
	String permissions;
	List<User> createdUsers;
	LocalDateTime lastLogin;
   
	AdminUser(int id, String password, String email, String role, String name, String surname, String secretQuestion, String permissions) {
		super(id, password, email, role, name, surname, secretQuestion);
        this.permissions = permissions;
        this.createdUsers = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}
	 public void addUser(User user) {
	        createdUsers.add(user);
	        System.out.println(user.getName() + " kullanıcı olarak eklendi.");
	    }

	    public void deleteUser(int userId) {
	        // Kullanıcı silme mantığı.
	    }

	    public void generateReports() {
	        System.out.println("Raporlar oluşturuluyor...");
	    }



}
