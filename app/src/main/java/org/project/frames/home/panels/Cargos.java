package org.project.frames.home.panels;

import org.project.models.Cargo;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.CargoService;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Cargos extends JPanel {
    private static final int CARGO_MAX_WIDTH = 1000;
    private static final int CARGO_HEIGHT = 50;
    private List<Cargo> cargos;
    private List<Order> orders;
    private JPanel cargosPanel;
    private JPanel mainPanel;

    public Cargos() {
        this.cargos = CargoService.getAllCargosForCurrentStore();
        this.orders = OrderService.getAllOrdersForCurrentStore();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        
        mainPanel = new JPanel(new BorderLayout());
        
        cargosPanel = new JPanel();
        cargosPanel.setLayout(null);
        addCargos();

        JScrollPane scrollPane = new JScrollPane(cargosPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setPreferredSize(new Dimension(1000, 100));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(1000, 100));
        styleAddButton(addButton);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        JPanel buttonWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapperPanel.add(buttonPanel);
        mainPanel.add(buttonWrapperPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addCargos() {
        for (Cargo cargo : cargos) {
            cargosPanel.add(createCargoPanel(cargo));
        }
    }

    private JPanel createCargoPanel(Cargo cargo) {
        JPanel cargoPanel = new JPanel(new BorderLayout());
        cargoPanel.setPreferredSize(new Dimension(CARGO_MAX_WIDTH, CARGO_HEIGHT));
        cargoPanel.setBackground(cargo.isReturned() ? Color.YELLOW : (cargo.isDelivered() ? Color.GREEN : Color.RED));

        JLabel cargoLabel = new JLabel("Cargo ID: " + cargo.getId() + " | Delivered: " + cargo.isDelivered() + " | Returned: " + cargo.isReturned());
        cargoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        cargoPanel.add(cargoLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton deleteButton = new JButton("Delete");
        JButton returnButton = new JButton("Return");
        JButton deliveredButton = new JButton("Delivered");

        styleButton(detailsButton);
        styleButton(deleteButton, Color.RED);
        styleButton(returnButton);
        styleButton(deliveredButton);

        detailsButton.addActionListener(e -> showDetailsDialog(cargo));
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

        if (cargo.isDelivered()) {
            returnButton.setEnabled(true);
            deliveredButton.setEnabled(false);
        }
        if (cargo.isReturned()) {
            returnButton.setEnabled(false);
            deliveredButton.setEnabled(false);
        }

        buttonPanel.add(detailsButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(deliveredButton);

        cargoPanel.add(buttonPanel, BorderLayout.EAST);

        return cargoPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        if (cargosPanel != null) {
            int y = 0;
            int x = (cargosPanel.getWidth() - CARGO_MAX_WIDTH) / 2;

            for (Component component : cargosPanel.getComponents()) {
                component.setBounds(x, y * (CARGO_HEIGHT + 10) + CARGO_HEIGHT / 4, CARGO_MAX_WIDTH, CARGO_HEIGHT);
                y++;
            }
            cargosPanel.setPreferredSize(new Dimension(
                    CARGO_MAX_WIDTH,
                    (y * (CARGO_HEIGHT + 10)) + CARGO_HEIGHT
            ));
        }
    }

    private void showDetailsDialog(Cargo cargo) {
        CargoDetail cargoDetail = new CargoDetail((Frame) SwingUtilities.getWindowAncestor(this), cargo);
        cargoDetail.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Cargo", true);
        dialog.setSize(600, 300);
        dialog.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);

        JComboBox<Order> orderComboBox = new JComboBox<>(orders.toArray(new Order[0]));

        JCheckBox isDeliveredCheckBox = new JCheckBox();
        JCheckBox isReturnedCheckBox = new JCheckBox();

        JLabel orderLabel = new JLabel("Order:");
        orderLabel.setBounds(30, 30, 80, 25);
        orderComboBox.setBounds(120, 30, 400, 30);

        JLabel deliveredLabel = new JLabel("Is Delivered:");
        deliveredLabel.setBounds(30, 80, 80, 25);
        isDeliveredCheckBox.setBounds(120, 80, 30, 30);

        JLabel returnedLabel = new JLabel("Is Returned:");
        returnedLabel.setBounds(30, 130, 80, 25);
        isReturnedCheckBox.setBounds(120, 130, 30, 30);

        mainPanel.add(orderLabel);
        mainPanel.add(orderComboBox);
        mainPanel.add(deliveredLabel);
        mainPanel.add(isDeliveredCheckBox);
        mainPanel.add(returnedLabel);
        mainPanel.add(isReturnedCheckBox);

        JButton addButton = new JButton("Add Cargo");
        addButton.setBounds(240, 190, 120, 35);
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);

        addButton.addActionListener(e -> {
            Order order = (Order) orderComboBox.getSelectedItem();
            boolean isDelivered = isDeliveredCheckBox.isSelected();
            boolean isReturned = isReturnedCheckBox.isSelected();
            if (order != null) {
                Cargo cargo = new Cargo(isDelivered, order.getId());
                cargo.setReturned(isReturned);
                CargoService.add(cargo);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Please select an order",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(addButton);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.cargos = CargoService.getAllCargosForCurrentStore();
        this.orders = OrderService.getAllOrdersForCurrentStore();
        cargosPanel.removeAll();
        addCargos();
        revalidate();
        repaint();
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
    }

    private void styleAddButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
    }
}