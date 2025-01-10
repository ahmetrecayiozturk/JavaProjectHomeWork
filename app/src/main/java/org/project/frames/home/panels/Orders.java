package org.project.frames.home.panels;

import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.models.Cargo;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Orders extends JPanel {
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private List<Order> orders;
    private OrderDetail orderDetail;
    private JPanel ordersPanel;
    private JPanel mainPanel;

    public Orders() {
        this.orders = OrderService.getAllOrdersForCurrentStore();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout());

        ordersPanel = new JPanel();
        ordersPanel.setLayout(null);
        addOrders();

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setPreferredSize(new Dimension(1000, 100));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(1000, 100));
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        JPanel buttonWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapperPanel.add(buttonPanel);
        mainPanel.add(buttonWrapperPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addOrders() {
        for (Order order : orders) {
            ordersPanel.add(createOrderPanel(order));
        }
    }

    private JPanel createOrderPanel(Order order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        boolean isSentToCargo = CargoService.getAllCargos().stream().anyMatch(cargo -> cargo.getOrderId().equals(order.getId()));
        orderPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        orderPanel.setBackground(isSentToCargo ? Color.GREEN : Color.RED);

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton cargoButton = new JButton("Send to Cargo");

        styleButton(detailsButton);
        styleButton(updateButton);
        styleButton(deleteButton, new Color(255, 69, 0));
        styleButton(cargoButton);

        detailsButton.addActionListener(e -> showDetailsDialog(order));
        updateButton.addActionListener(e -> showUpdateDialog(order));
        deleteButton.addActionListener(e -> {
            OrderService.delete(order.getId());
            refresh();
        });
        cargoButton.addActionListener(e -> {
            Cargo cargo = new Cargo(false, order.getId());
            Boolean isAdded = CargoService.add(cargo);
            if (isAdded) {
                JOptionPane.showMessageDialog(this, "Order has been sent to cargo.");
            } else {
                JOptionPane.showMessageDialog(this, "Order already been sent to cargo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            refresh();
        });

        if (isSentToCargo) {
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            cargoButton.setEnabled(false);
        }

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cargoButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        if (ordersPanel != null) {
            int y = 0;
            int x = (ordersPanel.getWidth() - ORDER_MAX_WIDTH) / 2;

            for (Component component : ordersPanel.getComponents()) {
                component.setBounds(x, y * (ORDER_HEIGHT + 10) + ORDER_HEIGHT / 4, ORDER_MAX_WIDTH, ORDER_HEIGHT);
                y++;
            }
            ordersPanel.setPreferredSize(new Dimension(
                    ORDER_MAX_WIDTH,
                    (y * (ORDER_HEIGHT + 10)) + ORDER_HEIGHT
            ));
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(600, 300);
        dialog.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        List<Receiver> receivers = ReceiverService.getAllReceivers();
        List<Product> products = ProductService.getAllProducts().stream()
                .filter(product -> product.getProductCount() > 0)
                .collect(Collectors.toList());

        DefaultListModel<String> receiverListModel = new DefaultListModel<>();
        for (Receiver receiver : receivers) {
            receiverListModel.addElement("Receiver Id " + receiver.getId() + " - Receiver Name " + receiver.getName() + " " + receiver.getSurname());
        }
        JList<String> receiverList = new JList<>(receiverListModel);
        receiverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane receiverScrollPane = new JScrollPane(receiverList);

        DefaultListModel<String> productListModel = new DefaultListModel<>();
        for (Product product : products) {
            productListModel.addElement("Product Id: " + product.getId() + " - Product Name " + product.getName());
        }
        JList<String> productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane productScrollPane = new JScrollPane(productList);

        JTextField quantityField = new JTextField();

        JLabel receiverLabel = new JLabel("Receiver:");
        receiverLabel.setBounds(30, 30, 80, 25);
        receiverScrollPane.setBounds(120, 30, 400, 30);

        JLabel productLabel = new JLabel("Product:");
        productLabel.setBounds(30, 80, 80, 25);
        productScrollPane.setBounds(120, 80, 400, 30);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(30, 130, 80, 25);
        quantityField.setBounds(120, 130, 400, 30);

        mainPanel.add(receiverLabel);
        mainPanel.add(receiverScrollPane);
        mainPanel.add(productLabel);
        mainPanel.add(productScrollPane);
        mainPanel.add(quantityLabel);
        mainPanel.add(quantityField);

        JButton addButton = new JButton("Add Order");
        addButton.setBounds(240, 190, 120, 35);
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);

        addButton.addActionListener(e -> {
            try {
                String selectedReceiver = receiverList.getSelectedValue();
                String selectedProduct = productList.getSelectedValue();
                String quantityText = quantityField.getText();
                if (selectedReceiver != null && selectedProduct != null && !quantityText.isEmpty()) {
                    int quantity = Integer.parseInt(quantityText);
                    if (quantity > 0) {
                        int receiverId = Integer.parseInt(selectedReceiver.split(" - ")[0].replace("Receiver Id ", ""));
                        int productId = Integer.parseInt(selectedProduct.split(" - ")[0].replace("Product Id: ", ""));
                        Order order = new Order(productId, receiverId, quantity);
                        OrderService.add(order);
                        refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for quantity", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(addButton);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void showDetailsDialog(Order order) {
        OrderDetail orderDetail = new OrderDetail((Frame) SwingUtilities.getWindowAncestor(this), order);
        orderDetail.setVisible(true);
    }

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(null);
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));
        quantityField.setBounds(150, 50, 100, 30);
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(50, 50, 75, 30);
        formPanel.add(quantityLabel);
        formPanel.add(quantityField);

        JButton updateButton = new JButton("Update");
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBounds(100, 100, 100, 30);
        updateButton.addActionListener(e -> {
            int newQuantity = Integer.parseInt(quantityField.getText());
            if (newQuantity > 0) {
                order.setQuantity(newQuantity);
                OrderService.update(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.orders = OrderService.getAllOrdersForCurrentStore();
        ordersPanel.removeAll();
        addOrders();
        revalidate();
        repaint();
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