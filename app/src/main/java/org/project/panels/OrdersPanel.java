package org.project.panels;

import org.project.models.Order;
import org.project.models.Product;
import org.project.models.Receiver;
import org.project.services.OrderService;
import org.project.services.ProductService;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrdersPanel extends JPanel {
    private OrderService orderService;
    private ReceiverService receiverService;
    private ProductService productService;
    private List<Order> orders;

    public OrdersPanel(OrderService orderService, ReceiverService receiverService, ProductService productService) {
        this.orderService = orderService;
        this.receiverService = receiverService;
        this.productService = productService;
        this.orders = orderService.getAllOrders();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadOrders();
    }

    private void loadOrders() {
        removeAll(); // Mevcut tüm bileşenleri kaldır
        for (Order order : orders) {
            addCard(order);
        }
        revalidate(); // Bileşenleri yeniden doğrula
        repaint(); // Paneli yeniden çiz
    }

    private void addCard(Order order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Order ID: " + order.getId());
        JTextArea contentArea = new JTextArea(order.toString());
        contentArea.setEditable(false);

        JButton detailsButton = new JButton("Details");
        detailsButton.addActionListener(e -> showDetailsDialog(order));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            orderService.delete(order.getId());
            refresh();
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> showUpdateDialog(order));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Order Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(order.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(receiverService.getAllReceivers().toArray(new Receiver[0]));
        receiverComboBox.setSelectedItem(order.getEntity1());
        JComboBox<Product> productComboBox = new JComboBox<>(productService.getAllProducts().toArray(new Product[0]));
        productComboBox.setSelectedItem(order.getEntity2());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            if (receiver != null && product != null) {
                order.setEntity1(receiver);
                order.setEntity2(product);
                orderService.update(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid Receiver or Product", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.orders = orderService.getAllOrders();
        loadOrders();
    }
}
/*
package org.project.components;

import org.project.models.Order;
import org.project.services.OrderService;
import org.project.services.ProductService;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrdersPanel extends JPanel {
    private OrderService orderService;
    private List<Order> orders;

    public OrdersPanel(OrderService orderService, ReceiverService receiverService, ProductService productService) {
        this.orderService = orderService;
        this.orders = orderService.getAllOrders();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadOrders();
    }

    private void loadOrders() {
        for (Order order : orders) {
            addCard(order);
        }
    }

    private void addCard(Order order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Order ID: " + order.getId());
        JTextArea contentArea = new JTextArea(order.toString());
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(order));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Order Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(order.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}*/