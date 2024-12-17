package org.example.gui;

import javax.swing.*;

public class ProductPanel extends JPanel {
    public ProductPanel() {
        // UI components for product management
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Ürün Yönetimi"));
        // Add more components as needed
    }
}