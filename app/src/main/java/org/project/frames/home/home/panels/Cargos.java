package org.project.frames.home.home.panels;

import org.project.models.Cargo;
import org.project.models.Order;
import org.project.services.CargoService;
import org.project.services.OrderService;
import org.project.data.JsonRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Cargos extends JPanel {
    private CargoService cargoService;
    private OrderService orderService;
    private List<Cargo> cargos;
    private List<Order> orders;
    private JPanel cargosListPanel;

    public Cargos() {
        this.cargoService = new CargoService(new JsonRepository<>(Cargo[].class));
        this.orderService = new OrderService(new JsonRepository<>(Order[].class));
        this.cargos = cargoService.getAllCargos();
        this.orders = orderService.getAllOrders();
        setLayout(new BorderLayout());

        cargosListPanel = new JPanel(new GridBagLayout());
        loadCargos();

        JScrollPane scrollPane = new JScrollPane(cargosListPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCargos() {
        cargosListPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH; // Kartların üstten başlamasını sağlar
        gbc.insets = new Insets(10, 10, 10, 10);

        for (Cargo cargo : cargos) {
            cargosListPanel.add(createCargoPanel(cargo), gbc);
            gbc.gridy++;
        }
        revalidate();
        repaint();
    }

    private JPanel createCargoPanel(Cargo cargo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("Cargo ID: " + cargo.getId());
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        infoPanel.add(titleLabel);
        infoPanel.add(new JScrollPane(contentArea));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.addActionListener(e -> showDetailsDialog(cargo));
        updateButton.addActionListener(e -> showUpdateDialog(cargo));
        deleteButton.addActionListener(e -> {
            cargoService.delete(cargo.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showDetailsDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Cargo Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Cargo", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Order> orderComboBox = new JComboBox<>(orders.toArray(new Order[0]));
        JCheckBox isDeliveredCheckBox = new JCheckBox();

        formPanel.add(new JLabel("Order:"));
        formPanel.add(orderComboBox);
        formPanel.add(new JLabel("Is Delivered:"));
        formPanel.add(isDeliveredCheckBox);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            Order order = (Order) orderComboBox.getSelectedItem();
            boolean isDelivered = isDeliveredCheckBox.isSelected();
            if (order != null) {
                Cargo cargo = new Cargo(isDelivered, order);
                cargoService.add(cargo);
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

    private void showUpdateDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Update Cargo", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Order> orderComboBox = new JComboBox<>(orders.toArray(new Order[0]));
        JCheckBox isDeliveredCheckBox = new JCheckBox();

        orderComboBox.setSelectedItem(cargo.getEntity());
        isDeliveredCheckBox.setSelected(cargo.isDelivered());

        formPanel.add(new JLabel("Order:"));
        formPanel.add(orderComboBox);
        formPanel.add(new JLabel("Is Delivered:"));
        formPanel.add(isDeliveredCheckBox);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            Order order = (Order) orderComboBox.getSelectedItem();
            boolean isDelivered = isDeliveredCheckBox.isSelected();
            if (order != null) {
                cargo.setEntity(order);
                cargo.setDelivered(isDelivered);
                cargoService.update(cargo);
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
        this.cargos = cargoService.getAllCargos();
        loadCargos();
    }
}