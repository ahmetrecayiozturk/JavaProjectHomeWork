package org.project.frames.home.panels;

import org.project.models.Receiver;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Receivers extends JPanel {
    // Constants for receiver panel dimensions
    private static final int RECEIVER_MAX_WIDTH = 1000;
    private static final int RECEIVER_HEIGHT = 50;
    // List to store receivers
    private List<Receiver> receivers;
    // Panels for displaying receiver list and scroll pane
    private JPanel receiverListPanel;
    private JScrollPane scrollPane;

    // Constructor to initialize the Receivers panel
    public Receivers() {
        this.receivers = ReceiverService.getAllReceivers();
        initialize();
    }

    // Method to initialize the panel components
    private void initialize() {
        setLayout(new BorderLayout());

        // Initialize the receiver list panel with a vertical box layout
        receiverListPanel = new JPanel();
        receiverListPanel.setLayout(new BoxLayout(receiverListPanel, BoxLayout.Y_AXIS));

        // Initialize the scroll pane for the receiver list panel
        scrollPane = new JScrollPane(receiverListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(RECEIVER_MAX_WIDTH, 600));
        add(scrollPane, BorderLayout.CENTER);

        // Add receivers to the list panel
        addReceivers();

        // Initialize the button panel for adding new receivers
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        // Add button to add new receivers
        JButton addButton = new JButton("Add");
        styleAddButton(addButton);
        addButton.setPreferredSize(new Dimension(300, 100)); // Increase size by 2.5 times
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to add receivers to the list panel
    private void addReceivers() {
        receiverListPanel.removeAll();
        for (Receiver receiver : receivers) {
            receiverListPanel.add(createReceiverPanel(receiver));
        }
        receiverListPanel.revalidate();
        receiverListPanel.repaint();
    }

    // Method to create a panel for each receiver
    private JPanel createReceiverPanel(Receiver receiver) {
        JPanel receiverPanel = new JPanel(new BorderLayout());
        receiverPanel.setPreferredSize(new Dimension(RECEIVER_MAX_WIDTH, RECEIVER_HEIGHT));
        receiverPanel.setMaximumSize(new Dimension(RECEIVER_MAX_WIDTH, RECEIVER_HEIGHT));
        receiverPanel.setBackground(new Color(100, 150, 100, 255));

        JLabel receiverLabel = new JLabel("Receiver: " + receiver.getName() + " " + receiver.getSurname());
        receiverLabel.setHorizontalAlignment(SwingConstants.LEFT);
        receiverLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        receiverPanel.add(receiverLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        styleButton(detailsButton);
        styleButton(updateButton);
        styleButton(deleteButton, new Color(255, 69, 0));

        detailsButton.addActionListener(e -> showDetailsDialog(receiver));
        updateButton.addActionListener(e -> showUpdateDialog(receiver));
        deleteButton.addActionListener(e -> {
            ReceiverService.delete(receiver.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        receiverPanel.add(buttonPanel, BorderLayout.EAST);

        return receiverPanel;
    }

    // Method to show the details dialog for a receiver
    private void showDetailsDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Receiver Details", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);

        ReceiverDetail receiverDetail = new ReceiverDetail();
        receiverDetail.setReceiver(receiver);

        dialog.add(receiverDetail);
        dialog.setVisible(true);
    }

    // Method to show the add receiver dialog
    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Receiver", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField emailField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField receiverNameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField addressField = new JTextField();

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(receiverNameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton addButton = new JButton("Add");
        styleAddButton(addButton);
        addButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = receiverNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
                Receiver receiver = new Receiver(email, password, name, surname, address);
                ReceiverService.add(receiver);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Method to show the update receiver dialog
    private void showUpdateDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Update Receiver", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField emailField = new JTextField(receiver.getEmail());
        JTextField passwordField = new JTextField(receiver.getPassword());
        JTextField receiverNameField = new JTextField(receiver.getName());
        JTextField surnameField = new JTextField(receiver.getSurname());
        JTextField addressField = new JTextField(receiver.getAddress());

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(receiverNameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton updateButton = new JButton("Update");
        styleButton(updateButton);
        updateButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = receiverNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
                receiver.setEmail(email);
                receiver.setPassword(password);
                receiver.setName(name);
                receiver.setSurname(surname);
                receiver.setAddress(address);
                ReceiverService.update(receiver);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Method to refresh the receiver list
    public void refresh() {
        this.receivers = ReceiverService.getAllReceivers();
        receiverListPanel.removeAll();
        addReceivers();
        revalidate();
        repaint();
    }

    // Method to style buttons
    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    // Method to style buttons with a specific background color
    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    // Method to style the add button
    private void styleAddButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
    }
}