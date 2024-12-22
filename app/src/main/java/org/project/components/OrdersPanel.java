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