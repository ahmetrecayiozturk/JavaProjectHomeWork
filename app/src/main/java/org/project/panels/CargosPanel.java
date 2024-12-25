package org.project.panels;

import org.project.models.Cargo;
import org.project.models.Order;
import org.project.services.CargoService;
import org.project.services.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CargosPanel extends JPanel {
    private CargoService cargoService;
    private OrderService orderService;
    private List<Cargo> cargos;

    public CargosPanel(CargoService cargoService, OrderService orderService) {
        this.cargoService = cargoService;
        this.orderService = orderService;
        this.cargos = cargoService.getAllCargos();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadCargos();
    }

    private void loadCargos() {
        removeAll(); // Mevcut tüm bileşenleri kaldır
        for (Cargo cargo : cargos) {
            addCard(cargo);
        }
        revalidate(); // Bileşenleri yeniden doğrula
        repaint(); // Paneli yeniden çiz
    }

    private void addCard(Cargo cargo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Cargo ID: " + cargo.getId());
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);

        JButton detailsButton = new JButton("Details");
        detailsButton.addActionListener(e -> showDetailsDialog(cargo));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            cargoService.delete(cargo.getId());
            refresh();
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> showUpdateDialog(cargo));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Cargo Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Update Cargo", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Order> orderComboBox = new JComboBox<>(orderService.getAllOrders().toArray(new Order[0]));
        orderComboBox.setSelectedItem(cargo.getEntity());
        JCheckBox isDeliveredCheckBox = new JCheckBox();
        isDeliveredCheckBox.setSelected(cargo.isDelivered());

        formPanel.add(new JLabel("Order:"));
        formPanel.add(orderComboBox);
        formPanel.add(new JLabel("Is Delivered:"));
        formPanel.add(isDeliveredCheckBox);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            Order selectedOrder = (Order) orderComboBox.getSelectedItem();
            boolean isDelivered = isDeliveredCheckBox.isSelected();
            if (selectedOrder != null) {
                cargo.setEntity(selectedOrder);
                cargo.setDelivered(isDelivered);
                cargoService.update(cargo);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid Order", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.cargos = cargoService.getAllCargos();
        loadCargos();
    }
}
/*
package org.project.panels;

import org.project.models.Cargo;
import org.project.models.Order;
import org.project.services.CargoService;
import org.project.services.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CargosPanel extends JPanel {
    private CargoService cargoService;
    private OrderService orderService;
    private List<Cargo> cargos;

    public CargosPanel(CargoService cargoService, OrderService orderService) {
        this.cargoService = cargoService;
        this.orderService = orderService;
        this.cargos = cargoService.getAllCargos();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadCargos();
    }

    private void loadCargos() {
        removeAll(); // Mevcut tüm bileşenleri kaldır
        for (Cargo cargo : cargos) {
            addCard(cargo);
        }
        revalidate(); // Bileşenleri yeniden doğrula
        repaint(); // Paneli yeniden çiz
    }

    private void addCard(Cargo cargo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Cargo ID: " + cargo.getId());
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);

        JButton detailsButton = new JButton("Details");
        detailsButton.addActionListener(e -> showDetailsDialog(cargo));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            cargoService.delete(cargo.getId());
            refresh();
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> showUpdateDialog(cargo));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);
        card.add(buttonPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Cargo Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Update Cargo", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Order> orderComboBox = new JComboBox<>(orderService.getAllOrders().toArray(new Order[0]));
        orderComboBox.setSelectedItem(cargo.getEntity());
        JCheckBox isDeliveredCheckBox = new JCheckBox();
        isDeliveredCheckBox.setSelected(cargo.isDelivered());

        formPanel.add(new JLabel("Order:"));
        formPanel.add(orderComboBox);
        formPanel.add(new JLabel("Is Delivered:"));
        formPanel.add(isDeliveredCheckBox);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            Order selectedOrder = (Order) orderComboBox.getSelectedItem();
            boolean isDelivered = isDeliveredCheckBox.isSelected();
            if (selectedOrder != null) {
                cargo.setEntity(selectedOrder);
                cargo.setDelivered(isDelivered);
                cargoService.update(cargo);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid Order", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.cargos = cargoService.getAllCargos();
        loadCargos();
    }
}*/
/*
package org.project.panels;

import org.project.models.Cargo;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CargosPanel extends JPanel {
    private CargoService cargoService;
    private List<Cargo> cargos;

    public CargosPanel(CargoService cargoService) {
        this.cargoService = cargoService;
        this.cargos = cargoService.getAllCargos();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadCargos();
    }

    private void loadCargos() {
        for (Cargo cargo : cargos) {
            addCard(cargo);
        }
    }

    private void addCard(Cargo cargo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Cargo ID: " + cargo.getId());
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(cargo));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Cargo Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    public void refresh() {
        this.cargos = cargoService.getAllCargos();
        loadCargos();
    }
}
*/
/*package org.project.components;

import org.project.models.Cargo;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CargosPanel extends JPanel {
    private CargoService cargoService;
    private List<Cargo> cargos;

    public CargosPanel(CargoService cargoService) {
        this.cargoService = cargoService;
        this.cargos = cargoService.getAllCargos();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadCargos();
    }

    private void loadCargos() {
        for (Cargo cargo : cargos) {
            addCard(cargo);
        }
    }

    private void addCard(Cargo cargo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Cargo ID: " + cargo.getId());
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(cargo));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Cargo Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}*/