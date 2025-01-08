package org.project.frames.entry.register;

import org.project.App;
import org.project.models.Store;
import org.project.models.User;
import org.project.services.StoreService;
import org.project.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.stream.Collectors;

public class RegisterPanel extends JPanel {
    // Services for interacting with stores and users
    private StoreService storeService = new StoreService();
    private UserService userService = new UserService();

    // Constructor to initialize the RegisterPanel
    public RegisterPanel(CardLayout cardLayout, JPanel cardPanel) {
        // Set the background color of the panel
        setBackground(new Color(222, 49, 99));
        // Set the layout manager to GridBagLayout
        setLayout(new GridBagLayout());
        // Set the panel to be non-opaque
        setOpaque(false);

        // Create GridBagConstraints for layout management
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        // Create and configure the register label
        JLabel registerLabel = new JLabel("Register");
        registerLabel.setFont(new Font("montserrat", Font.BOLD, 36));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setForeground(Color.WHITE);

        // Create and configure the email label and field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("montserrat", Font.BOLD, 20));
        emailLabel.setForeground(Color.WHITE);

        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(250, 40));

        // Create and configure the password label and field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("montserrat", Font.BOLD, 20));
        passwordLabel.setForeground(Color.WHITE);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 40));

        // Create and configure the confirm password label and field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("montserrat", Font.BOLD, 20));
        confirmPasswordLabel.setForeground(Color.WHITE);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(new Dimension(250, 40));

        // Create and configure the store label and dropdown
        JLabel storeLabel = new JLabel("Select Store");
        storeLabel.setFont(new Font("montserrat", Font.BOLD, 20));
        storeLabel.setForeground(Color.WHITE);

        JComboBox<String> storeDropdown = new JComboBox<>(storeService.getAllStores().stream().map(Store::getName).toArray(String[]::new));
        storeDropdown.setPreferredSize(new Dimension(250, 40));

        // Create and configure the new store label and field
        JLabel newStoreLabel = new JLabel("New Store Name");
        newStoreLabel.setFont(new Font("montserrat", Font.BOLD, 20));
        newStoreLabel.setForeground(Color.WHITE);
        JTextField newStoreField = new JTextField();
        newStoreField.setPreferredSize(new Dimension(250, 40));
        newStoreLabel.setVisible(false);
        newStoreField.setVisible(false);

        // Create and configure radio buttons for store options
        JRadioButton createStoreButton = new JRadioButton("Create New Store");
        createStoreButton.setFont(new Font("montserrat", Font.BOLD, 16));
        createStoreButton.setForeground(Color.WHITE);
        createStoreButton.setOpaque(false);

        JRadioButton joinStoreButton = new JRadioButton("Join to Store");
        joinStoreButton.setFont(new Font("montserrat", Font.BOLD, 16));
        joinStoreButton.setForeground(Color.WHITE);
        joinStoreButton.setOpaque(false);

        // Group the radio buttons
        ButtonGroup storeGroup = new ButtonGroup();
        storeGroup.add(createStoreButton);
        storeGroup.add(joinStoreButton);

        // Add action listeners to the radio buttons
        createStoreButton.addActionListener(e -> {
            newStoreLabel.setVisible(true);
            newStoreField.setVisible(true);
            storeDropdown.setVisible(false);
            storeLabel.setVisible(false);
        });

        joinStoreButton.addActionListener(e -> {
            newStoreLabel.setVisible(false);
            newStoreField.setVisible(false);
            storeDropdown.setVisible(true);
            storeLabel.setVisible(true);
        });

        // Set default selection for the radio buttons
        joinStoreButton.setSelected(true);
        storeDropdown.setVisible(true);
        newStoreLabel.setVisible(false);
        newStoreField.setVisible(false);

        // Create and configure the back to login button
        JButton backTologinButton = new JButton("Back to Login");
        backTologinButton.setFont(new Font("montserrat", Font.BOLD, 18));
        backTologinButton.setPreferredSize(new Dimension(170, 50));
        backTologinButton.setBackground(Color.WHITE);
        backTologinButton.setForeground(Color.BLACK);
        backTologinButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        // Add action listener to the back to login button
        backTologinButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "loginPage");
        });

        // Add mouse listener to change button colors on hover
        backTologinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backTologinButton.setBackground(new Color(227, 214, 214)); // Change background color on hover
                backTologinButton.setForeground(new Color(222, 49, 99)); // Change text color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backTologinButton.setBackground(Color.WHITE); // Reset background color
                backTologinButton.setForeground(Color.BLACK); // Reset text color
            }
        });

        // Create and configure the register button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("montserrat", Font.BOLD, 18));
        registerButton.setPreferredSize(new Dimension(170, 50));
        registerButton.setBackground(Color.WHITE); // Default background color
        registerButton.setForeground(Color.BLACK); // Default text color
        registerButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        // Add action listener to the register button
        registerButton.addActionListener(e -> {
            // Validate input fields
            if (emailField.getText().equals("") || passwordField.getPassword().length == 0 || confirmPasswordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(null, "Please fill all the required fields");
            } else if (!String.valueOf(passwordField.getPassword()).equals(String.valueOf(confirmPasswordField.getPassword()))) {
                JOptionPane.showMessageDialog(null, "Passwords do not match");
            } else {
                // Get user input
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());
                User user = new User(email, password, 15);

                // Determine the selected store
                String selectedStore = createStoreButton.isSelected() ? newStoreField.getText() : (String) storeDropdown.getSelectedItem();

                if (selectedStore == null || selectedStore.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please provide a store name");
                    return;
                }

                // Handle store creation or joining
                if (createStoreButton.isSelected()) {
                    String storeName = newStoreField.getText();
                    Store store = new Store(storeName, "This is your store address!", "This is your store phone number!", "This is your store description!", "");

                    storeService.add(store);
                    user.setStoreId(store.getId());
                } else {
                    String selectedStoreName = (String) storeDropdown.getSelectedItem();
                    Store store = storeService.getStoreByName(selectedStoreName);
                    user.setStoreId(store.getId());
                }

                // Add the user to the repository
                boolean isUserCreated = userService.addUser(user);
                if (isUserCreated) {
                    JOptionPane.showMessageDialog(null, "Successfully registered for store: " + selectedStore);
                    cardLayout.show(cardPanel, "loginPage");
                } else {
                    JOptionPane.showMessageDialog(null, "Email already exists");
                }
            }
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
        gbc.insets = new Insets(0, 0, 30, 0);
        add(registerLabel, gbc);

        gbc.gridwidth = 1; // Reset to single column
        gbc.gridy++; // Move to next row
        gbc.anchor = GridBagConstraints.LINE_END;
        add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(storeLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(storeDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(newStoreLabel, gbc);

        gbc.gridx = 1;
        add(newStoreField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(createStoreButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 40);
        add(joinStoreButton, gbc);

        gbc.gridwidth = 1; // Reset to single column
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(20, 20, 0, 0);
        add(registerButton, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(20, 100, 0, 0);
        add(backTologinButton, gbc);

        // Set the preferred size of the panel
        setPreferredSize(new Dimension(500, 700));
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