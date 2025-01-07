package org.project.frames.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.models.Cargo;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class Orders extends JPanel {
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private List<Order> orders;
    private OrderDetail orderDetail;
    private Cargos cargosPanel;
    private JPanel orderListPanel;
    private JScrollPane scrollPane;

    public Orders(Cargos cargosPanel) {
        this.cargosPanel = cargosPanel;
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, 600));
        add(scrollPane, BorderLayout.CENTER);

        addOrders();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        JButton addButton = new JButton("Add");
        styleAddButton(addButton);
        addButton.setPreferredSize(new Dimension(500, 125)); // Boyutu 2.5 katına çıkar
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addOrders() {
        orderListPanel.removeAll();
        for (Order order : orders) {
            orderListPanel.add(createOrderPanel(order));
        }
        orderListPanel.revalidate();
        orderListPanel.repaint();
    }

    private JPanel createOrderPanel(Order order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setMaximumSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));

        boolean isSentToCargo = CargoService.getAllCargos().stream()
                .anyMatch(cargo -> cargo.getOrderId().equals(order.getId()));

        orderPanel.setBackground(isSentToCargo ? Color.GREEN : Color.RED);

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton cargoButton = new JButton("Cargo'ya Ver");

        styleButton(detailsButton);
        styleButton(updateButton);
        styleButton(deleteButton, new Color(255, 69, 0));
        styleButton(cargoButton);

        detailsButton.addActionListener(e -> showDetailsDialog(order));
        updateButton.addActionListener(e -> showUpdateDialog(order));
        deleteButton.addActionListener(e -> {
            OrderService.delete(order.getId());
            refresh();
        });
        cargoButton.addActionListener(e -> {
            CargoService.add(new Cargo(false, false, order.getId()));
            JOptionPane.showMessageDialog(this, "Order has been sent to cargo.");
            cargosPanel.refresh(); // Cargos panelini güncelle
            refresh();
        });

        if (isSentToCargo) {
            detailsButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            cargoButton.setEnabled(false);
        }

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cargoButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    private void showDetailsDialog(Order order) {
        orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        List<Receiver> receivers = new ReceiverService().getAllReceivers();
        List<Product> products = new ProductService().getAllProducts().stream()
                .filter(product -> product.getProductCount() > 0)
                .collect(Collectors.toList());

        DefaultListModel<String> receiverListModel = new DefaultListModel<>();
        for (Receiver receiver : receivers) {
            receiverListModel.addElement("Receiver Id " + receiver.getId() + " - Receiver Name " + receiver.getName() + " " + receiver.getSurname());
        }
        JList<String> receiverList = new JList<>(receiverListModel);
        receiverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane receiverScrollPane = new JScrollPane(receiverList);

        DefaultListModel<String> productListModel = new DefaultListModel<>();
        for (Product product : products) {
            productListModel.addElement("Product Id: " + product.getId() + " - Product Name " + product.getName());
        }
        JList<String> productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane productScrollPane = new JScrollPane(productList);

        JTextField quantityField = new JTextField();

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverScrollPane);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productScrollPane);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton addButton = new JButton("Add");
        styleAddButton(addButton);
        addButton.addActionListener(e -> {
            String selectedReceiver = receiverList.getSelectedValue();
            String selectedProduct = productList.getSelectedValue();
            String quantityText = quantityField.getText();
            if (selectedReceiver != null && selectedProduct != null && !quantityText.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityText);
                    if (quantity > 0) {
                        int receiverId = Integer.parseInt(selectedReceiver.split(" - ")[0].replace("Receiver Id ", ""));
                        int productId = Integer.parseInt(selectedProduct.split(" - ")[0].replace("Product Id: ", ""));
                        Order order = new Order(productId, receiverId, quantity);
                        OrderService.add(order);

                        // Update product count
                        Product product = products.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);
                        if (product != null) {
                            product.setProductCount(product.getProductCount() - quantity);
                            ProductService.update(product);
                        }

                        refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        List<Receiver> receivers = new ReceiverService().getAllReceivers();
        List<Product> products = new ProductService().getAllProducts().stream()
                .filter(product -> product.getProductCount() > 0 || product.getId() == order.getProductId())
                .collect(Collectors.toList());

        DefaultListModel<String> receiverListModel = new DefaultListModel<>();
        for (Receiver receiver : receivers) {
            receiverListModel.addElement("Receiver Id " + receiver.getId() + " - Receiver Name " + receiver.getName() + " " + receiver.getSurname());
        }
        JList<String> receiverList = new JList<>(receiverListModel);
        receiverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        receiverList.setSelectedValue("Receiver Id " + order.getReceiverId() + " - Receiver Name " + receivers.stream().filter(r -> r.getId() == order.getReceiverId()).findFirst().get().getName() + " " + receivers.stream().filter(r -> r.getId() == order.getReceiverId()).findFirst().get().getSurname(), true);
        JScrollPane receiverScrollPane = new JScrollPane(receiverList);

        DefaultListModel<String> productListModel = new DefaultListModel<>();
        for (Product product : products) {
            productListModel.addElement("Product Id: " + product.getId() + " - Product Name " + product.getName());
        }
        JList<String> productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setSelectedValue("Product Id: " + order.getProductId() + " - Product Name " + products.stream().filter(p -> p.getId() == order.getProductId()).findFirst().get().getName(), true);
        JScrollPane productScrollPane = new JScrollPane(productList);

        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverScrollPane);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productScrollPane);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton updateButton = new JButton("Update");
        styleButton(updateButton);
        updateButton.addActionListener(e -> {
            String selectedReceiver = receiverList.getSelectedValue();
            String selectedProduct = productList.getSelectedValue();
            String quantityText = quantityField.getText();
            if (selectedReceiver != null && selectedProduct != null && !quantityText.isEmpty()) {
                try {
                    int newQuantity = Integer.parseInt(quantityText);
                    if (newQuantity > 0) {
                        int oldQuantity = order.getQuantity();
                        int receiverId = Integer.parseInt(selectedReceiver.split(" - ")[0].replace("Receiver Id ", ""));
                        int productId = Integer.parseInt(selectedProduct.split(" - ")[0].replace("Product Id: ", ""));
                        order.setReceiverId(receiverId);
                        order.setProductId(productId);
                        order.setQuantity(newQuantity);

                        // Update product count
                        Product product = products.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);
                        if (product != null) {
                            int difference = newQuantity - oldQuantity;
                            product.setProductCount(product.getProductCount() - difference);
                            ProductService.update(product);
                        }

                        OrderService.update(order);
                        refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
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
        addOrders();
    }

    private List<Order> getAllOrders() {
        Type orderListType = new TypeToken<List<Order>>() {}.getType();
        return new Gson().fromJson(OrderService.getAllOrdersJson(), orderListType);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleAddButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
    }
}
/*
package org.project.frames.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.models.Cargo;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.List;

public class Orders extends JPanel {
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private List<Order> orders;
    private OrderDetail orderDetail;
    private Cargos cargosPanel;
    private JPanel orderListPanel;
    private JScrollPane scrollPane;

    public Orders(Cargos cargosPanel) {
        this.cargosPanel = cargosPanel;
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, 600));
        add(scrollPane, BorderLayout.CENTER);

        addOrders();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        JButton addButton = new JButton("Add");
        styleAddButton(addButton);
        addButton.setPreferredSize(new Dimension(500, 125)); // Boyutu 2.5 katına çıkar
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addOrders() {
        orderListPanel.removeAll();
        for (Order order : orders) {
            orderListPanel.add(createOrderPanel(order));
        }
        orderListPanel.revalidate();
        orderListPanel.repaint();
    }

    private JPanel createOrderPanel(Order order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setMaximumSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));

        boolean isSentToCargo = CargoService.getAllCargos().stream()
                .anyMatch(cargo -> cargo.getOrderId().equals(order.getId()));

        orderPanel.setBackground(isSentToCargo ? Color.GREEN : Color.RED);

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton cargoButton = new JButton("Cargo'ya Ver");

        styleButton(detailsButton);
        styleButton(updateButton);
        styleButton(deleteButton, new Color(255, 69, 0));
        styleButton(cargoButton);

        detailsButton.addActionListener(e -> showDetailsDialog(order));
        updateButton.addActionListener(e -> showUpdateDialog(order));
        deleteButton.addActionListener(e -> {
            OrderService.delete(order.getId());
            refresh();
        });
        cargoButton.addActionListener(e -> {
            CargoService.add(new Cargo(false, false, order.getId()));
            JOptionPane.showMessageDialog(this, "Order has been sent to cargo.");
            cargosPanel.refresh(); // Cargos panelini güncelle
            refresh();
        });

        if (isSentToCargo) {
            detailsButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            cargoButton.setEnabled(false);
        }

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cargoButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    private void showDetailsDialog(Order order) {
        orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        List<Receiver> receivers = new ReceiverService().getAllReceivers();
        List<Product> products = new ProductService().getAllProducts();

        DefaultListModel<String> receiverListModel = new DefaultListModel<>();
        for (Receiver receiver : receivers) {
            receiverListModel.addElement("Receiver Id " + receiver.getId() + " - Receiver Name " + receiver.getName() + " " + receiver.getSurname());
        }
        JList<String> receiverList = new JList<>(receiverListModel);
        receiverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane receiverScrollPane = new JScrollPane(receiverList);

        DefaultListModel<String> productListModel = new DefaultListModel<>();
        for (Product product : products) {
            productListModel.addElement("Product Id: " + product.getId() + " - Product Name " + product.getName());
        }
        JList<String> productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane productScrollPane = new JScrollPane(productList);

        JTextField quantityField = new JTextField();

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverScrollPane);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productScrollPane);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton addButton = new JButton("Add");
        styleAddButton(addButton);
        addButton.addActionListener(e -> {
            String selectedReceiver = receiverList.getSelectedValue();
            String selectedProduct = productList.getSelectedValue();
            String quantityText = quantityField.getText();
            if (selectedReceiver != null && selectedProduct != null && !quantityText.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityText);
                    if (quantity > 0) {
                        int receiverId = Integer.parseInt(selectedReceiver.split(" - ")[0].replace("Receiver Id ", ""));
                        int productId = Integer.parseInt(selectedProduct.split(" - ")[0].replace("Product Id: ", ""));
                        Order order = new Order(productId, receiverId, quantity);
                        OrderService.add(order);

                        // Update product count
                        Product product = products.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);
                        if (product != null) {
                            product.setProductCount(product.getProductCount() - quantity);
                            ProductService.update(product);
                        }

                        refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        List<Receiver> receivers = new ReceiverService().getAllReceivers();
        List<Product> products = new ProductService().getAllProducts();

        DefaultListModel<String> receiverListModel = new DefaultListModel<>();
        for (Receiver receiver : receivers) {
            receiverListModel.addElement("Receiver Id " + receiver.getId() + " - Receiver Name " + receiver.getName() + " " + receiver.getSurname());
        }
        JList<String> receiverList = new JList<>(receiverListModel);
        receiverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        receiverList.setSelectedValue("Receiver Id " + order.getReceiverId() + " - Receiver Name " + receivers.stream().filter(r -> r.getId() == order.getReceiverId()).findFirst().get().getName() + " " + receivers.stream().filter(r -> r.getId() == order.getReceiverId()).findFirst().get().getSurname(), true);
        JScrollPane receiverScrollPane = new JScrollPane(receiverList);

        DefaultListModel<String> productListModel = new DefaultListModel<>();
        for (Product product : products) {
            productListModel.addElement("Product Id: " + product.getId() + " - Product Name " + product.getName());
        }
        JList<String> productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setSelectedValue("Product Id: " + order.getProductId() + " - Product Name " + products.stream().filter(p -> p.getId() == order.getProductId()).findFirst().get().getName(), true);
        JScrollPane productScrollPane = new JScrollPane(productList);

        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverScrollPane);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productScrollPane);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton updateButton = new JButton("Update");
        styleButton(updateButton);
        updateButton.addActionListener(e -> {
            String selectedReceiver = receiverList.getSelectedValue();
            String selectedProduct = productList.getSelectedValue();
            String quantityText = quantityField.getText();
            if (selectedReceiver != null && selectedProduct != null && !quantityText.isEmpty()) {
                try {
                    int newQuantity = Integer.parseInt(quantityText);
                    if (newQuantity > 0) {
                        int oldQuantity = order.getQuantity();
                        int receiverId = Integer.parseInt(selectedReceiver.split(" - ")[0].replace("Receiver Id ", ""));
                        int productId = Integer.parseInt(selectedProduct.split(" - ")[0].replace("Product Id: ", ""));
                        order.setReceiverId(receiverId);
                        order.setProductId(productId);
                        order.setQuantity(newQuantity);

                        // Update product count
                        Product product = products.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);
                        if (product != null) {
                            int difference = newQuantity - oldQuantity;
                            product.setProductCount(product.getProductCount() - difference);
                            ProductService.update(product);
                        }

                        OrderService.update(order);
                        refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
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
        addOrders();
    }

    private List<Order> getAllOrders() {
        Type orderListType = new TypeToken<List<Order>>() {}.getType();
        return new Gson().fromJson(OrderService.getAllOrdersJson(), orderListType);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleAddButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
    }
}
*/
/*package org.project.frames.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.models.Cargo;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.List;

public class Orders extends JPanel {
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private List<Order> orders;
    private OrderDetail orderDetail;
    private Cargos cargosPanel;
    private JPanel orderListPanel;
    private JScrollPane scrollPane;

    public Orders(Cargos cargosPanel) {
        this.cargosPanel = cargosPanel;
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, 600));
        add(scrollPane, BorderLayout.CENTER);

        addOrders();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        JButton addButton = new JButton("Add");
        styleAddButton(addButton);
        addButton.setPreferredSize(new Dimension(500, 125)); // Boyutu 2.5 katına çıkar
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addOrders() {
        orderListPanel.removeAll();
        for (Order order : orders) {
            orderListPanel.add(createOrderPanel(order));
        }
        orderListPanel.revalidate();
        orderListPanel.repaint();
    }

    private JPanel createOrderPanel(Order order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setMaximumSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));

        boolean isSentToCargo = CargoService.getAllCargos().stream()
                .anyMatch(cargo -> cargo.getOrderId().equals(order.getId()));

        orderPanel.setBackground(isSentToCargo ? Color.GREEN : Color.RED);

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton cargoButton = new JButton("Cargo'ya Ver");

        styleButton(detailsButton);
        styleButton(updateButton);
        styleButton(deleteButton, new Color(255, 69, 0));
        styleButton(cargoButton);

        detailsButton.addActionListener(e -> showDetailsDialog(order));
        updateButton.addActionListener(e -> showUpdateDialog(order));
        deleteButton.addActionListener(e -> {
            OrderService.delete(order.getId());
            refresh();
        });
        cargoButton.addActionListener(e -> {
            CargoService.add(new Cargo(false, false, order.getId()));
            JOptionPane.showMessageDialog(this, "Order has been sent to cargo.");
            cargosPanel.refresh(); // Cargos panelini güncelle
            refresh();
        });

        if (isSentToCargo) {
            detailsButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            cargoButton.setEnabled(false);
        }

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cargoButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    private void showDetailsDialog(Order order) {
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
        styleAddButton(addButton);
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            String quantityText = quantityField.getText();
            if (receiver != null && product != null && !quantityText.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityText);
                    if (quantity > 0) {
                        Order order = new Order(product.getId(), receiver.getId(), quantity);
                        OrderService.add(order);

                        // Update product count
                        product.setProductCount(product.getProductCount() - quantity);
                        ProductService.update(product);

                        refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(ReceiverService.getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(ProductService.getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        receiverComboBox.setSelectedItem(order.getReceiverId());
        productComboBox.setSelectedItem(order.getProductId());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton updateButton = new JButton("Update");
        styleButton(updateButton);
        updateButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            String quantityText = quantityField.getText();
            if (receiver != null && product != null && !quantityText.isEmpty()) {
                try {
                    int newQuantity = Integer.parseInt(quantityText);
                    if (newQuantity > 0) {
                        int oldQuantity = order.getQuantity();
                        order.setReceiverId(receiver.getId());
                        order.setProductId(product.getId());
                        order.setQuantity(newQuantity);

                        // Update product count
                        int difference = newQuantity - oldQuantity;
                        product.setProductCount(product.getProductCount() - difference);
                        ProductService.update(product);

                        OrderService.update(order);
                        refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
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
        addOrders();
    }

    private List<Order> getAllOrders() {
        Type orderListType = new TypeToken<List<Order>>() {}.getType();
        return new Gson().fromJson(OrderService.getAllOrdersJson(), orderListType);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleAddButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
    }
}*/
/*
package org.project.frames.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.models.Cargo;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.List;

public class Orders extends JPanel {
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private List<Order> orders;
    private OrderDetail orderDetail;
    private Cargos cargosPanel;
    private JPanel orderListPanel;
    private JScrollPane scrollPane;

    public Orders(Cargos cargosPanel) {
        this.cargosPanel = cargosPanel;
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, 600));
        add(scrollPane, BorderLayout.CENTER);

        addOrders();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // FlowLayout kullanımı

        JButton addButton = new JButton("Add");
        styleButton(addButton);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addOrders() {
        orderListPanel.removeAll();
        for (Order order : orders) {
            orderListPanel.add(createOrderPanel(order));
        }
        orderListPanel.revalidate();
        orderListPanel.repaint();
    }

    private JPanel createOrderPanel(Order order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setMaximumSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));

        boolean isSentToCargo = CargoService.getAllCargos().stream()
                .anyMatch(cargo -> cargo.getOrderId().equals(order.getId()));

        orderPanel.setBackground(isSentToCargo ? Color.GREEN : Color.RED);

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton cargoButton = new JButton("Cargo'ya Ver");

        styleButton(detailsButton);
        styleButton(updateButton);
        styleButton(deleteButton, new Color(255, 69, 0));
        styleButton(cargoButton);

        detailsButton.addActionListener(e -> showDetailsDialog(order));
        updateButton.addActionListener(e -> showUpdateDialog(order));
        deleteButton.addActionListener(e -> {
            OrderService.delete(order.getId());
            refresh();
        });
        cargoButton.addActionListener(e -> {
            CargoService.add(new Cargo(false, false, order.getId()));
            JOptionPane.showMessageDialog(this, "Order has been sent to cargo.");
            cargosPanel.refresh(); // Cargos panelini güncelle
            refresh();
        });

        if (isSentToCargo) {
            detailsButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            cargoButton.setEnabled(false);
        }

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cargoButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    private void showDetailsDialog(Order order) {
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
        styleButton(addButton);
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            String quantityText = quantityField.getText();
            if (receiver != null && product != null && !quantityText.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityText);
                    if (quantity > 0) {
                        Order order = new Order(product.getId(), receiver.getId(), quantity);
                        OrderService.add(order);

                        // Update product count
                        product.setProductCount(product.getProductCount() - quantity);
                        ProductService.update(product);

                        refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(ReceiverService.getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(ProductService.getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        receiverComboBox.setSelectedItem(order.getReceiverId());
        productComboBox.setSelectedItem(order.getProductId());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton updateButton = new JButton("Update");
        styleButton(updateButton);
        updateButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            String quantityText = quantityField.getText();
            if (receiver != null && product != null && !quantityText.isEmpty()) {
                try {
                    int newQuantity = Integer.parseInt(quantityText);
                    if (newQuantity > 0) {
                        int oldQuantity = order.getQuantity();
                        order.setReceiverId(receiver.getId());
                        order.setProductId(product.getId());
                        order.setQuantity(newQuantity);

                        // Update product count
                        int difference = newQuantity - oldQuantity;
                        product.setProductCount(product.getProductCount() - difference);
                        ProductService.update(product);

                        OrderService.update(order);
                        refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
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
        addOrders();
    }

    private List<Order> getAllOrders() {
        Type orderListType = new TypeToken<List<Order>>() {}.getType();
        return new Gson().fromJson(OrderService.getAllOrdersJson(), orderListType);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}*/
/*
package org.project.frames.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.models.Cargo;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.List;

public class Orders extends JPanel {
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private List<Order> orders;
    private OrderDetail orderDetail;
    private Cargos cargosPanel;
    private JPanel orderListPanel;
    private JScrollPane scrollPane;

    public Orders(Cargos cargosPanel) {
        this.cargosPanel = cargosPanel;
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, 600));
        add(scrollPane, BorderLayout.CENTER);

        addOrders();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 1, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addOrders() {
        orderListPanel.removeAll();
        for (Order order : orders) {
            orderListPanel.add(createOrderPanel(order));
        }
        orderListPanel.revalidate();
        orderListPanel.repaint();
    }

    private JPanel createOrderPanel(Order order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setMaximumSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));

        boolean isSentToCargo = CargoService.getAllCargos().stream()
                .anyMatch(cargo -> cargo.getOrderId().equals(order.getId()));

        orderPanel.setBackground(isSentToCargo ? Color.GREEN : Color.RED);

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton cargoButton = new JButton("Cargo'ya Ver");

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
            OrderService.delete(order.getId());
            refresh();
        });

        cargoButton.setBackground(new Color(0, 120, 215));
        cargoButton.setForeground(Color.WHITE);
        cargoButton.setFocusPainted(false);
        cargoButton.addActionListener(e -> {
            CargoService.add(new Cargo(false, false, order.getId()));
            JOptionPane.showMessageDialog(this, "Order has been sent to cargo.");
            cargosPanel.refresh(); // Cargos panelini güncelle
            refresh();
        });

        if (isSentToCargo) {
            detailsButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            cargoButton.setEnabled(false);
        }

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cargoButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    private void showDetailsDialog(Order order) {
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
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                if (receiver != null && product != null && quantity > 0) {
                    Order order = new Order(product.getId(), receiver.getId(), quantity);
                    OrderService.add(order);

                    // Update product count
                    product.setProductCount(product.getProductCount() - quantity);
                    ProductService.update(product);

                    refresh();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(ReceiverService.getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(ProductService.getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        receiverComboBox.setSelectedItem(order.getReceiverId());
        productComboBox.setSelectedItem(order.getProductId());

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
            try {
                int newQuantity = Integer.parseInt(quantityField.getText());
                if (receiver != null && product != null && newQuantity > 0) {
                    int oldQuantity = order.getQuantity();
                    order.setReceiverId(receiver.getId());
                    order.setProductId(product.getId());
                    order.setQuantity(newQuantity);

                    // Update product count
                    int difference = newQuantity - oldQuantity;
                    product.setProductCount(product.getProductCount() - difference);
                    ProductService.update(product);

                    OrderService.update(order);
                    refresh();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.orders = getAllOrders();
        addOrders();
    }

    private List<Order> getAllOrders() {
        Type orderListType = new TypeToken<List<Order>>() {}.getType();
        return new Gson().fromJson(OrderService.getAllOrdersJson(), orderListType);
    }
}
*/
/*
package org.project.frames.home.panels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.project.frames.home.panels.OrderDetail;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.models.Cargo;
import org.project.models.*;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.List;

public class Orders extends JPanel {
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private List<Order> orders;
    private OrderDetail orderDetail;
    private Cargos cargosPanel;
    private JPanel orderListPanel;
    private JScrollPane scrollPane;

    public Orders(Cargos cargosPanel) {
        this.cargosPanel = cargosPanel;
        this.orders = getAllOrders();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, 600));
        add(scrollPane, BorderLayout.CENTER);

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

    private void addOrders() {
        orderListPanel.removeAll();
        for (Order order : orders) {
            orderListPanel.add(createOrderPanel(order));
        }
        orderListPanel.revalidate();
        orderListPanel.repaint();
    }

    private JPanel createOrderPanel(Order order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setMaximumSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));

        boolean isSentToCargo = CargoService.getAllCargos().stream()
                .anyMatch(cargo -> cargo.getOrderId().equals(order.getId()));

        orderPanel.setBackground(isSentToCargo ? Color.GREEN : Color.RED);

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton cargoButton = new JButton("Cargo'ya Ver");

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
            OrderService.delete(order.getId());
            refresh();
        });

        cargoButton.setBackground(new Color(0, 120, 215));
        cargoButton.setForeground(Color.WHITE);
        cargoButton.setFocusPainted(false);
        cargoButton.addActionListener(e -> {
            CargoService.add(new Cargo(false, false,order.getId()));
            JOptionPane.showMessageDialog(this, "Order has been sent to cargo.");
            cargosPanel.refresh(); // Cargos panelini güncelle
            refresh();
        });

        if (isSentToCargo) {
            detailsButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            cargoButton.setEnabled(false);
        }

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cargoButton);

        orderPanel.add(buttonPanel, BorderLayout.EAST);

        return orderPanel;
    }

    private void showDetailsDialog(Order order) {
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
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                if (receiver != null && product != null && quantity > 0) {
                    Order order = new Order(product.getId(), receiver.getId(), quantity);
                    OrderService.add(order);

                    // Update product count
                    product.setProductCount(product.getProductCount() - quantity);
                    ProductService.update(product);

                    refresh();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(ReceiverService.getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(ProductService.getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        receiverComboBox.setSelectedItem(order.getReceiverId());
        productComboBox.setSelectedItem(order.getProductId());

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
            try {
                int newQuantity = Integer.parseInt(quantityField.getText());
                if (receiver != null && product != null && newQuantity > 0) {
                    int oldQuantity = order.getQuantity();
                    order.setReceiverId(receiver.getId());
                    order.setProductId(product.getId());
                    order.setQuantity(newQuantity);

                    // Update product count
                    int difference = newQuantity - oldQuantity;
                    product.setProductCount(product.getProductCount() - difference);
                    ProductService.update(product);

                    OrderService.update(order);
                    refresh();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "All fields must be filled and quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Quantity must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.orders = getAllOrders();
        addOrders();
    }

    private List<Order> getAllOrders() {
        Type orderListType = new TypeToken<List<Order>>() {}.getType();
        return new Gson().fromJson(OrderService.getAllOrdersJson(), orderListType);
    }
}*/
/*
package org.project.frames.home.panels;

import org.project.data.JsonRepository;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.models.Cargo;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class Orders extends JPanel {
    private int numRows;
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private List<Order> orders;
    private OrderDetail orderDetail;
    private Cargos cargosPanel;

    public Orders(Cargos cargosPanel) {
        this.cargosPanel = cargosPanel;
        this.orders = OrderService.getAllOrdersForCurrentStore();
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
        for (Order order : orders) {
            add(createOrderPanel(order));
        }
    }

    private JPanel createOrderPanel(Order order) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setBackground(new Color(255, 255, 255));
        orderPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton cargoButton = new JButton("Send to Cargo");

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
            OrderService.delete(order.getId());
            refresh();
        });

        cargoButton.setBackground(new Color(0, 120, 215));
        cargoButton.setForeground(Color.WHITE);
        cargoButton.setFocusPainted(false);
        cargoButton.addActionListener(e -> {
            Cargo cargo = new Cargo(false, order.getId());
            Boolean isAdded=CargoService.add(cargo);
            if(isAdded){
                JOptionPane.showMessageDialog(this, "Order has been sent to cargo.");
            }else {
                JOptionPane.showMessageDialog(this, "Order already been sent to cargo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            cargosPanel.refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cargoButton);

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

    private void showDetailsDialog(Order order) {
        orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(ReceiverService.getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(ProductService.getAllProducts().toArray(new Product[0]));
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
                Order order = new Order(product.id, receiver.getId(), quantity);
                OrderService.add(order);
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

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(ReceiverService.getAllReceivers().toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(ProductService.getAllProducts().toArray(new Product[0]));
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        receiverComboBox.setSelectedItem(order.getReceiverId());
        productComboBox.setSelectedItem(order.getProductId());

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
                order.setReceiverId(receiver.getId());
                order.setProductId(product.getId());
                order.setQuantity(newQuantity);

                JsonRepository<Product> productRepo = new JsonRepository<>(Product[].class);
                if (newQuantity > oldQuantity) {
                    int difference = newQuantity - oldQuantity;
                    product.setProductCount(product.getProductCount() - difference);
                } else if (newQuantity < oldQuantity) {
                    int difference = oldQuantity - newQuantity;
                    product.setProductCount(product.getProductCount() + difference);
                }
                productRepo.update(product);

                OrderService.update(order);
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
        this.orders = OrderService.getAllOrdersForCurrentStore();
        removeAll();
        initialize();
        revalidate();
        repaint();
    }
}
*/