package org.project.frames.home.panels;

import org.project.models.Cargo;
import org.project.models.Order;
import org.project.services.CargoService;
import org.project.services.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Cargos extends JPanel {
    private static final int CARGO_MAX_WIDTH = 1000;
    private static final int CARGO_HEIGHT = 50;
    private List<Cargo> cargos;
    private List<Order> orders;
    private JPanel cargoListPanel;
    private JScrollPane scrollPane;

    public Cargos() {
        this.cargos = CargoService.getAllCargos();
        this.orders = OrderService.getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        cargoListPanel = new JPanel();
        cargoListPanel.setLayout(new BoxLayout(cargoListPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(cargoListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(CARGO_MAX_WIDTH, 600));
        add(scrollPane, BorderLayout.CENTER);

        addCargos();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        JButton addButton = new JButton("Add");
        styleAddButton(addButton);
        addButton.setPreferredSize(new Dimension(500, 125)); // Boyutu 2.5 katına çıkar
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addCargos() {
        cargoListPanel.removeAll();
        for (Cargo cargo : cargos) {
            cargoListPanel.add(createCargoPanel(cargo));
        }
        cargoListPanel.revalidate();
        cargoListPanel.repaint();
    }

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

    private void showDetailsDialog(Cargo cargo) {
        CargoDetail cargoDetail = new CargoDetail((Frame) SwingUtilities.getWindowAncestor(this), cargo);
        cargoDetail.setVisible(true);
    }

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

    public void refresh() {
        this.cargos = CargoService.getAllCargos();
        addCargos();
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleAddButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
    }
}
/*package org.project.frames.home.panels;

import org.project.models.Cargo;
import org.project.models.Order;
import org.project.services.CargoService;
import org.project.services.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Cargos extends JPanel {
    private static final int CARGO_MAX_WIDTH = 1000;
    private static final int CARGO_HEIGHT = 50;
    private List<Cargo> cargos;
    private List<Order> orders;
    private JPanel cargoListPanel;
    private JScrollPane scrollPane;

    public Cargos() {
        this.cargos = CargoService.getAllCargos();
        this.orders = OrderService.getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        cargoListPanel = new JPanel();
        cargoListPanel.setLayout(new BoxLayout(cargoListPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(cargoListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(CARGO_MAX_WIDTH, 600));
        add(scrollPane, BorderLayout.CENTER);

        addCargos();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // FlowLayout kullanımı

        JButton addButton = new JButton("Add");
        styleButton(addButton);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addCargos() {
        cargoListPanel.removeAll();
        for (Cargo cargo : cargos) {
            cargoListPanel.add(createCargoPanel(cargo));
        }
        cargoListPanel.revalidate();
        cargoListPanel.repaint();
    }

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

    private void showDetailsDialog(Cargo cargo) {
        CargoDetail cargoDetail = new CargoDetail((Frame) SwingUtilities.getWindowAncestor(this), cargo);
        cargoDetail.setVisible(true);
    }

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
        styleButton(addButton);
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

    public void refresh() {
        this.cargos = CargoService.getAllCargos();
        addCargos();
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}*/
/*package org.project.frames.home.panels;

import org.project.frames.home.panels.CargoDetail;
import org.project.models.Cargo;
import org.project.models.Order;
import org.project.services.CargoService;
import org.project.services.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Cargos extends JPanel {
    private static final int CARGO_MAX_WIDTH = 1000;
    private static final int CARGO_HEIGHT = 50;
    private List<Cargo> cargos;
    private List<Order> orders;
    private JPanel cargoListPanel;
    private JScrollPane scrollPane;

    public Cargos() {
        this.cargos = CargoService.getAllCargos();
        this.orders = OrderService.getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        cargoListPanel = new JPanel();
        cargoListPanel.setLayout(new BoxLayout(cargoListPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(cargoListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(CARGO_MAX_WIDTH, 600));
        add(scrollPane, BorderLayout.CENTER);

        addCargos();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addCargos() {
        cargoListPanel.removeAll();
        for (Cargo cargo : cargos) {
            cargoListPanel.add(createCargoPanel(cargo));
        }
        cargoListPanel.revalidate();
        cargoListPanel.repaint();
    }

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

    private void showDetailsDialog(Cargo cargo) {
        CargoDetail cargoDetail = new CargoDetail((Frame) SwingUtilities.getWindowAncestor(this), cargo);
        cargoDetail.setVisible(true);
    }

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

    public void refresh() {
        this.cargos = CargoService.getAllCargos();
        addCargos();
    }
}
*/
/*
package org.project.frames.home.panels;

import org.project.models.Cargo;
import org.project.models.Order;
import org.project.services.CargoService;
import org.project.services.OrderService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class Cargos extends JPanel {
    private int numRows;
    private static final int CARGO_MAX_WIDTH = 1000;
    private static final int CARGO_HEIGHT = 50;
    private List<Cargo> cargos;
    private List<Order> orders;

    public Cargos() {
        this.cargos = CargoService.getAllCargosForCurrentStore();
        this.orders = OrderService.getAllOrdersForCurrentStore();
        initialize();
    }

    private void initialize() {
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumRows();
                revalidate();
                repaint();
            }
        });
        addCargos();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateNumRows() {
        int panelHeight = getHeight();
        numRows = Math.max(1, panelHeight / (CARGO_HEIGHT + 10));
    }

    private void addCargos() {
        for (Cargo cargo : cargos) {
            add(createCargoPanel(cargo));
        }
    }

    private JPanel createCargoPanel(Cargo cargo) {
        JPanel cargoPanel = new JPanel(new BorderLayout());
        cargoPanel.setPreferredSize(new Dimension(CARGO_MAX_WIDTH, CARGO_HEIGHT));
        cargoPanel.setBackground(cargo.isDelivered() ? Color.GREEN : Color.RED);

        JLabel cargoLabel = new JLabel("Cargo ID: " + cargo.getId());
        cargoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        cargoPanel.add(cargoLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.addActionListener(e -> showDetailsDialog(cargo));
        updateButton.addActionListener(e -> showUpdateDialog(cargo));
        deleteButton.addActionListener(e -> {
            CargoService.delete(cargo.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        cargoPanel.add(buttonPanel, BorderLayout.EAST);

        return cargoPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int y = 0;
        int x = (getWidth() - CARGO_MAX_WIDTH) / 2;

        for (Component component : getComponents()) {
            component.setBounds(x, y * (CARGO_HEIGHT + 10) + CARGO_HEIGHT / 4, CARGO_MAX_WIDTH, CARGO_HEIGHT);
            y++;
        }
    }

    private void showDetailsDialog(Cargo cargo) {
        CargoDetail cargoDetail = new CargoDetail((Frame) SwingUtilities.getWindowAncestor(this), cargo);
        cargoDetail.setVisible(true);
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
                Cargo cargo = new Cargo(isDelivered, order.getId());
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

    private void showUpdateDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Update Cargo", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Order> orderComboBox = new JComboBox<>(orders.toArray(new Order[0]));
        JCheckBox isDeliveredCheckBox = new JCheckBox();

        orderComboBox.setSelectedItem(cargo.getOrderId());
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
                cargo.setOrderId(order.getId());
                cargo.setDelivered(isDelivered);
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

    public void refresh() {
        this.cargos = CargoService.getAllCargosForCurrentStore();
        this.orders = OrderService.getAllOrdersForCurrentStore();
        removeAll();
        initialize();
        revalidate();
        repaint();
    }
}
*/
