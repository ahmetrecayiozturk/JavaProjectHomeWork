package org.project.components;

import org.project.models.Cargo;
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