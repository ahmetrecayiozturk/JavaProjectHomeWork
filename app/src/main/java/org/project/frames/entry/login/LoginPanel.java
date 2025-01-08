package org.project.frames.entry.login;

import org.project.App;
import org.project.models.User;
import org.project.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {
    // Service for interacting with users
    private UserService userService = new UserService();

    // Constructor to initialize the LoginPanel
    public LoginPanel(JFrame frame, CardLayout cardLayout, JPanel cardPanel) {
        // Set the background color of the panel
        setBackground(new Color(10, 50, 211));
        // Set the panel to be non-opaque
        setOpaque(false);
        // Set the layout manager to GridBagLayout
        setLayout(new GridBagLayout());

        // Create GridBagConstraints for layout management
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        // Create and configure the login label
        JLabel loginLabel = new JLabel("LOGIN");
        loginLabel.setFont(new Font("montserrat", Font.BOLD, 36)); // Larger font
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setForeground(Color.WHITE);

        // Create and configure the email label and field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("montserrat", Font.BOLD, 20)); // Larger font
        emailLabel.setForeground(Color.WHITE);

        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(250, 40)); // Larger size

        // Create and configure the password label and field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("montserrat", Font.BOLD, 20)); // Larger font
        passwordLabel.setForeground(Color.WHITE);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 40)); // Larger size

        // Create and configure the login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("montserrat", Font.BOLD, 18)); // Larger font
        loginButton.setPreferredSize(new Dimension(140, 50));
        loginButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        // Add action listener to the login button
        loginButton.addActionListener(e -> {
            // Validate input fields
            if (emailField.getText().equals("") || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(frame, "Please fill all the required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Get user input
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());
                User user = userService.authenticate(email, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    App.switchToHomeFrame();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add mouse listener to change button colors on hover
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(227, 214, 214)); // Change background color on hover
                loginButton.setForeground(new Color(222, 49, 99)); // Change text color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(Color.WHITE); // Reset background color
                loginButton.setForeground(Color.BLACK); // Reset text color
            }
        });

        // Create and configure the register button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("montserrat", Font.BOLD, 18)); // Larger font
        registerButton.setPreferredSize(new Dimension(140, 50));
        registerButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        // Add action listener to the register button
        registerButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "registerPage");
        });

        // Add mouse listener to change button colors on hover
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(227, 214, 214)); // Change background color on hover
                registerButton.setForeground(new Color(222, 49, 99)); // Change text color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(Color.WHITE); // Reset background color
                registerButton.setForeground(Color.BLACK); // Reset text color
            }
        });

        // Add components to the panel using GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(loginLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 40, 30);
        add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 30, 40, 0);
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 20, 25, 30);
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 30, 25, 0);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(35, 20, 0, 0);
        add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(35, 100, 0, 0); // Add padding to the left to shift the register button to the right
        add(registerButton, gbc);

        // Set the preferred size of the panel
        setPreferredSize(new Dimension(580, 400));
    }

    // Override the paintComponent method to draw a rounded rectangle background
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        int arcWidth = 40; // Width of the rounded corners
        int arcHeight = 40; // Height of the rounded corners
        RoundRectangle2D roundRectangle = new RoundRectangle2D.Float(0, 0, width, height, arcWidth, arcHeight);
        g2.setColor(getBackground());
        g2.fill(roundRectangle);
        g2.dispose();
    }
}