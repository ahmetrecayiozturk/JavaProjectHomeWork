package org.project.panels;

import org.project.models.Order;
import org.project.models.Product;
import org.project.models.Receiver;
import org.project.services.CargoService;
import org.project.services.OrderService;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ReceiversPanel extends JPanel {
    private ReceiverService receiverService;
    private OrderService orderService;
    private CargoService cargoService;
    private List<Receiver> receivers;

    public ReceiversPanel(ReceiverService receiverService, OrderService orderService, CargoService cargoService) {
        this.receiverService = receiverService;
        this.orderService = orderService;
        this.cargoService = cargoService;
        this.receivers = receiverService.getAllReceivers();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadReceivers();
    }

    private void loadReceivers() {
        removeAll(); // Mevcut tüm bileşenleri kaldır
        for (Receiver receiver : receivers) {
            addCard(receiver);
        }
        revalidate(); // Bileşenleri yeniden doğrula
        repaint(); // Paneli yeniden çiz
    }

    private void addCard(Receiver receiver) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Receiver ID: " + receiver.getId());
        JTextArea contentArea = new JTextArea(receiver.toString());
        contentArea.setEditable(false);

        JButton detailsButton = new JButton("Details");
        detailsButton.addActionListener(e -> showDetailsDialog(receiver));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            receiverService.delete(receiver.getId());
            refresh();
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> showUpdateDialog(receiver));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Receiver Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(receiver.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        JPanel ordersPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        List<Order<Receiver, Product>> receiverOrders = getOrdersByReceiver(receiver);
        for (Order<Receiver, Product> order : receiverOrders) {
            addOrderCard(ordersPanel, order);
        }
        dialog.add(new JScrollPane(ordersPanel), BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void addOrderCard(JPanel panel, Order<Receiver, Product> order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Order ID: " + order.getId());
        JTextArea contentArea = new JTextArea(order.toString());
        contentArea.setEditable(false);
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        panel.add(card);
    }

    private List<Order<Receiver, Product>> getOrdersByReceiver(Receiver receiver) {
        List<Order<Receiver, Product>> receiverOrders = new ArrayList<>();
        for (Order<Receiver, Product> order : orderService.getAllOrders()) {
            if (order.getEntity1().equals(receiver)) {
                receiverOrders.add(order);
            }
        }
        return receiverOrders;
    }

    private void showUpdateDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Update Receiver", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField emailField = new JTextField(receiver.getEmail());
        JTextField passwordField = new JTextField(receiver.getPassword());
        JTextField receiverNameField = new JTextField(receiver.getName());
        JTextField surnameField = new JTextField(receiver.getSurname());
        JTextField addressField = new JTextField(receiver.getAddress());

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(receiverNameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = receiverNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
                receiver.setEmail(email);
                receiver.setPassword(password);
                receiver.setName(name);
                receiver.setSurname(surname);
                receiver.setAddress(address);
                receiverService.update(receiver);
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
        this.receivers = receiverService.getAllReceivers();
        loadReceivers();
    }
}
/*package org.project.components;

import org.project.models.Receiver;
import org.project.services.CargoService;
import org.project.services.OrderService;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReceiversPanel extends JPanel {
    private ReceiverService receiverService;
    private List<Receiver> receivers;

    public ReceiversPanel(ReceiverService receiverService, OrderService orderService, CargoService cargoService) {
        this.receiverService = receiverService;
        this.receivers = receiverService.getAllReceivers();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadReceivers();
    }

    private void loadReceivers() {
        for (Receiver receiver : receivers) {
            addCard(receiver);
        }
    }

    private void addCard(Receiver receiver) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Receiver ID: " + receiver.getId());
        JTextArea contentArea = new JTextArea(receiver.toString());
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(receiver));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Receiver Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(receiver.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}*/