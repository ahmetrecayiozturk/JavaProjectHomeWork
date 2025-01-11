package org.project.frames.entry.login;

import org.project.App;
import org.project.models.User;
import org.project.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {
    public LoginPanel(JFrame frame,CardLayout cardLayout, JPanel cardPanel) {
        setBackground(Color.BLUE);
        setLayout(null);

        JLabel loginLabel = new JLabel("LOGIN");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setForeground(Color.WHITE);

        JLabel emailLabel = new JLabel("Email");
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(150, 30));
        emailLabel.setForeground(Color.WHITE);

        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(150, 30));
        passwordLabel.setForeground(Color.WHITE);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.addActionListener(e -> {
            if (emailField.getText().equals("") || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(frame, "Please fill all the required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());
                User user=UserService.authenticate(email, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    App.switchToHomeFrame();
                }else{
                    JOptionPane.showMessageDialog(frame, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(227, 214, 214));
                loginButton.setForeground(new Color(222, 49, 99));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(Color.WHITE);
                loginButton.setForeground(Color.BLACK);
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "registerPage");
        });
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(227, 214, 214));
                registerButton.setForeground(new Color(222, 49, 99));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(Color.WHITE);
                registerButton.setForeground(Color.BLACK);
            }
        });

        loginLabel.setBounds(95, 20, 150, 40);
        emailLabel.setBounds(50, 80, 120, 30);
        emailField.setBounds(170, 80, 150, 30);
        passwordLabel.setBounds(50, 125, 120, 30);
        passwordField.setBounds(170, 125, 150, 30);
        loginButton.setBounds(75, 180, 100, 40);
        registerButton.setBounds(195, 180, 100, 40);

        add(loginLabel);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(registerButton);

        setPreferredSize(new Dimension(370, 300));
    }
}
