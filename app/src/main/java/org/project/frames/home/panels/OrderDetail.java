package org.project.frames.home.panels;

import org.project.models.Order;
import org.project.models.Product;
import org.project.models.Receiver;
import org.project.services.ProductService;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class OrderDetail extends JDialog {
    private Order order;
    private Product product;
    private Receiver receiver;

    private JLabel orderIdLabel;
    private JLabel productIdLabel;
    private JLabel productNameLabel;
    private JLabel productDescriptionLabel;
    private JLabel productPriceLabel;
    private JLabel productCountLabel;
    private JLabel receiverIdLabel;
    private JLabel receiverFirstNameLabel;
    private JLabel receiverLastNameLabel;
    private JLabel receiverAddressLabel;
    private JLabel receiverEmailLabel;
    private JLabel quantityLabel;
    private JLabel statusLabel;
    private JLabel imageLabel;

    public OrderDetail(Frame owner, Order order) {
        super(owner, "Order Details", true); // modal dialog
        this.order = order;
        this.product = ProductService.getProductById(order.getProductId());
        this.receiver = ReceiverService.findReceiverById(order.getReceiverId());

        initializeComponents();
        setLayout();
        setImage(product.getImageUrl());
        setSize(800, 500);
        setLocationRelativeTo(owner);
    }

    private void initializeComponents() {
        orderIdLabel = new JLabel("Order ID: " + order.getId());
        productIdLabel = new JLabel("Product ID: " + product.getId());
        productNameLabel = new JLabel("Product: " + product.getName());
        productDescriptionLabel = new JLabel("Description: " + product.getDescription());
        productPriceLabel = new JLabel("Price: $" + product.getPrice());
        productCountLabel = new JLabel("Stock: " + product.getProductCount());
        quantityLabel = new JLabel("Quantity: " + order.getQuantity());
        statusLabel = new JLabel("Status: " + (order.getStatus() != null ? order.getStatus() : "Not specified"));
        imageLabel = new JLabel("Image not available", JLabel.CENTER);
        receiverIdLabel = new JLabel("Receiver ID: " + receiver.getId());
        receiverFirstNameLabel = new JLabel("First Name: " + receiver.getName());
        receiverLastNameLabel = new JLabel("Last Name: " + receiver.getSurname());
        receiverAddressLabel = new JLabel("Address: " + receiver.getAddress());
        receiverEmailLabel = new JLabel("Email: " + receiver.getEmail());
    }

    private void setLayout() {
        setLayout(null);
        imageLabel.setBounds(20, 20, 300, 200);

        productIdLabel.setBounds(20, 240, 300, 25);
        productPriceLabel.setBounds(20, 280, 300, 25);
        productCountLabel.setBounds(20, 320, 300, 25);
        productNameLabel.setBounds(20, 360, 300, 25);
        productDescriptionLabel.setBounds(30, 390, 300, 25);

        add(imageLabel);
        add(productIdLabel);
        add(productPriceLabel);
        add(productCountLabel);
        add(productNameLabel);
        add(productDescriptionLabel);

        orderIdLabel.setBounds(400, 20, 350, 25);
        quantityLabel.setBounds(400, 60, 350, 25);
        statusLabel.setBounds(400, 100, 350, 25);
        add(orderIdLabel);
        add(quantityLabel);
        add(statusLabel);

        receiverIdLabel.setBounds(400, 160, 350, 25);
        receiverFirstNameLabel.setBounds(400, 200, 350, 25);
        receiverLastNameLabel.setBounds(400, 240, 350, 25);
        receiverAddressLabel.setBounds(400, 280, 350, 25);
        receiverEmailLabel.setBounds(400, 320, 350, 25);

        add(receiverIdLabel);
        add(receiverFirstNameLabel);
        add(receiverLastNameLabel);
        add(receiverAddressLabel);
        add(receiverEmailLabel);
    }

    private void setImage(String imageUrl) {
        if (imageLabel.getWidth() > 0 && imageLabel.getHeight() > 0) {
            File imageFile = new File(imageUrl);
            if (imageFile.exists()) {
                Image image = new ImageIcon(imageFile.getAbsolutePath()).getImage();
                image = image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(image);
                imageLabel.setIcon(icon);
                imageLabel.setText("");
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            } else {
                imageLabel.setText("Image not available");
            }
        } else {
            imageLabel.setText("Image not available");
        }
    }
}
