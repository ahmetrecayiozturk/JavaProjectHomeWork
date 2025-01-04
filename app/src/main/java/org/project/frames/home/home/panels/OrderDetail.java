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
    private JLabel productNameLabel;
    private JLabel productDescriptionLabel;
    private JLabel productPriceLabel;
    private JLabel productCategoryLabel;

    private Order<Receiver, Product> order;

    private final int innerPanelWidth = 500;

    public OrderDetail() {
        initialize();
    }

    public void initialize() {
        removeAll();
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBounds(0, 0, innerPanelWidth, 400);

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
    private JLabel productNameLabel;
    private JLabel productDescriptionLabel;
    private JLabel productPriceLabel;
    private JLabel productCategoryLabel;

    private Order<Receiver, Product> order;

    private final int innerPanelWidth = 500;

    public OrderDetail() {
        initialize();
    }

    public void initialize() {
        removeAll();
        setLayout(null);
        setBounds(0, 0, innerPanelWidth, 400);

        orderIdLabel = new JLabel();
        orderIdLabel.setBounds(50, 20, 400, 30);
        add(orderIdLabel);

        receiverNameLabel = new JLabel();
        receiverNameLabel.setBounds(50, 60, 400, 30);
        add(receiverNameLabel);

        receiverEmailLabel = new JLabel();
        receiverEmailLabel.setBounds(50, 100, 400, 30);
        add(receiverEmailLabel);

        receiverAddressLabel = new JLabel();
        receiverAddressLabel.setBounds(50, 140, 400, 30);
        add(receiverAddressLabel);

        productNameLabel = new JLabel();
        productNameLabel.setBounds(50, 180, 400, 30);
        add(productNameLabel);

        productDescriptionLabel = new JLabel();
        productDescriptionLabel.setBounds(50, 220, 400, 30);
        add(productDescriptionLabel);

        productPriceLabel = new JLabel();
        productPriceLabel.setBounds(50, 260, 400, 30);
        add(productPriceLabel);

        productCategoryLabel = new JLabel();
        productCategoryLabel.setBounds(50, 300, 400, 30);
        add(productCategoryLabel);

        revalidate();
        repaint();
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
    }
}*/
/*
package org.project.frames.home.home.panels;

import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.OrderService;

import javax.swing.*;
import java.awt.*;

public class OrderDetail extends JPanel {
    private JLabel orderIdLabel;
    private JLabel receiverLabel;
    private JLabel productLabel;
    private OrderService orderService = new OrderService();

    private Order<Receiver, Product> order;

    private final int innerPanelWidth = 500;

    public OrderDetail() {
        order = new Order<>(new Receiver("example@example.com", "password", "John", "Doe", "123 Main St"), new Product("Product", "Description", "Category", "ImageUrl", 0.0, 0));
        initialize();
    }

    public void initialize() {
        removeAll();
        setLayout(null);
        setBounds(0, 0, innerPanelWidth, 300);

        orderIdLabel = new JLabel();
        orderIdLabel.setBounds(50, 40, 400, 30);
        add(orderIdLabel);

        receiverLabel = new JLabel();
        receiverLabel.setBounds(50, 80, 400, 30);
        add(receiverLabel);

        productLabel = new JLabel();
        productLabel.setBounds(50, 120, 400, 30);
        add(productLabel);

        revalidate();
        repaint();
    }

    public void setOrder(Order<Receiver, Product> order) {
        this.order = order;
        orderIdLabel.setText("Order ID: " + order.getId());
        receiverLabel.setText("Receiver: " + order.getEntity1().getName() + " " + order.getEntity1().getSurname());
        productLabel.setText("Product: " + order.getEntity2().getName());
        revalidate();
        repaint();
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
    private JLabel receiverLabel;
    private JLabel productLabel;
    private Order<Receiver, Product> order;

    private final int innerPanelWidth = 500;

    public OrderDetail() {
        order = new Order<>(new Receiver("example@example.com", "password", "John", "Doe", "123 Main St"), new Product("Product", "Description", "Category", "ImageUrl", 0.0, 0));
        initialize();
    }

    public void initialize() {
        removeAll();
        setLayout(null);
        setBounds(0, 0, innerPanelWidth, 300);

        orderIdLabel = new JLabel();
        orderIdLabel.setBounds(50, 40, 400, 30);
        add(orderIdLabel);

        receiverLabel = new JLabel();
        receiverLabel.setBounds(50, 80, 400, 30);
        add(receiverLabel);

        productLabel = new JLabel();
        productLabel.setBounds(50, 120, 400, 30);
        add(productLabel);

        revalidate();
        repaint();
    }

    public void setOrder(Order<Receiver, Product> order) {
        this.order = order;
        orderIdLabel.setText("Order ID: " + order.getId());
        receiverLabel.setText("Receiver: " + order.getEntity1().getName() + " " + order.getEntity1().getSurname());
        productLabel.setText("Product: " + order.getEntity2().getName());
        revalidate();
        repaint();
    }
}*/