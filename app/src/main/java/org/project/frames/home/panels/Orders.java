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
        buttonPanel.setPreferredSize(new Dimension(300, 100)); // Boyutu yüzde 30 küçült

        JButton addButton = new JButton("Add");
        styleAddButton(addButton);
        addButton.setPreferredSize(new Dimension(300, 100)); // Boyutu yüzde 30 küçült
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
        receivers.stream().filter(r -> r.getId() == order.getReceiverId()).findFirst().ifPresent(receiver -> {
            receiverList.setSelectedValue("Receiver Id " + receiver.getId() + " - Receiver Name " + receiver.getName() + " " + receiver.getSurname(), true);
        });
        JScrollPane receiverScrollPane = new JScrollPane(receiverList);

        DefaultListModel<String> productListModel = new DefaultListModel<>();
        for (Product product : products) {
            productListModel.addElement("Product Id: " + product.getId() + " - Product Name " + product.getName());
        }
        JList<String> productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        products.stream().filter(p -> p.getId() == order.getProductId()).findFirst().ifPresent(product -> {
            productList.setSelectedValue("Product Id: " + product.getId() + " - Product Name " + product.getName(), true);
        });
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
                            int difference = (newQuantity - oldQuantity);
                            System.out.println(newQuantity);
                            System.out.println(oldQuantity);
                            System.out.println(difference);
                            product.setProductCount(product.getProductCount() - difference);
                            System.out.println(product.getProductCount());
                            System.out.println(product.getProductCount() - difference);
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