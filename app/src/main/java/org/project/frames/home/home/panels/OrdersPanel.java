package org.project.frames.home.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.data.JsonRepository;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Type;
import java.util.List;

public class OrdersPanel extends JPanel {
    private int numRows;
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private final OrderService orderService = new OrderService();
    private List<Order<Receiver, Product>> orders;
    private OrderDetail orderDetail;

    public OrdersPanel() {
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumRows();
                revalidate();
                repaint();
            }
        });
        addOrders();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateNumRows() {
        int panelHeight = getHeight();
        numRows = Math.max(1, panelHeight / (ORDER_HEIGHT + 10));
    }

    private void addOrders() {
        for (Order<Receiver, Product> order : orders) {
            add(createOrderPanel(order));
        }
    }

    private JPanel createOrderPanel(Order<Receiver, Product> order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setBackground(new Color(255, 255, 255));
        orderPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.setBackground(new Color(0, 120, 215));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(e -> showDetailsDialog(order));

        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> showUpdateDialog(order));

        deleteButton.setBackground(new Color(255, 69, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> {
            orderService.delete(order.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int y = 0;
        int x = (getWidth() - ORDER_MAX_WIDTH) / 2;

        for (Component component : getComponents()) {
            component.setBounds(x, y * (ORDER_HEIGHT + 10) + ORDER_HEIGHT / 4, ORDER_MAX_WIDTH, ORDER_HEIGHT);
            y++;
        }
    }

    private void showDetailsDialog(Order<Receiver, Product> order) {
        orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField();

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());
            if (receiver != null && product != null && quantity > 0) {
                Order<Receiver, Product> order = new Order<>(receiver, product, quantity);
                orderService.add(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order<Receiver, Product> order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        receiverComboBox.setSelectedItem(order.getEntity1());
        productComboBox.setSelectedItem(order.getEntity2());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton updateButton = new JButton("Update");
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            int newQuantity = Integer.parseInt(quantityField.getText());
            if (receiver != null && product != null && newQuantity > 0) {
                int oldQuantity = order.getQuantity();
                order.setEntity1(receiver);
                order.setEntity2(product);
                order.setQuantity(newQuantity);
                orderService.update(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.orders = getAllOrders();
        removeAll();
        initialize();
    }

    private List<Order<Receiver, Product>> getAllOrders() {
        Type orderListType = new TypeToken<List<Order<Receiver, Product>>>() {}.getType();
        return new Gson().fromJson(orderService.getAllOrdersJson(), orderListType);
    }
}
/*
package org.project.frames.home.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.data.JsonRepository;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Type;
import java.util.List;

public class OrdersPanel extends JPanel {
    private int numRows;
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private final OrderService orderService = new OrderService();
    private List<Order<Receiver, Product>> orders;
    private OrderDetail orderDetail;

    public OrdersPanel() {
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumRows();
                revalidate();
                repaint();
            }
        });
        addOrders();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateNumRows() {
        int panelHeight = getHeight();
        numRows = Math.max(1, panelHeight / (ORDER_HEIGHT + 10));
    }

    private void addOrders() {
        for (Order<Receiver, Product> order : orders) {
            add(createOrderPanel(order));
        }
    }

    private JPanel createOrderPanel(Order<Receiver, Product> order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setBackground(new Color(255, 255, 255));
        orderPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.setBackground(new Color(0, 120, 215));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(e -> showDetailsDialog(order));

        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> showUpdateDialog(order));

        deleteButton.setBackground(new Color(255, 69, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> {
            orderService.delete(order.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int y = 0;
        int x = (getWidth() - ORDER_MAX_WIDTH) / 2;

        for (Component component : getComponents()) {
            component.setBounds(x, y * (ORDER_HEIGHT + 10) + ORDER_HEIGHT / 4, ORDER_MAX_WIDTH, ORDER_HEIGHT);
            y++;
        }
    }

    private void showDetailsDialog(Order<Receiver, Product> order) {
        orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField();

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());
            if (receiver != null && product != null && quantity > 0) {
                Order<Receiver, Product> order = new Order<>(receiver, product, quantity);
                orderService.add(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order<Receiver, Product> order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        receiverComboBox.setSelectedItem(order.getEntity1());
        productComboBox.setSelectedItem(order.getEntity2());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton updateButton = new JButton("Update");
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            int newQuantity = Integer.parseInt(quantityField.getText());
            if (receiver != null && product != null && newQuantity > 0) {
                int oldQuantity = order.getQuantity();
                order.setEntity1(receiver);
                order.setEntity2(product);
                order.setQuantity(newQuantity);

                JsonRepository<Product> productRepo = new JsonRepository<>(Product[].class);
                if (newQuantity > oldQuantity) {
                    int difference = newQuantity - oldQuantity;
                    product.setProductCount(product.getProductCount() - difference);
                } else {
                    int difference = oldQuantity - newQuantity;
                    product.setProductCount(product.getProductCount() + difference);
                }
                productRepo.update(product);

                // Debug adımları ekleyelim
                System.out.println("Product ID: " + product.getId());
                System.out.println("New Product Count: " + product.getProductCount());

                orderService.update(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.orders = getAllOrders();
        removeAll();
        initialize();
    }

    private List<Order<Receiver, Product>> getAllOrders() {
        Type orderListType = new TypeToken<List<Order<Receiver, Product>>>() {}.getType();
        return new Gson().fromJson(orderService.getAllOrdersJson(), orderListType);
    }
}
*/
/*
package org.project.frames.home.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.data.JsonRepository;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Type;
import java.util.List;

public class OrdersPanel extends JPanel {
    private int numRows;
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private final OrderService orderService = new OrderService();
    private List<Order<Receiver, Product>> orders;
    private OrderDetail orderDetail;

    public OrdersPanel() {
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumRows();
                revalidate();
                repaint();
            }
        });
        addOrders();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateNumRows() {
        int panelHeight = getHeight();
        numRows = Math.max(1, panelHeight / (ORDER_HEIGHT + 10));
    }

    private void addOrders() {
        for (Order<Receiver, Product> order : orders) {
            add(createOrderPanel(order));
        }
    }

    private JPanel createOrderPanel(Order<Receiver, Product> order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setBackground(new Color(255, 255, 255));
        orderPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.setBackground(new Color(0, 120, 215));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(e -> showDetailsDialog(order));

        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> showUpdateDialog(order));

        deleteButton.setBackground(new Color(255, 69, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> {
            orderService.delete(order.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int y = 0;
        int x = (getWidth() - ORDER_MAX_WIDTH) / 2;

        for (Component component : getComponents()) {
            component.setBounds(x, y * (ORDER_HEIGHT + 10) + ORDER_HEIGHT / 4, ORDER_MAX_WIDTH, ORDER_HEIGHT);
            y++;
        }
    }

    private void showDetailsDialog(Order<Receiver, Product> order) {
        orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField();

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());
            if (receiver != null && product != null && quantity > 0) {
                Order<Receiver, Product> order = new Order<>(receiver, product, quantity);
                orderService.add(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order<Receiver, Product> order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        receiverComboBox.setSelectedItem(order.getEntity1());
        productComboBox.setSelectedItem(order.getEntity2());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton updateButton = new JButton("Update");
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            int newQuantity = Integer.parseInt(quantityField.getText());
            if (receiver != null && product != null && newQuantity > 0) {
                int oldQuantity = order.getQuantity();
                order.setEntity1(receiver);
                order.setEntity2(product);
                order.setQuantity(newQuantity);

                JsonRepository<Product> productRepo=new JsonRepository<>( Product[].class);
                if (newQuantity > oldQuantity) {
                    int difference = newQuantity - oldQuantity;
                    product.setProductCount(product.getProductCount() - difference);
                } else {
                    int difference = oldQuantity - newQuantity;
                    product.setProductCount(product.getProductCount() + difference);
                }
                productRepo.update(product);

                orderService.update(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.orders = getAllOrders();
        removeAll();
        initialize();
    }

    private List<Order<Receiver, Product>> getAllOrders() {
        Type orderListType = new TypeToken<List<Order<Receiver, Product>>>() {}.getType();
        return new Gson().fromJson(orderService.getAllOrdersJson(), orderListType);
    }
}*/
/*
package org.project.frames.home.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Type;
import java.util.List;

public class OrdersPanel extends JPanel {
    private int numRows;
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private final OrderService orderService = new OrderService();
    private List<Order<Receiver, Product>> orders;
    private OrderDetail orderDetail;

    public OrdersPanel() {
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumRows();
                revalidate();
                repaint();
            }
        });
        addOrders();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateNumRows() {
        int panelHeight = getHeight();
        numRows = Math.max(1, panelHeight / (ORDER_HEIGHT + 10));
    }

    private void addOrders() {
        for (Order<Receiver, Product> order : orders) {
            add(createOrderPanel(order));
        }
    }

    private JPanel createOrderPanel(Order<Receiver, Product> order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setBackground(new Color(255, 255, 255));
        orderPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.setBackground(new Color(0, 120, 215));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(e -> showDetailsDialog(order));

        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> showUpdateDialog(order));

        deleteButton.setBackground(new Color(255, 69, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> {
            orderService.delete(order.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int y = 0;
        int x = (getWidth() - ORDER_MAX_WIDTH) / 2;

        for (Component component : getComponents()) {
            component.setBounds(x, y * (ORDER_HEIGHT + 10) + ORDER_HEIGHT / 4, ORDER_MAX_WIDTH, ORDER_HEIGHT);
            y++;
        }
    }

    private void showDetailsDialog(Order<Receiver, Product> order) {
        orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField();

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());
            if (receiver != null && product != null && quantity > 0) {
                Order<Receiver, Product> order = new Order<>(receiver, product, quantity);
                orderService.add(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order<Receiver, Product> order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        receiverComboBox.setSelectedItem(order.getEntity1());
        productComboBox.setSelectedItem(order.getEntity2());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton updateButton = new JButton("Update");
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());
            if (receiver != null && product != null && quantity > 0) {
                order.setEntity1(receiver);
                order.setEntity2(product);
                order.setQuantity(quantity);
                orderService.update(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.orders = getAllOrders();
        removeAll();
        initialize();
    }

    private List<Order<Receiver, Product>> getAllOrders() {
        Type orderListType = new TypeToken<List<Order<Receiver, Product>>>() {}.getType();
        return new Gson().fromJson(orderService.getAllOrdersJson(), orderListType);
    }
}*/
/*
package org.project.frames.home.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Type;
import java.util.List;

public class OrdersPanel extends JPanel {
    private int numRows;
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private final OrderService orderService = new OrderService();
    private List<Order<Receiver, Product>> orders;
    private OrderDetail orderDetail;

    public OrdersPanel() {
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumRows();
                revalidate();
                repaint();
            }
        });
        addOrders();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateNumRows() {
        int panelHeight = getHeight();
        numRows = Math.max(1, panelHeight / (ORDER_HEIGHT + 10));
    }

    private void addOrders() {
        for (Order<Receiver, Product> order : orders) {
            add(createOrderPanel(order));
        }
    }

    private JPanel createOrderPanel(Order<Receiver, Product> order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setBackground(new Color(255, 255, 255));
        orderPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.setBackground(new Color(0, 120, 215));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(e -> showDetailsDialog(order));

        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> showUpdateDialog(order));

        deleteButton.setBackground(new Color(255, 69, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> {
            orderService.delete(order.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int y = 0;
        int x = (getWidth() - ORDER_MAX_WIDTH) / 2;

        for (Component component : getComponents()) {
            component.setBounds(x, y * (ORDER_HEIGHT + 10) + ORDER_HEIGHT / 4, ORDER_MAX_WIDTH, ORDER_HEIGHT);
            y++;
        }
    }

    private void showDetailsDialog(Order<Receiver, Product> order) {
        orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField();

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());
            if (receiver != null && product != null && quantity > 0) {
                Order<Receiver, Product> order = new Order<>(receiver, product, quantity);
                orderService.add(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order<Receiver, Product> order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        receiverComboBox.setSelectedItem(order.getEntity1());
        productComboBox.setSelectedItem(order.getEntity2());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton updateButton = new JButton("Update");
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());
            if (receiver != null && product != null && quantity > 0) {
                order.setEntity1(receiver);
                order.setEntity2(product);
                order.setQuantity(quantity);
                orderService.update(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.orders = getAllOrders();
        removeAll();
        initialize();
    }

    private List<Order<Receiver, Product>> getAllOrders() {
        Type orderListType = new TypeToken<List<Order<Receiver, Product>>>() {}.getType();
        return new Gson().fromJson(orderService.getAllOrdersJson(), orderListType);
    }
}
*/
/*
package org.project.frames.home.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Type;
import java.util.List;

public class OrdersPanel extends JPanel {
    private int numRows;
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private final OrderService orderService = new OrderService();
    private List<Order<Receiver, Product>> orders;
    private OrderDetail orderDetail;

    public OrdersPanel() {
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumRows();
                revalidate();
                repaint();
            }
        });
        addOrders();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateNumRows() {
        int panelHeight = getHeight();
        numRows = Math.max(1, panelHeight / (ORDER_HEIGHT + 10));
    }

    private void addOrders() {
        for (Order<Receiver, Product> order : orders) {
            add(createOrderPanel(order));
        }
    }

    private JPanel createOrderPanel(Order<Receiver, Product> order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setBackground(new Color(255, 255, 255));
        orderPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.setBackground(new Color(0, 120, 215));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(e -> showDetailsDialog(order));

        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> showUpdateDialog(order));

        deleteButton.setBackground(new Color(255, 69, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> {
            orderService.delete(order.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int y = 0;
        int x = (getWidth() - ORDER_MAX_WIDTH) / 2;

        for (Component component : getComponents()) {
            component.setBounds(x, y * (ORDER_HEIGHT + 10) + ORDER_HEIGHT / 4, ORDER_MAX_WIDTH, ORDER_HEIGHT);
            y++;
        }
    }

    private void showDetailsDialog(Order<Receiver, Product> order) {
        orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            if (receiver != null && product != null) {
                Order<Receiver, Product> order = new Order<>(receiver, product);
                orderService.add(order);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order<Receiver, Product> order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(new ReceiverService().getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(new ProductService().getAllProducts().toArray(new Product[0]));

        receiverComboBox.setSelectedItem(order.getEntity1());
        productComboBox.setSelectedItem(order.getEntity2());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        JButton updateButton = new JButton("Update");
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
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
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.orders = getAllOrders();
        removeAll();
        initialize();
    }

    private List<Order<Receiver, Product>> getAllOrders() {
        Type orderListType = new TypeToken<List<Order<Receiver, Product>>>() {}.getType();
        return new Gson().fromJson(orderService.getAllOrdersJson(), orderListType);
    }
}
*/