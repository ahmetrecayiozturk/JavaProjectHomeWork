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
        JDialog dialog = new JDialog((Frame) null, "Order Details", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(null);

        orderDetail = new OrderDetail();
        orderDetail.setOrder(order);

        dialog.add(orderDetail);
        dialog.setVisible(true);
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
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private OrderDetail orderDetail;

    public OrdersPanel(CardLayout cardLayout, JPanel cardPanel, OrderDetail orderDetail) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.orderDetail = orderDetail;
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
        orderPanel.setBackground(new Color(100, 150, 100, 255));

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.addActionListener(e -> showDetailsDialog(order));
        updateButton.addActionListener(e -> showUpdateDialog(order));
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
        orderDetail.setOrder(order);
        cardLayout.show(cardPanel, "orderDetail");
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
    private int numColumns;
    private int remainedWith;
    private static final int ORDER_SIZE = 250;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private List<Order<Receiver, Product>> orders;
    private OrderDetail orderDetail;
    private OrderService orderService = new OrderService();
    private ReceiverService receiverService = new ReceiverService();
    private ProductService productService = new ProductService();

    public OrdersPanel(CardLayout cardLayout, JPanel cardPanel, OrderDetail orderDetail) {
        this.numColumns = 2;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.orderDetail = orderDetail;
        initialize();
    }

    public void initialize() {
        this.orders = getAllOrders();
        setLayout(null);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumColumns();
                revalidate();
                repaint();
            }
        });

        addOrders();
    }

    private void updateNumColumns() {
        int panelWidth = getWidth();
        numColumns = Math.max(1, panelWidth / (ORDER_SIZE + 10));
        remainedWith = panelWidth - (numColumns * (ORDER_SIZE + 10) - 10);
    }

    private void addOrders() {
        for (Order<Receiver, Product> order : orders) {
            add(createOrderPanel(order));
        }
    }

    private JPanel createOrderPanel(Order<Receiver, Product> order) {
        JPanel orderPanel = new JPanel();
        orderPanel.setPreferredSize(new Dimension(ORDER_SIZE, ORDER_SIZE));
        orderPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        orderPanel.setLayout(new BorderLayout(10, 10));
        orderPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        orderLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        topPanel.add(orderLabel, BorderLayout.CENTER);

        JPanel receiverPanel = new JPanel(new BorderLayout());
        receiverPanel.setBackground(Color.WHITE);
        JLabel receiverLabel = new JLabel("Receiver: " + order.getEntity1().getName() + " " + order.getEntity1().getSurname());
        receiverLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        receiverPanel.add(receiverLabel, BorderLayout.CENTER);

        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(Color.WHITE);
        JLabel productLabel = new JLabel("Product: " + order.getEntity2().getName());
        productLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        productPanel.add(productLabel, BorderLayout.CENTER);

        orderPanel.add(topPanel, BorderLayout.NORTH);
        orderPanel.add(receiverPanel, BorderLayout.CENTER);
        orderPanel.add(productPanel, BorderLayout.SOUTH);

        orderPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                orderPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2));
                orderPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                orderPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                orderPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                orderDetail.setOrder(order);
                cardLayout.show(cardPanel, "orderDetail");
            }
        });

        return orderPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int panelWidth = getWidth();
        int columnWidth = ORDER_SIZE + 10;
        int x = 0;
        int y = 0;

        for (Component component : getComponents()) {
            if (x >= numColumns) {
                x = 0;
                y++;
            }
            component.setBounds(x * columnWidth + remainedWith / 2, y * (ORDER_SIZE + 10) + ORDER_SIZE / 6, ORDER_SIZE, ORDER_SIZE);
            x++;
        }
    }

    public void refresh() {
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
import java.util.List;

public class OrdersPanel extends JPanel {
    private int numRows;
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private final OrderService orderService = new OrderService();
    private final ReceiverService receiverService = new ReceiverService();
    private final ProductService productService = new ProductService();
    private List<Order> orders;
    private List<Receiver> receivers;
    private List<Product> products;

    public OrdersPanel() {
        this.orders = orderService.getAllOrders();
        this.receivers = receiverService.getAllReceivers();
        this.products = productService.getAllProducts();
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
        orderPanel.setBackground(new Color(100, 150, 100, 255));

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        orderPanel.add(orderLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.addActionListener(e -> showDetailsDialog(order));
        updateButton.addActionListener(e -> showUpdateDialog(order));
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

    private void showDetailsDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Order Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(order.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(receivers.toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(products.toArray(new Product[0]));

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            if (receiver != null && product != null) {
                Order order = new Order(receiver, product);
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

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(receivers.toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(products.toArray(new Product[0]));

        receiverComboBox.setSelectedItem(order.getEntity1());
        productComboBox.setSelectedItem(order.getEntity2());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        JButton updateButton = new JButton("Update");
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
        this.orders = orderService.getAllOrders();
        removeAll();
        initialize();
    }
}
*/
/*
package org.project.frames.home.home.panels;

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
import java.util.List;

public class OrdersPanel extends JPanel {
    private int numRows;
    private static final int ORDER_MAX_WIDTH = 1000;
    private static final int ORDER_HEIGHT = 50;
    private final OrderService orderService = new OrderService();
    private final ReceiverService receiverService = new ReceiverService();
    private final ProductService productService = new ProductService();
    private List<Order> orders;
    private List<Receiver> receivers;
    private List<Product> products;

    public OrdersPanel() {
        this.orders = orderService.getAllOrders();
        this.receivers = receiverService.getAllReceivers();
        this.products = productService.getAllProducts();
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
        JPanel orderPanel = new JPanel();
        orderPanel.setPreferredSize(new Dimension(ORDER_MAX_WIDTH, ORDER_HEIGHT));
        orderPanel.setBackground(new Color(100, 150, 100, 255));

        JLabel orderLabel = new JLabel("Order ID: " + order.getId());
        orderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orderPanel.add(orderLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.addActionListener(e -> showDetailsDialog(order));
        updateButton.addActionListener(e -> showUpdateDialog(order));
        deleteButton.addActionListener(e -> {
            orderService.delete(order.getId());
            refresh();
        });

        orderPanel.add(detailsButton);
        orderPanel.add(updateButton);
        orderPanel.add(deleteButton);

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
        JDialog dialog = new JDialog((Frame) null, "Order Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(order.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(receivers.toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(products.toArray(new Product[0]));

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            if (receiver != null && product != null) {
                Order order = new Order(receiver, product);
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

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(receivers.toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(products.toArray(new Product[0]));

        receiverComboBox.setSelectedItem(order.getEntity1());
        productComboBox.setSelectedItem(order.getEntity2());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        JButton updateButton = new JButton("Update");
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
        this.orders = orderService.getAllOrders();
        removeAll();
        initialize();
    }
}
*/
/*
package org.project.frames.home.home.panels;

import org.project.App;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrdersPanel extends JPanel {
    private List<Order> orders;
    private final OrderService orderService = new OrderService();
    private final ReceiverService receiverService = new ReceiverService();
    private final ProductService productService = new ProductService();
    private List<Receiver> receivers;
    private List<Product> products;
    private JPanel ordersListPanel;

    public OrdersPanel() {
        this.orders = orderService.getAllOrders();
        this.receivers = receiverService.getAllReceivers();
        this.products = productService.getAllProducts();
        setLayout(new BorderLayout());

        ordersListPanel = new JPanel(new GridBagLayout());
        loadOrders();

        JScrollPane scrollPane = new JScrollPane(ordersListPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        ordersListPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Kartların üstten başlamasını sağlar
        gbc.insets = new Insets(10, 10, 10, 10);

        for (Order order : orders) {
            ordersListPanel.add(createOrderPanel(order), gbc);
            gbc.gridy++;
        }
        revalidate();
        repaint();
    }

    private JPanel createOrderPanel(Order order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("Order ID: " + order.getId());
        JTextArea contentArea = new JTextArea(order.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        infoPanel.add(titleLabel);
        infoPanel.add(new JScrollPane(contentArea));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.addActionListener(e -> showDetailsDialog(order));
        updateButton.addActionListener(e -> showUpdateDialog(order));
        deleteButton.addActionListener(e -> {
            orderService.delete(order.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showDetailsDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Order Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(order.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(receivers.toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(products.toArray(new Product[0]));

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            if (receiver != null && product != null) {
                Order order = new Order(receiver, product);
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

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(receivers.toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(products.toArray(new Product[0]));

        receiverComboBox.setSelectedItem(order.getEntity1());
        productComboBox.setSelectedItem(order.getEntity2());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        JButton updateButton = new JButton("Update");
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
        this.orders = orderService.getAllOrders();
        loadOrders();
    }
}
*/
/*
package org.project.frames.home.home.panels;

import org.project.App;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrdersPanel extends JPanel {
    private List<Order> orders;
    private final OrderService orderService=new OrderService();
    private final ReceiverService receiverService=new ReceiverService();
    private final ProductService productService=new ProductService();
    private List<Receiver> receivers;
    private List<Product> products;
    private JPanel ordersListPanel;

    public OrdersPanel() {
        this.orders = orderService.getAllOrders();
        this.receivers = receiverService.getAllReceivers();
        this.products = productService.getAllProducts();
        setLayout(new BorderLayout());

        ordersListPanel = new JPanel(new GridBagLayout());
        loadOrders();

        JScrollPane scrollPane = new JScrollPane(ordersListPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        ordersListPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH; // Kartların üstten başlamasını sağlar
        gbc.insets = new Insets(10, 10, 10, 10);

        for (Order order : orders) {
            ordersListPanel.add(createOrderPanel(order), gbc);
            gbc.gridy++;
        }
        revalidate();
        repaint();
    }

    private JPanel createOrderPanel(Order order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("Order ID: " + order.getId());
        JTextArea contentArea = new JTextArea(order.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        infoPanel.add(titleLabel);
        infoPanel.add(new JScrollPane(contentArea));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.addActionListener(e -> showDetailsDialog(order));
        updateButton.addActionListener(e -> showUpdateDialog(order));
        deleteButton.addActionListener(e -> {
            orderService.delete(order.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showDetailsDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Order Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(order.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(receivers.toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(products.toArray(new Product[0]));

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            if (receiver != null && product != null) {
                Order order = new Order(receiver, product);
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

    private void showUpdateDialog(Order order) {
        JDialog dialog = new JDialog((Frame) null, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Receiver> receiverComboBox = new JComboBox<>(receivers.toArray(new Receiver[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(products.toArray(new Product[0]));

        receiverComboBox.setSelectedItem(order.getEntity1());
        productComboBox.setSelectedItem(order.getEntity2());

        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverComboBox);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        JButton updateButton = new JButton("Update");
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
        this.orders = orderService.getAllOrders();
        loadOrders();
    }
}
*/