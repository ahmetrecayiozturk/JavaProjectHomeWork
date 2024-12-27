package org.project.frames.entry.register;

import org.project.models.User;
import org.project.services.UserService;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    public RegisterPanel(CardLayout cardLayout, JPanel cardPanel) {
        setBackground(Color.BLUE);
        setLayout(null);

        JLabel registerLabel = new JLabel("Register");
        registerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel emailLabel = new JLabel("Email");
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(150, 30));

        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(150, 30));

        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(new Dimension(150, 30));



        JButton backTologinButton = new JButton("Back to Login");
        backTologinButton.setFont(new Font("Arial", Font.BOLD, 14));
        backTologinButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "loginPage");
        });

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.addActionListener(e -> {
            if(emailField.getText().equals("") || passwordField.getPassword().length==0 || confirmPasswordField.getPassword().length==0) {
                JOptionPane.showMessageDialog(null, "Please fill all the required fields");
            }else if(passwordField.getPassword().equals(confirmPasswordField.getPassword())) {
                JOptionPane.showMessageDialog(null, "Passwords do not match");
            } else{
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());
                User user = new User(email, password);
                Boolean isUserCreated=UserService.addUser(user);
                if(isUserCreated) {
                    JOptionPane.showMessageDialog(null, "Successfully registered");
                    cardLayout.show(cardPanel, "loginPage");
                }else {
                    JOptionPane.showMessageDialog(null,"Email already exists");
                }

            }
        });

        registerLabel.setBounds(90, 20, 150, 40);
        emailLabel.setBounds(50, 80, 120, 30);
        emailField.setBounds(170, 80, 150, 30);
        passwordLabel.setBounds(50, 125, 120, 30);
        passwordField.setBounds(170, 125, 150, 30);
        confirmPasswordLabel.setBounds(50, 170, 120, 30);
        confirmPasswordField.setBounds(170, 170, 150, 30);
        backTologinButton.setBounds(55, 225, 140, 40);
        registerButton.setBounds(215, 225, 100, 40);

        add(registerLabel);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(confirmPasswordLabel);
        add(confirmPasswordField);
        add(backTologinButton);
        add(registerButton);

        setPreferredSize(new Dimension(370, 300));
    }
}
