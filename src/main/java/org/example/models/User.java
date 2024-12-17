package org.example.models;

public class User {
	//public static int id;
	int id;
	String email,role,name,surname,secretQuestion,password;
	public User(int id, String password, String email, String role, String name, String surname, String secretQuestion){
		this.id = id;
		this.password = password;
		this.email = email;
		this.role = role;
		this.name = name;
		this.surname = surname;
		this.secretQuestion = secretQuestion;
	}
	public static void login() {

	}
	public static void changePassword() {

	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getSecretQuestion() {
		return secretQuestion;
	}
	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}}
/*
package org.example.models;

public class User {
	private String id;
	private String username;
	private String password;
	private boolean isAdmin;

	// Getters and Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
*/

/*
package org.example.models;

public class User {
	//public static int id;
	int id;
	String email,role,name,surname,secretQuestion,password;
	User(int id, String password, String email, String role, String name, String surname, String secretQuestion){
		 this.id = id;
		 this.password = password;
		 this.email = email;
		 this.role = role;
		 this.name = name;
		 this.surname = surname;
		 this.secretQuestion = secretQuestion;
	}
	public static void login() {
		
	}
	public static void changePassword() {
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getSecretQuestion() {
		return secretQuestion;
	}
	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}
	
}*/
