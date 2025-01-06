package org.project.frames.home.home.panels;

import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;

public class OrderDetail extends JPanel {
    private JLabel orderIdLabel;
    private JLabel receiverNameLabel;
    private JLabel receiverEmailLabel;
    private JLabel receiverAddressLabel;

    private Order order;

    public OrderDetail() {
        initialize();
    }

    public void initialize() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

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

        add(detailsPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    public void setOrder(Order order) {
        this.order = order;
        Receiver receiver = ReceiverService.findReceiverById(order.getReceiverId());
        orderIdLabel.setText("Order ID: " + order.getId());
        if (receiver != null) {
            receiverNameLabel.setText("Receiver: " + receiver.getName() + " " + receiver.getSurname());
            receiverEmailLabel.setText("Email: " + receiver.getEmail());
            receiverAddressLabel.setText("<html><body style='width: 300px'>Address: " + receiver.getAddress() + "</body></html>");
        } else {
            receiverNameLabel.setText("Receiver: Not available");
            receiverEmailLabel.setText("Email: Not available");
            receiverAddressLabel.setText("Address: Not available");
        }
        revalidate();
        repaint();

        // Yeni sayfa açma işlemi
        showOrderDetail();
    }

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
/*
package org.project.frames.home.home.panels;

import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;

public class OrderDetail extends JPanel {
    private JLabel orderIdLabel;
    private JLabel receiverNameLabel;
    private JLabel receiverEmailLabel;
    private JLabel receiverAddressLabel;

    private Order order;

    public OrderDetail() {
        initialize();
    }

    public void initialize() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

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

        add(detailsPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    public void setOrder(Order order) {
        this.order = order;
        Receiver receiver= ReceiverService.findReceiverById(order.getReceiverId());
        orderIdLabel.setText("Order ID: " + order.getId());
        receiverNameLabel.setText("Receiver: " + receiver.getName()+ " " + receiver.getSurname());
        receiverEmailLabel.setText("Email: " + receiver.getEmail());
        receiverAddressLabel.setText("<html><body style='width: 300px'>Address: " + receiver.getAddress() + "</body></html>");
        revalidate();
        repaint();

        // Yeni sayfa açma işlemi
        showOrderDetail();
    }

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

        Receiver receiver=ReceiverService.findReceiverById(order.getReceiverId());

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
*/