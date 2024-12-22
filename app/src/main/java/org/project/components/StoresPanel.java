package org.project.components;

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
        for (Store store : stores) {
            addCard(store);
        }
    }

    private void addCard(Store store) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Store ID: " + store.getId());
        JTextArea contentArea = new JTextArea(store.toString());
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(store));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

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
}