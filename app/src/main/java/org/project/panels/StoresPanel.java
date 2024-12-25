package org.project.panels;

import org.project.models.Store;
import org.project.services.StoreService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StoresPanel extends JPanel {
    private StoreService storeService;
    private List<Store> stores;

    public StoresPanel(StoreService storeService) {
        this.storeService = storeService;
        this.stores = storeService.getAllStores();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadStores();
    }

    private void loadStores() {
        removeAll(); // Mevcut tüm bileşenleri kaldır
        for (Store store : stores) {
            addCard(store);
        }
        revalidate(); // Bileşenleri yeniden doğrula
        repaint(); // Paneli yeniden çiz
    }

    private void addCard(Store store) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Store ID: " + store.getId());
        JTextArea contentArea = new JTextArea(store.toString());
        contentArea.setEditable(false);

        JButton detailsButton = new JButton("Details");
        detailsButton.addActionListener(e -> showDetailsDialog(store));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            storeService.delete(store.getId());
            refresh();
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> showUpdateDialog(store));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Store store) {
        JDialog dialog = new JDialog((Frame) null, "Store Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(store.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Store store) {
        JDialog dialog = new JDialog((Frame) null, "Update Store", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField nameField = new JTextField(store.getName());
        JTextField addressField = new JTextField(store.getAddress());
        JTextField phoneField = new JTextField(store.getPhone());

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            String name = nameField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            if (!name.isEmpty() && !address.isEmpty() && !phone.isEmpty()) {
                store.setName(name);
                store.setAddress(address);
                store.setPhone(phone);
                storeService.update(store);
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

    public void refresh() {
        this.stores = storeService.getAllStores();
        loadStores();
    }
}