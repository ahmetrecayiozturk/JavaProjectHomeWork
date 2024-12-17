package org.example.gui;

import javax.swing.*;

public class CustomerPanel extends JPanel {
    public CustomerPanel() {
        // UI components for customer management
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Müşteri Yönetimi"));
        // Add more components as needed
    }
}