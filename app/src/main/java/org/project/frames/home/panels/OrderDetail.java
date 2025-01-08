package org.project.frames.home.panels;

import org.project.models.Order;
import org.project.models.Receiver;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;

public class OrderDetail extends JPanel {
    // Labels to display order and receiver details
    private JLabel orderIdLabel;
    private JLabel receiverNameLabel;
    private JLabel receiverEmailLabel;
    private JLabel receiverAddressLabel;
    // Order object to hold the current order details
    private Order order;

    // Constructor to initialize the OrderDetail panel
    public OrderDetail() {
        initialize();
    }

    // Method to initialize the panel components
    public void initialize() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Panel to hold the order details
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize and add labels for order details
        orderIdLabel = createLabel("");
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(orderIdLabel, gbc);

        receiverNameLabel = createLabel("");
        gbc.gridy++;
        detailsPanel.add(receiverNameLabel, gbc);

        receiverEmailLabel = createLabel("");
        gbc.gridy++;
        detailsPanel.add(receiverEmailLabel, gbc);

        receiverAddressLabel = createLabel("");
        gbc.gridy++;
        detailsPanel.add(receiverAddressLabel, gbc);

        // Add the details panel to the main panel
        add(detailsPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    // Method to create a label with specific styling
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    // Method to set the order details
    public void setOrder(Order order) {
        this.order = order;
        Receiver receiver = ReceiverService.findReceiverById(order.getReceiverId());
        orderIdLabel.setText("Order ID: " + order.getId());
        receiverNameLabel.setText("Receiver: " + receiver.getName() + " " + receiver.getSurname());
        receiverEmailLabel.setText("Email: " + receiver.getEmail());
        receiverAddressLabel.setText("<html><body style='width: 300px'>Address: " + receiver.getAddress() + "</body></html>");
        revalidate();
        repaint();

        // Open a new dialog to show order details
        showOrderDetail();
    }

    // Method to show the order details in a new dialog
    private void showOrderDetail() {
        JDialog dialog = new JDialog((Frame) null, "Order Details", true);
        dialog.setSize(900, 600);
        dialog.setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2));

        // Product details panel
        ProductDetail2 productDetail2 = new ProductDetail2();
        productDetail2.setProduct(order.getProductId(), order.getQuantity());
        contentPanel.add(productDetail2);

        // Receiver details panel
        JPanel receiverPanel = new JPanel(new GridBagLayout());
        receiverPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Receiver receiver = ReceiverService.findReceiverById(order.getReceiverId());

        JLabel receiverNameLabel = createLabel("Receiver: " + receiver.getName() + " " + receiver.getSurname());
        gbc.gridy = 0;
        receiverPanel.add(receiverNameLabel, gbc);

        JLabel receiverEmailLabel = createLabel("Email: " + receiver.getEmail());
        gbc.gridy++;
        receiverPanel.add(receiverEmailLabel, gbc);

        JLabel receiverAddressLabel = createLabel("<html><body style='width: 300px'>Address: " + receiver.getAddress() + "</body></html>");
        gbc.gridy++;
        receiverPanel.add(receiverAddressLabel, gbc);

        contentPanel.add(receiverPanel);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }
}