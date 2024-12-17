package org.example.gui;

import javax.swing.*;

public class OrderPanel extends JPanel {
    public OrderPanel() {
        // UI components for order management
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Sipariş Yönetimi"));
        // Add more components as needed
    }
}