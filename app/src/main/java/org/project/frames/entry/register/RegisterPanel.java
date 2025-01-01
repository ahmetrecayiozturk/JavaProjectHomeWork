package org.project.frames.entry.register;

import org.project.App;
import org.project.models.Store;
import org.project.models.User;
import org.project.services.StoreService;
import org.project.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

public class RegisterPanel extends JPanel {
    private StoreService storeService=new StoreService();
    private UserService userService=new UserService();

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

        JLabel storeLabel = new JLabel("Select Store");
        JComboBox<String> storeDropdown = new JComboBox<>(storeService.getAllStores().stream().map(Store::getName).toArray(String[]::new));
        storeDropdown.setPreferredSize(new Dimension(150, 30));

        JLabel newStoreLabel = new JLabel("New Store Name");
        JTextField newStoreField = new JTextField();
        newStoreField.setPreferredSize(new Dimension(150, 30));
        newStoreLabel.setVisible(false);
        newStoreField.setVisible(false);

        JRadioButton createStoreButton = new JRadioButton("Create New Store");
        createStoreButton.setBackground(Color.BLUE);

        JRadioButton joinStoreButton = new JRadioButton("Join to Store");
        joinStoreButton.setBackground(Color.BLUE);

        ButtonGroup storeGroup = new ButtonGroup();
        storeGroup.add(createStoreButton);
        storeGroup.add(joinStoreButton);

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
            newStoreLabel.setVisible(true);
        });

        joinStoreButton.setSelected(true);
        storeDropdown.setVisible(true);
        newStoreLabel.setVisible(false);
        newStoreField.setVisible(false);

        JButton backTologinButton = new JButton("Back to Login");
        backTologinButton.setFont(new Font("Arial", Font.BOLD, 14));
        backTologinButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "loginPage");
        });

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.addActionListener(e -> {
            if (emailField.getText().equals("") || passwordField.getPassword().length == 0 || confirmPasswordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(null, "Please fill all the required fields");
            } else if (!String.valueOf(passwordField.getPassword()).equals(String.valueOf(confirmPasswordField.getPassword()))) {
                JOptionPane.showMessageDialog(null, "Passwords do not match");
            } else {
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());
                User user = new User(email, password, 15);

               String selectedStore=createStoreButton.isSelected() ? newStoreField.getText() : (String) storeDropdown.getSelectedItem();

                if (selectedStore == null || selectedStore.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please provide a store name");
                    return;
                }

                if(createStoreButton.isSelected()) {
                    String storeName = newStoreField.getText();
                    Store store = new Store(storeName,"This is your store address!","This is your store phone number!","This is your store description!", "");

                    storeService.add(store);
                    user.setStoreId(store.getId());
                } else {
                    String selectedStoreName = (String) storeDropdown.getSelectedItem();
                    Store store = storeService.getStoreByName(selectedStoreName);
                    user.setStoreId(store.getId());
                }

                boolean isUserCreated = userService.addUser(user);
                if (isUserCreated) {
                    JOptionPane.showMessageDialog(null, "Successfully registered for store: " + selectedStore);
                    cardLayout.show(cardPanel, "loginPage");
                } else {
                    JOptionPane.showMessageDialog(null, "Email already exists");
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
        storeLabel.setBounds(50, 250, 120, 30);
        storeDropdown.setBounds(170, 250, 150, 30);
        newStoreLabel.setBounds(50, 250, 120, 30);
        newStoreField.setBounds(170, 250, 150, 30);
        createStoreButton.setBounds(50, 210, 150, 30);
        joinStoreButton.setBounds(210, 210, 150, 30);
        backTologinButton.setBounds(55, 290, 140, 40);
        registerButton.setBounds(215, 290, 100, 40);

        add(registerLabel);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(confirmPasswordLabel);
        add(confirmPasswordField);
        add(storeLabel);
        add(storeDropdown);
        add(newStoreLabel);
        add(newStoreField);
        add(createStoreButton);
        add(joinStoreButton);
        add(backTologinButton);
        add(registerButton);

        setPreferredSize(new Dimension(370, 380));
    }
}
