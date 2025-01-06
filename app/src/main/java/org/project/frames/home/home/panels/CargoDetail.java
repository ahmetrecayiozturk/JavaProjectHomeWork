package org.project.frames.home.home.panels;

import org.project.models.Cargo;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;

import javax.swing.*;
import java.awt.*;

public class CargoDetail extends JDialog {
    private Cargo cargo;

    public CargoDetail(Frame owner, Cargo cargo) {
        super(owner, "Cargo Details", true);
        this.cargo = cargo;
        initialize();
    }

    private void initialize() {
        setSize(400, 300);
        setLocationRelativeTo(null);

        Order order = cargo.getEntity();
        Receiver receiver = (Receiver) order.getEntity1();
        Product product = (Product) order.getEntity2();

        JTextArea contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setText(
                "Cargo ID: " + cargo.getId() + "\n" +
                        "Is Delivered: " + cargo.isDelivered() + "\n\n" +
                        "Order Details:\n" +
                        "Order ID: " + order.getId() + "\n" +
                        "Quantity: " + order.getQuantity() + "\n\n" +
                        "Receiver Details:\n" +
                        "Name: " + receiver.getName() + " " + receiver.getSurname() + "\n" +
                        "Address: " + receiver.getAddress() + "\n\n" +
                        "Product Details:\n" +
                        "Name: " + product.getName() + "\n" +
                        "Description: " + product.getDescription()
        );

        add(new JScrollPane(contentArea), BorderLayout.CENTER);
    }
}