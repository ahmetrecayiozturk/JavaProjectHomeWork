package org.project.frames.home.panels;

import org.project.models.Cargo;
import org.project.models.Order;
import org.project.services.CargoService;
import org.project.services.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Cargos extends JPanel {
    // Constants for cargo panel dimensions
    private static final int CARGO_MAX_WIDTH = 1000;
    private static final int CARGO_HEIGHT = 50;
    // List to store cargos and orders
    private List<Cargo> cargos;
    private List<Order> orders;
    // Panels for displaying cargo list and scroll pane
    private JPanel cargoListPanel;
    private JScrollPane scrollPane;

    // Constructor to initialize the Cargos panel
    public Cargos() {
        this.cargos = CargoService.getAllCargos();
        this.orders = OrderService.getAllOrders();
        initialize();
    }

    // Method to initialize the panel components
    private void initialize() {
        setLayout(new BorderLayout());

        // Initialize the cargo list panel with a vertical box layout
        cargoListPanel = new JPanel();
        cargoListPanel.setLayout(new BoxLayout(cargoListPanel, BoxLayout.Y_AXIS));

        // Initialize the scroll pane for the cargo list panel
        scrollPane = new JScrollPane(cargoListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(CARGO_MAX_WIDTH, 600));
        add(scrollPane, BorderLayout.CENTER);

        // Add cargos to the list panel
        addCargos();

        // Initialize the button panel for adding new cargos
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        // Add button to add new cargos
        JButton addButton = new JButton("Add");
        styleAddButton(addButton);
        addButton.setPreferredSize(new Dimension(300, 100)); // Increase size by 2.5 times
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to add cargos to the list panel
    private void addCargos() {
        cargoListPanel.removeAll();
        for (Cargo cargo : cargos) {
            cargoListPanel.add(createCargoPanel(cargo));
        }
        cargoListPanel.revalidate();
        cargoListPanel.repaint();
    }

    // Method to create a panel for each cargo
    private JPanel createCargoPanel(Cargo cargo) {
        JPanel cargoPanel = new JPanel(new BorderLayout());
        cargoPanel.setPreferredSize(new Dimension(CARGO_MAX_WIDTH, CARGO_HEIGHT));
        cargoPanel.setMaximumSize(new Dimension(CARGO_MAX_WIDTH, CARGO_HEIGHT));
        cargoPanel.setBackground(cargo.isDelivered() ? Color.GREEN : Color.RED);

        JLabel cargoLabel = new JLabel("Cargo ID: " + cargo.getId() + " | Delivered: " + cargo.isDelivered() + " | Returned: " + cargo.isReturned());
        cargoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        cargoPanel.add(cargoLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton returnButton = new JButton("Return");
        JButton deliveredButton = new JButton("Delivered");

        styleButton(detailsButton);
        styleButton(updateButton);
        styleButton(deleteButton, new Color(255, 69, 0));
        styleButton(returnButton);
        styleButton(deliveredButton);

        // Initially disable the deleteButton
        deleteButton.setEnabled(false);

        detailsButton.addActionListener(e -> showDetailsDialog(cargo));
        updateButton.addActionListener(e -> showUpdateDialog(cargo));
        deleteButton.addActionListener(e -> {
            CargoService.delete(cargo.getId());
            refresh();
        });
        returnButton.addActionListener(e -> {
            CargoService.returnCargo(cargo.getId());
            cargo.setReturned(true);
            CargoService.update(cargo);
            refresh();
        });
        deliveredButton.addActionListener(e -> {
            CargoService.markAsDeliveredById(cargo.getId());
            cargo.setDelivered(true);
            CargoService.update(cargo);
            refresh();
        });

        // Enable deleteButton if cargo is delivered or returned
        if (cargo.isDelivered() || cargo.isReturned()) {
            deleteButton.setEnabled(true);
        }

        // Disable other buttons if cargo is delivered or returned
        if (cargo.isDelivered() || cargo.isReturned()) {
            detailsButton.setEnabled(false);
            updateButton.setEnabled(false);
            returnButton.setEnabled(false);
            deliveredButton.setEnabled(false);
        }

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(deliveredButton);

        cargoPanel.add(buttonPanel, BorderLayout.EAST);

        return cargoPanel;
    }

    // Method to show the details dialog for a cargo
    private void showDetailsDialog(Cargo cargo) {
        CargoDetail cargoDetail = new CargoDetail((Frame) SwingUtilities.getWindowAncestor(this), cargo);
        cargoDetail.setVisible(true);
    }

    // Method to show the add cargo dialog
    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Cargo", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Order> orderComboBox = new JComboBox<>(orders.toArray(new Order[0]));
        JCheckBox isDeliveredCheckBox = new JCheckBox();
        JCheckBox isReturnedCheckBox = new JCheckBox();

        formPanel.add(new JLabel("Order:"));
        formPanel.add(orderComboBox);
        formPanel.add(new JLabel("Is Delivered:"));
        formPanel.add(isDeliveredCheckBox);
        formPanel.add(new JLabel("Is Returned:"));
        formPanel.add(isReturnedCheckBox);
        JButton addButton = new JButton("Add");
        styleAddButton(addButton);
        addButton.addActionListener(e -> {
            Order order = (Order) orderComboBox.getSelectedItem();
            boolean isDelivered = isDeliveredCheckBox.isSelected();
            boolean isReturned = isReturnedCheckBox.isSelected();
            if (order != null) {
                Cargo cargo = new Cargo(isDelivered, isReturned, order.getId());
                CargoService.add(cargo);
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

    // Method to show the update cargo dialog
    private void showUpdateDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Update Cargo", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Order> orderComboBox = new JComboBox<>(orders.toArray(new Order[0]));
        JCheckBox isDeliveredCheckBox = new JCheckBox();
        JCheckBox isReturnedCheckBox = new JCheckBox();

        orderComboBox.setSelectedItem(cargo.getOrderId());
        isDeliveredCheckBox.setSelected(cargo.isDelivered());
        isReturnedCheckBox.setSelected(cargo.isReturned());

        formPanel.add(new JLabel("Order:"));
        formPanel.add(orderComboBox);
        formPanel.add(new JLabel("Is Delivered:"));
        formPanel.add(isDeliveredCheckBox);
        formPanel.add(new JLabel("Is Returned:"));
        formPanel.add(isReturnedCheckBox);

        JButton updateButton = new JButton("Update");
        styleButton(updateButton);
        updateButton.addActionListener(e -> {
            Order order = (Order) orderComboBox.getSelectedItem();
            boolean isDelivered = isDeliveredCheckBox.isSelected();
            boolean isReturned = isReturnedCheckBox.isSelected();
            if (order != null) {
                cargo.setOrderId(order.getId());
                cargo.setDelivered(isDelivered);
                cargo.setReturned(isReturned);
                CargoService.update(cargo);
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

    // Method to refresh the cargo list
    public void refresh() {
        this.cargos = CargoService.getAllCargos();
        addCargos();
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