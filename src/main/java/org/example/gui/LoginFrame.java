package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.example.models.User;
import org.example.User.service.UserService;

public class LoginFrame extends JFrame {
    private UserService userService;

    public LoginFrame(UserService userService) {
        this.userService = userService;

        setTitle("Giriş");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Kullanıcı Adı:");
        JTextField userText = new JTextField();
        JLabel passwordLabel = new JLabel("Şifre:");
        JPasswordField passwordText = new JPasswordField();

        JButton loginButton = new JButton("Giriş");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                User user = userService.authenticate(username, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(null, "Giriş başarılı!");
                    MainFrame mainFrame = new MainFrame(user);
                    mainFrame.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Geçersiz kullanıcı adı veya şifre!");
                }
            }
        });

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {

    }
}