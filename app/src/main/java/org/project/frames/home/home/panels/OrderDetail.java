package org.project.frames.home.home.panels;

import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;

import javax.swing.*;
import java.awt.*;

public class OrderDetail extends JPanel {
    private JLabel orderIdLabel;
    private JLabel receiverNameLabel;
    private JLabel receiverEmailLabel;
    private JLabel receiverAddressLabel;

    private Order<Receiver, Product> order;

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

    public void setOrder(Order<Receiver, Product> order) {
        this.order = order;
        orderIdLabel.setText("Order ID: " + order.getId());
        receiverNameLabel.setText("Receiver: " + order.getEntity1().getName() + " " + order.getEntity1().getSurname());
        receiverEmailLabel.setText("Email: " + order.getEntity1().getEmail());
        receiverAddressLabel.setText("<html><body style='width: 300px'>Address: " + order.getEntity1().getAddress() + "</body></html>");
        revalidate();
        repaint();

        // Yeni sayfa açma işlemi
        showOrderDetail();
    }

    private void showOrderDetail() {
        JDialog dialog = new JDialog((Frame) null, "Order Details", true);
        dialog.setSize(1000, 800);
        dialog.setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2));

        // Product details panel
        ProductDetail2 productDetail2 = new ProductDetail2();
        productDetail2.setProduct(order.getEntity2(), order.getQuantity());
        contentPanel.add(productDetail2);

        // Receiver details panel
        JPanel receiverPanel = new JPanel(new GridBagLayout());
        receiverPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel receiverNameLabel = createLabel("Receiver: " + order.getEntity1().getName() + " " + order.getEntity1().getSurname());
        gbc.gridy = 0;
        receiverPanel.add(receiverNameLabel, gbc);

        JLabel receiverEmailLabel = createLabel("Email: " + order.getEntity1().getEmail());
        gbc.gridy++;
        receiverPanel.add(receiverEmailLabel, gbc);

        JLabel receiverAddressLabel = createLabel("<html><body style='width: 300px'>Address: " + order.getEntity1().getAddress() + "</body></html>");
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

import javax.swing.*;
import java.awt.*;

public class OrderDetail extends JPanel {
    private JLabel orderIdLabel;
    private JLabel receiverNameLabel;
    private JLabel receiverEmailLabel;
    private JLabel receiverAddressLabel;

    private Order<Receiver, Product> order;

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

    public void setOrder(Order<Receiver, Product> order) {
        this.order = order;
        orderIdLabel.setText("Order ID: " + order.getId());
        receiverNameLabel.setText("Receiver: " + order.getEntity1().getName() + " " + order.getEntity1().getSurname());
        receiverEmailLabel.setText("Email: " + order.getEntity1().getEmail());
        receiverAddressLabel.setText("<html><body style='width: 300px'>Address: " + order.getEntity1().getAddress() + "</body></html>");
        revalidate();
        repaint();

        // Yeni sayfa açma işlemi
        showOrderDetail();
    }

    private void showOrderDetail() {
        JDialog dialog = new JDialog((Frame) null, "Order Details", true);
        dialog.setSize(1000, 800);
        dialog.setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2));

        // Product details panel
        ProductDetail2 productDetail2 = new ProductDetail2();
        productDetail2.setProduct(order.getEntity2());
        contentPanel.add(productDetail2);

        // Receiver details panel
        JPanel receiverPanel = new JPanel(new GridBagLayout());
        receiverPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel receiverNameLabel = createLabel("Receiver: " + order.getEntity1().getName() + " " + order.getEntity1().getSurname());
        gbc.gridy = 0;
        receiverPanel.add(receiverNameLabel, gbc);

        JLabel receiverEmailLabel = createLabel("Email: " + order.getEntity1().getEmail());
        gbc.gridy++;
        receiverPanel.add(receiverEmailLabel, gbc);

        JLabel receiverAddressLabel = createLabel("<html><body style='width: 300px'>Address: " + order.getEntity1().getAddress() + "</body></html>");
        gbc.gridy++;
        receiverPanel.add(receiverAddressLabel, gbc);

        contentPanel.add(receiverPanel);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }
}
*/
/*
package org.project.frames.home.home.panels;

import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;

import javax.swing.*;
import java.awt.*;

public class OrderDetail extends JPanel {
    private JLabel orderIdLabel;
    private JLabel receiverNameLabel;
    private JLabel receiverEmailLabel;
    private JLabel receiverAddressLabel;

    private Order<Receiver, Product> order;

    public OrderDetail() {
        initialize();
    }

    public void initialize() {
        removeAll();
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        orderIdLabel = createLabel("");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(orderIdLabel, gbc);

        receiverNameLabel = createLabel("");
        gbc.gridy++;
        add(receiverNameLabel, gbc);

        receiverEmailLabel = createLabel("");
        gbc.gridy++;
        add(receiverEmailLabel, gbc);

        receiverAddressLabel = createLabel("");
        gbc.gridy++;
        add(receiverAddressLabel, gbc);

        revalidate();
        repaint();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    public void setOrder(Order<Receiver, Product> order) {
        this.order = order;
        orderIdLabel.setText("Order ID: " + order.getId());
        receiverNameLabel.setText("Receiver: " + order.getEntity1().getName() + " " + order.getEntity1().getSurname());
        receiverEmailLabel.setText("Email: " + order.getEntity1().getEmail());
        receiverAddressLabel.setText("Address: " + order.getEntity1().getAddress());
        revalidate();
        repaint();

        // Yeni sayfa açma işlemi
        showOrderDetail();
    }

    private void showOrderDetail() {
        JDialog dialog = new JDialog((Frame) null, "Order Details", true);
        dialog.setSize(1000, 800);
        dialog.setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2));

        // Product details panel
        ProductDetail2 productDetail2 = new ProductDetail2();
        productDetail2.setProduct(order.getEntity2());
        contentPanel.add(productDetail2);

        // Receiver details panel
        JPanel receiverPanel = new JPanel(new GridBagLayout());
        receiverPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel receiverNameLabel = createLabel("Receiver: " + order.getEntity1().getName() + " " + order.getEntity1().getSurname());
        gbc.gridy = 0;
        receiverPanel.add(receiverNameLabel, gbc);

        JLabel receiverEmailLabel = createLabel("Email: " + order.getEntity1().getEmail());
        gbc.gridy++;
        receiverPanel.add(receiverEmailLabel, gbc);

        JLabel receiverAddressLabel = createLabel("Address: " + order.getEntity1().getAddress());
        gbc.gridy++;
        receiverPanel.add(receiverAddressLabel, gbc);

        contentPanel.add(receiverPanel);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }
}*/
/*
package org.project.frames.home.home.panels;

import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;

import javax.swing.*;
import java.awt.*;

public class OrderDetail extends JPanel {
    private JLabel orderIdLabel;
    private JLabel receiverNameLabel;
    private JLabel receiverEmailLabel;
    private JLabel receiverAddressLabel;
    private JLabel productNameLabel;
    private JLabel productDescriptionLabel;
    private JLabel productPriceLabel;
    private JLabel productCategoryLabel;

    private Order<Receiver, Product> order;

    public OrderDetail() {
        initialize();
    }

    public void initialize() {
        removeAll();
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        orderIdLabel = createLabel("");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(orderIdLabel, gbc);

        receiverNameLabel = createLabel("");
        gbc.gridy++;
        add(receiverNameLabel, gbc);

        receiverEmailLabel = createLabel("");
        gbc.gridy++;
        add(receiverEmailLabel, gbc);

        receiverAddressLabel = createLabel("");
        gbc.gridy++;
        add(receiverAddressLabel, gbc);

        productNameLabel = createLabel("");
        gbc.gridy++;
        add(productNameLabel, gbc);

        productDescriptionLabel = createLabel("");
        gbc.gridy++;
        add(productDescriptionLabel, gbc);

        productPriceLabel = createLabel("");
        gbc.gridy++;
        add(productPriceLabel, gbc);

        productCategoryLabel = createLabel("");
        gbc.gridy++;
        add(productCategoryLabel, gbc);

        revalidate();
        repaint();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    public void setOrder(Order<Receiver, Product> order) {
        this.order = order;
        orderIdLabel.setText("Order ID: " + order.getId());
        receiverNameLabel.setText("Receiver: " + order.getEntity1().getName() + " " + order.getEntity1().getSurname());
        receiverEmailLabel.setText("Email: " + order.getEntity1().getEmail());
        receiverAddressLabel.setText("Address: " + order.getEntity1().getAddress());
        productNameLabel.setText("Product: " + order.getEntity2().getName());
        productDescriptionLabel.setText("Description: " + order.getEntity2().getDescription());
        productPriceLabel.setText("Price: $" + order.getEntity2().getPrice());
        revalidate();
        repaint();

        // Yeni sayfa açma işlemi
        showProductAndReceiverDetail();
    }

    private void showProductAndReceiverDetail() {
        JDialog dialog = new JDialog((Frame) null, "Product and Receiver Details", true);
        dialog.setSize(1250, 700);
        dialog.setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2));

        ///
        // ProductDetail panelini oluştur ve butonları gizle

        ///
        // ProductDetail panelini oluştur ve butonları gizle
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < order.getQuantity(); i++) {
            ProductDetail productDetail = new ProductDetail(cardLayout, cardPanel);
            productDetail.setProduct(order.getEntity2());
            hideButtons(productDetail);
            productPanel.add(productDetail);
        }
        contentPanel.add(new JScrollPane(productPanel));

        // ReceiverDetail panelini oluştur
        ReceiverDetail receiverDetail = new ReceiverDetail();
        receiverDetail.setReceiver(order.getEntity1());
        contentPanel.add(receiverDetail);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void hideButtons(ProductDetail productDetail) {
        for (Component component : productDetail.getComponents()) {
            if (component instanceof JButton) {
                component.setVisible(false);
            }
        }
    }
}*/
/*
package org.project.frames.home.home.panels;

import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;

import javax.swing.*;
import java.awt.*;

public class OrderDetail extends JPanel {
    private JLabel orderIdLabel;
    private JLabel receiverNameLabel;
    private JLabel receiverEmailLabel;
    private JLabel receiverAddressLabel;
    private JLabel productNameLabel;
    private JLabel productDescriptionLabel;
    private JLabel productPriceLabel;
    private JLabel productCategoryLabel;

    private Order<Receiver, Product> order;

    public OrderDetail() {
        initialize();
    }

    public void initialize() {
        removeAll();
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        orderIdLabel = createLabel("");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(orderIdLabel, gbc);

        receiverNameLabel = createLabel("");
        gbc.gridy++;
        add(receiverNameLabel, gbc);

        receiverEmailLabel = createLabel("");
        gbc.gridy++;
        add(receiverEmailLabel, gbc);

        receiverAddressLabel = createLabel("");
        gbc.gridy++;
        add(receiverAddressLabel, gbc);

        productNameLabel = createLabel("");
        gbc.gridy++;
        add(productNameLabel, gbc);

        productDescriptionLabel = createLabel("");
        gbc.gridy++;
        add(productDescriptionLabel, gbc);

        productPriceLabel = createLabel("");
        gbc.gridy++;
        add(productPriceLabel, gbc);

        productCategoryLabel = createLabel("");
        gbc.gridy++;
        add(productCategoryLabel, gbc);

        revalidate();
        repaint();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    public void setOrder(Order<Receiver, Product> order) {
        this.order = order;
        orderIdLabel.setText("Order ID: " + order.getId());
        receiverNameLabel.setText("Receiver: " + order.getEntity1().getName() + " " + order.getEntity1().getSurname());
        receiverEmailLabel.setText("Email: " + order.getEntity1().getEmail());
        receiverAddressLabel.setText("Address: " + order.getEntity1().getAddress());
        productNameLabel.setText("Product: " + order.getEntity2().getName());
        productDescriptionLabel.setText("Description: " + order.getEntity2().getDescription());
        productPriceLabel.setText("Price: $" + order.getEntity2().getPrice());
        revalidate();
        repaint();

        // Yeni sayfa açma işlemi
        showProductAndReceiverDetail();
    }

    private void showProductAndReceiverDetail() {
        JDialog dialog = new JDialog((Frame) null, "Product and Receiver Details", true);
        dialog.setSize(1250, 700);
        dialog.setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2));

        // ProductDetail panelini oluştur ve butonları gizle
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        ProductDetail productDetail = new ProductDetail(cardLayout, cardPanel);
        productDetail.setProduct(order.getEntity2());
        hideButtons(productDetail);
        contentPanel.add(productDetail);

        // ReceiverDetail panelini oluştur
        ReceiverDetail receiverDetail = new ReceiverDetail();
        receiverDetail.setReceiver(order.getEntity1());
        contentPanel.add(receiverDetail);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void hideButtons(ProductDetail productDetail) {
        for (Component component : productDetail.getComponents()) {
            if (component instanceof JButton) {
                component.setVisible(false);
            }
        }
    }
}*/