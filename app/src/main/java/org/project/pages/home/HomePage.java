package org.project.pages.home;

import org.project.panels.*;
import org.project.data.JsonRepository;
import org.project.models.*;
import org.project.services.*;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;

public class HomePage extends JPanel {
    private OrderService orderService;
    private CargoService cargoService;
    private ProductService productService;
    private ReceiverService receiverService;
    private StoreService storeService;

    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private String currentPanel;

    private OrdersPanel ordersPanel;
    private CargosPanel cargosPanel;
    private ProductsPanel productsPanel;
    private ReceiversPanel receiversPanel;
    private StoresPanel storesPanel;
    // Image Service
    String appDataPath = System.getenv("APPDATA");
    String appStorageDirectory = Paths.get(appDataPath, ".myapp").toString();
    ImageService imageService = new ImageService(appStorageDirectory);

    public HomePage(Frame frame, CardLayout cardLayout, JPanel contentPanel) {
        orderService = new OrderService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json", Order[].class));
        cargoService = new CargoService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json", Cargo[].class));
        productService = new ProductService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/products.json", Product[].class),imageService);
        receiverService = new ReceiverService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json", Receiver[].class));
        storeService = new StoreService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json", Store[].class));

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        add(buttonPanel, BorderLayout.WEST);

        JButton ordersButton = new JButton("Orders");
        JButton cargosButton = new JButton("Cargos");
        JButton productsButton = new JButton("Products");
        JButton receiversButton = new JButton("Receivers");
        JButton storesButton = new JButton("Stores");

        buttonPanel.add(ordersButton);
        buttonPanel.add(cargosButton);
        buttonPanel.add(productsButton);
        buttonPanel.add(receiversButton);
        buttonPanel.add(storesButton);

        cardsPanel = new JPanel(new CardLayout());
        add(new JScrollPane(cardsPanel), BorderLayout.CENTER);

        ordersPanel = new OrdersPanel(orderService, receiverService, productService);
        cargosPanel = new CargosPanel(cargoService, orderService);
        productsPanel = new ProductsPanel(productService,imageService);
        receiversPanel = new ReceiversPanel(receiverService, orderService, cargoService);
        storesPanel = new StoresPanel(storeService);

        cardsPanel.add(new JScrollPane(ordersPanel), "Orders");
        cardsPanel.add(new JScrollPane(cargosPanel), "Cargos");
        cardsPanel.add(new JScrollPane(productsPanel), "Products");
        cardsPanel.add(new JScrollPane(receiversPanel), "Receivers");
        cardsPanel.add(new JScrollPane(storesPanel), "Stores");

        this.cardLayout = (CardLayout) cardsPanel.getLayout();

        ordersButton.addActionListener(e -> showPanel("Orders"));
        cargosButton.addActionListener(e -> showPanel("Cargos"));
        productsButton.addActionListener(e -> showPanel("Products"));
        receiversButton.addActionListener(e -> showPanel("Receivers"));
        storesButton.addActionListener(e -> showPanel("Stores"));

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        add(actionPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        addButton.addActionListener(e -> showAddDialog());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> showDeleteDialog());
    }

    private void showPanel(String panelName) {
        currentPanel = panelName;
        cardLayout.show(cardsPanel, panelName);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        switch (currentPanel) {
            case "Orders":
                JComboBox<Receiver> receiverComboBox = new JComboBox<>(receiverService.getAllReceivers().toArray(new Receiver[0]));
                JComboBox<Product> productComboBox = new JComboBox<>(productService.getAllProducts().toArray(new Product[0]));
                formPanel.add(new JLabel("Receiver:"));
                formPanel.add(receiverComboBox);
                formPanel.add(new JLabel("Product:"));
                formPanel.add(productComboBox);

                JButton addOrderButton = new JButton("Add");
                addOrderButton.addActionListener(e -> {
                    Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
                    Product product = (Product) productComboBox.getSelectedItem();
                    if (receiver != null && product != null) {
                        Order order = new Order(receiver, product);
                        orderService.add(order);
                        ordersPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid Receiver or Product", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(addOrderButton, BorderLayout.SOUTH);
                break;

            case "Products":
                JTextField nameField = new JTextField();
                JTextField descriptionField = new JTextField();
                JTextField sellerIdField = new JTextField();
                JTextField imageUrlField = new JTextField();
                JTextField priceField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(nameField);
                formPanel.add(new JLabel("Description:"));
                formPanel.add(descriptionField);
                formPanel.add(new JLabel("Seller ID:"));
                formPanel.add(sellerIdField);
                formPanel.add(new JLabel("Image URL:"));
                formPanel.add(imageUrlField);
                formPanel.add(new JLabel("Price:"));
                formPanel.add(priceField);

                JButton addProductButton = new JButton("Add");
                addProductButton.addActionListener(e -> {
                    String name = nameField.getText();
                    String description = descriptionField.getText();
                    String sellerId = sellerIdField.getText();
                    String imageUrl = imageUrlField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    Product product = new Product(name, description, sellerId, imageUrl, price);
                    productService.add(product);
                    productsPanel.refresh();
                    dialog.dispose();
                });
                dialog.add(addProductButton, BorderLayout.SOUTH);
                break;

            case "Receivers":
                JTextField emailField = new JTextField();
                JTextField passwordField = new JTextField();
                JTextField receiverNameField = new JTextField();
                JTextField surnameField = new JTextField();
                JTextField addressField = new JTextField();
                formPanel.add(new JLabel("Email:"));
                formPanel.add(emailField);
                formPanel.add(new JLabel("Password:"));
                formPanel.add(passwordField);
                formPanel.add(new JLabel("Name:"));
                formPanel.add(receiverNameField);
                formPanel.add(new JLabel("Surname:"));
                formPanel.add(surnameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(addressField);

                JButton addReceiverButton = new JButton("Add");
                addReceiverButton.addActionListener(e -> {
                    String email = emailField.getText();
                    String password = passwordField.getText();
                    String name = receiverNameField.getText();
                    String surname = surnameField.getText();
                    String address = addressField.getText();
                    Receiver receiver = new Receiver(email, password, name, surname, address);
                    receiverService.add(receiver);
                    receiversPanel.refresh();
                    dialog.dispose();
                });
                dialog.add(addReceiverButton, BorderLayout.SOUTH);
                break;

            case "Cargos":
                JComboBox<Order> orderComboBox = new JComboBox<>(orderService.getAllOrders().toArray(new Order[0]));
                JCheckBox isDeliveredCheckBox = new JCheckBox();
                formPanel.add(new JLabel("Order:"));
                formPanel.add(orderComboBox);
                formPanel.add(new JLabel("Is Delivered:"));
                formPanel.add(isDeliveredCheckBox);

                JButton addCargoButton = new JButton("Add");
                addCargoButton.addActionListener(e -> {
                    Order order = (Order) orderComboBox.getSelectedItem();
                    boolean isDelivered = isDeliveredCheckBox.isSelected();
                    if (order != null) {
                        Cargo cargo = new Cargo(isDelivered, order);
                        cargoService.add(cargo);
                        cargosPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid Order", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(addCargoButton, BorderLayout.SOUTH);
                break;

            case "Stores":
                JTextField storeNameField = new JTextField();
                JTextField storeAddressField = new JTextField();
                JTextField storePhoneField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(storeNameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(storeAddressField);
                formPanel.add(new JLabel("Phone:"));
                formPanel.add(storePhoneField);

                JButton addStoreButton = new JButton("Add");
                addStoreButton.addActionListener(e -> {
                    String name = storeNameField.getText();
                    String address = storeAddressField.getText();
                    String phone = storePhoneField.getText();
                    Store store = new Store(name, address, phone);
                    storeService.add(store);
                    storesPanel.refresh();
                    dialog.dispose();
                });
                dialog.add(addStoreButton, BorderLayout.SOUTH);
                break;
        }

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showUpdateDialog() {
        JDialog dialog = new JDialog((Frame) null, "Update", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField idField = new JTextField();
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);

        switch (currentPanel) {
            case "Orders":
                JComboBox<Receiver> receiverComboBox = new JComboBox<>(receiverService.getAllReceivers().toArray(new Receiver[0]));
                JComboBox<Product> productComboBox = new JComboBox<>(productService.getAllProducts().toArray(new Product[0]));
                formPanel.add(new JLabel("Receiver:"));
                formPanel.add(receiverComboBox);
                formPanel.add(new JLabel("Product:"));
                formPanel.add(productComboBox);

                JButton updateOrderButton = new JButton("Update");
                updateOrderButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
                    Product product = (Product) productComboBox.getSelectedItem();
                    Order order = orderService.getOrderById(id);
                    if (order != null && receiver != null && product != null) {
                        order.setEntity1(receiver);
                        order.setEntity2(product);
                        orderService.update(order);
                        ordersPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid ID, Receiver or Product", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateOrderButton, BorderLayout.SOUTH);
                break;

            case "Products":
                JTextField nameField = new JTextField();
                JTextField descriptionField = new JTextField();
                JTextField sellerIdField = new JTextField();
                JTextField imageUrlField = new JTextField();
                JTextField priceField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(nameField);
                formPanel.add(new JLabel("Description:"));
                formPanel.add(descriptionField);
                formPanel.add(new JLabel("Seller ID:"));
                formPanel.add(sellerIdField);
                formPanel.add(new JLabel("Image URL:"));
                formPanel.add(imageUrlField);
                formPanel.add(new JLabel("Price:"));
                formPanel.add(priceField);

                JButton updateProductButton = new JButton("Update");
                updateProductButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    String description = descriptionField.getText();
                    String sellerId = sellerIdField.getText();
                    String imageUrl = imageUrlField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    Product product = productService.getProductById(id);
                    if (product != null) {
                        product.setName(name);
                        product.setDescription(description);
                        product.setSellerId(sellerId);
                        product.setImageUrl(imageUrl);
                        product.setPrice(price);
                        productService.update(product);
                        productsPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateProductButton, BorderLayout.SOUTH);
                break;

            case "Receivers":
                JTextField emailField = new JTextField();
                JTextField passwordField = new JTextField();
                JTextField receiverNameField = new JTextField();
                JTextField surnameField = new JTextField();
                JTextField addressField = new JTextField();
                formPanel.add(new JLabel("Email:"));
                formPanel.add(emailField);
                formPanel.add(new JLabel("Password:"));
                formPanel.add(passwordField);
                formPanel.add(new JLabel("Name:"));
                formPanel.add(receiverNameField);
                formPanel.add(new JLabel("Surname:"));
                formPanel.add(surnameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(addressField);

                JButton updateReceiverButton = new JButton("Update");
                updateReceiverButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    String email = emailField.getText();
                    String password = passwordField.getText();
                    String name = receiverNameField.getText();
                    String surname = surnameField.getText();
                    String address = addressField.getText();
                    Receiver receiver = receiverService.getReceiverById(id);
                    if (receiver != null) {
                        receiver.setEmail(email);
                        receiver.setPassword(password);
                        receiver.setName(name);
                        receiver.setSurname(surname);
                        receiver.setAddress(address);
                        receiverService.update(receiver);
                        receiversPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Receiver not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateReceiverButton, BorderLayout.SOUTH);
                break;

            case "Cargos":
                JComboBox<Order> orderComboBox = new JComboBox<>(orderService.getAllOrders().toArray(new Order[0]));
                orderComboBox.setSelectedItem(null);
                JCheckBox isDeliveredCheckBox = new JCheckBox();
                formPanel.add(new JLabel("Order:"));
                formPanel.add(orderComboBox);
                formPanel.add(new JLabel("Is Delivered:"));
                formPanel.add(isDeliveredCheckBox);

                JButton updateCargoButton = new JButton("Update");
                updateCargoButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    Order order = (Order) orderComboBox.getSelectedItem();
                    boolean isDelivered = isDeliveredCheckBox.isSelected();
                    Cargo cargo = cargoService.getCargoById(id);
                    if (cargo != null && order != null) {
                        cargo.setEntity(order);
                        cargo.setDelivered(isDelivered);
                        cargoService.update(cargo);
                        cargosPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid ID or Order", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateCargoButton, BorderLayout.SOUTH);
                break;

            case "Stores":
                JTextField storeNameField = new JTextField();
                JTextField storeAddressField = new JTextField();
                JTextField storePhoneField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(storeNameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(storeAddressField);
                formPanel.add(new JLabel("Phone:"));
                formPanel.add(storePhoneField);

                JButton updateStoreButton = new JButton("Update");
                updateStoreButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    String name = storeNameField.getText();
                    String address = storeAddressField.getText();
                    String phone = storePhoneField.getText();
                    Store store = storeService.getStoreById(id);
                    if (store != null) {
                        store.setName(name);
                        store.setAddress(address);
                        store.setPhone(phone);
                        storeService.update(store);
                        storesPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Store not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateStoreButton, BorderLayout.SOUTH);
                break;
        }

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showDeleteDialog() {
        JDialog dialog = new JDialog((Frame) null, "Delete", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField idField = new JTextField();
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            switch (currentPanel) {
                case "Orders":
                    orderService.delete(id);
                    ordersPanel.refresh();
                    break;
                case "Products":
                    productService.delete(id);
                    productsPanel.refresh();
                    break;
                case "Receivers":
                    receiverService.delete(id);
                    receiversPanel.refresh();
                    break;
                case "Cargos":
                    cargoService.delete(id);
                    cargosPanel.refresh();
                    break;
                case "Stores":
                    storeService.delete(id);
                    storesPanel.refresh();
                    break;
            }
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(deleteButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDetailsDialog(Object entity) {
        JDialog dialog = new JDialog((Frame) null, "Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(entity.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}
/*
package org.project.pages.home;

import org.project.panels.*;
import org.project.data.JsonRepository;
import org.project.models.*;
import org.project.services.*;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {
    private OrderService orderService;
    private CargoService cargoService;
    private ProductService productService;
    private ReceiverService receiverService;
    private StoreService storeService;

    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private String currentPanel;

    private OrdersPanel ordersPanel;
    private CargosPanel cargosPanel;
    private ProductsPanel productsPanel;
    private ReceiversPanel receiversPanel;
    private StoresPanel storesPanel;

    public HomePage(Frame frame, CardLayout cardLayout, JPanel contentPanel) {
        orderService = new OrderService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json", Order[].class));
        cargoService = new CargoService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json", Cargo[].class));
        productService = new ProductService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/products.json", Product[].class));
        receiverService = new ReceiverService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json", Receiver[].class));
        storeService = new StoreService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json", Store[].class));

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        add(buttonPanel, BorderLayout.WEST);

        JButton ordersButton = new JButton("Orders");
        JButton cargosButton = new JButton("Cargos");
        JButton productsButton = new JButton("Products");
        JButton receiversButton = new JButton("Receivers");
        JButton storesButton = new JButton("Stores");

        buttonPanel.add(ordersButton);
        buttonPanel.add(cargosButton);
        buttonPanel.add(productsButton);
        buttonPanel.add(receiversButton);
        buttonPanel.add(storesButton);

        cardsPanel = new JPanel(new CardLayout());
        add(new JScrollPane(cardsPanel), BorderLayout.CENTER);

        ordersPanel = new OrdersPanel(orderService, receiverService, productService);
        cargosPanel = new CargosPanel(cargoService, orderService);
        productsPanel = new ProductsPanel(productService);
        receiversPanel = new ReceiversPanel(receiverService, orderService, cargoService);
        storesPanel = new StoresPanel(storeService);

        cardsPanel.add(new JScrollPane(ordersPanel), "Orders");
        cardsPanel.add(new JScrollPane(cargosPanel), "Cargos");
        cardsPanel.add(new JScrollPane(productsPanel), "Products");
        cardsPanel.add(new JScrollPane(receiversPanel), "Receivers");
        cardsPanel.add(new JScrollPane(storesPanel), "Stores");

        this.cardLayout = (CardLayout) cardsPanel.getLayout();

        ordersButton.addActionListener(e -> showPanel("Orders"));
        cargosButton.addActionListener(e -> showPanel("Cargos"));
        productsButton.addActionListener(e -> showPanel("Products"));
        receiversButton.addActionListener(e -> showPanel("Receivers"));
        storesButton.addActionListener(e -> showPanel("Stores"));

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        add(actionPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        addButton.addActionListener(e -> showAddDialog());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> showDeleteDialog());
    }

    private void showPanel(String panelName) {
        currentPanel = panelName;
        cardLayout.show(cardsPanel, panelName);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        switch (currentPanel) {
            case "Orders":
                JComboBox<Receiver> receiverComboBox = new JComboBox<>(receiverService.getAllReceivers().toArray(new Receiver[0]));
                JComboBox<Product> productComboBox = new JComboBox<>(productService.getAllProducts().toArray(new Product[0]));
                formPanel.add(new JLabel("Receiver:"));
                formPanel.add(receiverComboBox);
                formPanel.add(new JLabel("Product:"));
                formPanel.add(productComboBox);

                JButton addOrderButton = new JButton("Add");
                addOrderButton.addActionListener(e -> {
                    Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
                    Product product = (Product) productComboBox.getSelectedItem();
                    if (receiver != null && product != null) {
                        Order order = new Order(receiver, product);
                        orderService.add(order);
                        ordersPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid Receiver or Product", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(addOrderButton, BorderLayout.SOUTH);
                break;

            case "Products":
                JTextField nameField = new JTextField();
                JTextField descriptionField = new JTextField();
                JTextField sellerIdField = new JTextField();
                JTextField imageUrlField = new JTextField();
                JTextField priceField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(nameField);
                formPanel.add(new JLabel("Description:"));
                formPanel.add(descriptionField);
                formPanel.add(new JLabel("Seller ID:"));
                formPanel.add(sellerIdField);
                formPanel.add(new JLabel("Image URL:"));
                formPanel.add(imageUrlField);
                formPanel.add(new JLabel("Price:"));
                formPanel.add(priceField);

                JButton addProductButton = new JButton("Add");
                addProductButton.addActionListener(e -> {
                    String name = nameField.getText();
                    String description = descriptionField.getText();
                    String sellerId = sellerIdField.getText();
                    String imageUrl = imageUrlField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    Product product = new Product(name, description, sellerId, imageUrl, price);
                    productService.add(product);
                    productsPanel.refresh();
                    dialog.dispose();
                });
                dialog.add(addProductButton, BorderLayout.SOUTH);
                break;

            case "Receivers":
                JTextField emailField = new JTextField();
                JTextField passwordField = new JTextField();
                JTextField receiverNameField = new JTextField();
                JTextField surnameField = new JTextField();
                JTextField addressField = new JTextField();
                formPanel.add(new JLabel("Email:"));
                formPanel.add(emailField);
                formPanel.add(new JLabel("Password:"));
                formPanel.add(passwordField);
                formPanel.add(new JLabel("Name:"));
                formPanel.add(receiverNameField);
                formPanel.add(new JLabel("Surname:"));
                formPanel.add(surnameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(addressField);

                JButton addReceiverButton = new JButton("Add");
                addReceiverButton.addActionListener(e -> {
                    String email = emailField.getText();
                    String password = passwordField.getText();
                    String name = receiverNameField.getText();
                    String surname = surnameField.getText();
                    String address = addressField.getText();
                    Receiver receiver = new Receiver(email, password, name, surname, address);
                    receiverService.add(receiver);
                    receiversPanel.refresh();
                    dialog.dispose();
                });
                dialog.add(addReceiverButton, BorderLayout.SOUTH);
                break;

            case "Cargos":
                JComboBox<Order> orderComboBox = new JComboBox<>(orderService.getAllOrders().toArray(new Order[0]));
                JCheckBox isDeliveredCheckBox = new JCheckBox();
                formPanel.add(new JLabel("Order:"));
                formPanel.add(orderComboBox);
                formPanel.add(new JLabel("Is Delivered:"));
                formPanel.add(isDeliveredCheckBox);

                JButton addCargoButton = new JButton("Add");
                addCargoButton.addActionListener(e -> {
                    Order order = (Order) orderComboBox.getSelectedItem();
                    boolean isDelivered = isDeliveredCheckBox.isSelected();
                    if (order != null) {
                        Cargo cargo = new Cargo(isDelivered, order);
                        cargoService.add(cargo);
                        cargosPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid Order", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(addCargoButton, BorderLayout.SOUTH);
                break;

            case "Stores":
                JTextField storeNameField = new JTextField();
                JTextField storeAddressField = new JTextField();
                JTextField storePhoneField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(storeNameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(storeAddressField);
                formPanel.add(new JLabel("Phone:"));
                formPanel.add(storePhoneField);

                JButton addStoreButton = new JButton("Add");
                addStoreButton.addActionListener(e -> {
                    String name = storeNameField.getText();
                    String address = storeAddressField.getText();
                    String phone = storePhoneField.getText();
                    Store store = new Store(name, address, phone);
                    storeService.add(store);
                    storesPanel.refresh();
                    dialog.dispose();
                });
                dialog.add(addStoreButton, BorderLayout.SOUTH);
                break;
        }

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showUpdateDialog() {
        JDialog dialog = new JDialog((Frame) null, "Update", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField idField = new JTextField();
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);

        switch (currentPanel) {
            case "Orders":
                JComboBox<Receiver> receiverComboBox = new JComboBox<>(receiverService.getAllReceivers().toArray(new Receiver[0]));
                JComboBox<Product> productComboBox = new JComboBox<>(productService.getAllProducts().toArray(new Product[0]));
                formPanel.add(new JLabel("Receiver:"));
                formPanel.add(receiverComboBox);
                formPanel.add(new JLabel("Product:"));
                formPanel.add(productComboBox);

                JButton updateOrderButton = new JButton("Update");
                updateOrderButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
                    Product product = (Product) productComboBox.getSelectedItem();
                    Order order = orderService.getOrderById(id);
                    if (order != null && receiver != null && product != null) {
                        order.setEntity1(receiver);
                        order.setEntity2(product);
                        orderService.update(order);
                        ordersPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid ID, Receiver or Product", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateOrderButton, BorderLayout.SOUTH);
                break;

            case "Products":
                JTextField nameField = new JTextField();
                JTextField descriptionField = new JTextField();
                JTextField sellerIdField = new JTextField();
                JTextField imageUrlField = new JTextField();
                JTextField priceField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(nameField);
                formPanel.add(new JLabel("Description:"));
                formPanel.add(descriptionField);
                formPanel.add(new JLabel("Seller ID:"));
                formPanel.add(sellerIdField);
                formPanel.add(new JLabel("Image URL:"));
                formPanel.add(imageUrlField);
                formPanel.add(new JLabel("Price:"));
                formPanel.add(priceField);

                JButton updateProductButton = new JButton("Update");
                updateProductButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    String description = descriptionField.getText();
                    String sellerId = sellerIdField.getText();
                    String imageUrl = imageUrlField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    Product product = productService.getProductById(id);
                    if (product != null) {
                        product.setName(name);
                        product.setDescription(description);
                        product.setSellerId(sellerId);
                        product.setImageUrl(imageUrl);
                        product.setPrice(price);
                        productService.update(product);
                        productsPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateProductButton, BorderLayout.SOUTH);
                break;

            case "Receivers":
                JTextField emailField = new JTextField();
                JTextField passwordField = new JTextField();
                JTextField receiverNameField = new JTextField();
                JTextField surnameField = new JTextField();
                JTextField addressField = new JTextField();
                formPanel.add(new JLabel("Email:"));
                formPanel.add(emailField);
                formPanel.add(new JLabel("Password:"));
                formPanel.add(passwordField);
                formPanel.add(new JLabel("Name:"));
                formPanel.add(receiverNameField);
                formPanel.add(new JLabel("Surname:"));
                formPanel.add(surnameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(addressField);

                JButton updateReceiverButton = new JButton("Update");
                updateReceiverButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    String email = emailField.getText();
                    String password = passwordField.getText();
                    String name = receiverNameField.getText();
                    String surname = surnameField.getText();
                    String address = addressField.getText();
                    Receiver receiver = receiverService.getReceiverById(id);
                    if (receiver != null) {
                        receiver.setEmail(email);
                        receiver.setPassword(password);
                        receiver.setName(name);
                        receiver.setSurname(surname);
                        receiver.setAddress(address);
                        receiverService.update(receiver);
                        receiversPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Receiver not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateReceiverButton, BorderLayout.SOUTH);
                break;

            case "Cargos":
                JComboBox<Order> orderComboBox = new JComboBox<>(orderService.getAllOrders().toArray(new Order[0]));
                orderComboBox.setSelectedItem(null);
                JCheckBox isDeliveredCheckBox = new JCheckBox();
                formPanel.add(new JLabel("Order:"));
                formPanel.add(orderComboBox);
                formPanel.add(new JLabel("Is Delivered:"));
                formPanel.add(isDeliveredCheckBox);

                JButton updateCargoButton = new JButton("Update");
                updateCargoButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    Order order = (Order) orderComboBox.getSelectedItem();
                    boolean isDelivered = isDeliveredCheckBox.isSelected();
                    Cargo cargo = cargoService.getCargoById(id);
                    if (cargo != null && order != null) {
                        cargo.setEntity(order);
                        cargo.setDelivered(isDelivered);
                        cargoService.update(cargo);
                        cargosPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid ID or Order", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateCargoButton, BorderLayout.SOUTH);
                break;

            case "Stores":
                JTextField storeNameField = new JTextField();
                JTextField storeAddressField = new JTextField();
                JTextField storePhoneField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(storeNameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(storeAddressField);
                formPanel.add(new JLabel("Phone:"));
                formPanel.add(storePhoneField);

                JButton updateStoreButton = new JButton("Update");
                updateStoreButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    String name = storeNameField.getText();
                    String address = storeAddressField.getText();
                    String phone = storePhoneField.getText();
                    Store store = storeService.getStoreById(id);
                    if (store != null) {
                        store.setName(name);
                        store.setAddress(address);
                        store.setPhone(phone);
                        storeService.update(store);
                        storesPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Store not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateStoreButton, BorderLayout.SOUTH);
                break;
        }

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showDeleteDialog() {
        JDialog dialog = new JDialog((Frame) null, "Delete", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField idField = new JTextField();
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            switch (currentPanel) {
                case "Orders":
                    orderService.delete(id);
                    ordersPanel.refresh();
                    break;
                case "Products":
                    productService.delete(id);
                    productsPanel.refresh();
                    break;
                case "Receivers":
                    receiverService.delete(id);
                    receiversPanel.refresh();
                    break;
                case "Cargos":
                    cargoService.delete(id);
                    cargosPanel.refresh();
                    break;
                case "Stores":
                    storeService.delete(id);
                    storesPanel.refresh();
                    break;
            }
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(deleteButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

}
*/
/*
package org.project.pages.home;

import org.project.panels.*;
import org.project.data.JsonRepository;
import org.project.models.*;
import org.project.services.*;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {
    private OrderService orderService;
    private CargoService cargoService;
    private ProductService productService;
    private ReceiverService receiverService;
    private StoreService storeService;

    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private String currentPanel;

    private OrdersPanel ordersPanel;
    private CargosPanel cargosPanel;
    private ProductsPanel productsPanel;
    private ReceiversPanel receiversPanel;
    private StoresPanel storesPanel;

    public HomePage(Frame frame, CardLayout cardLayout, JPanel contentPanel) {
        orderService = new OrderService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json", Order[].class));
        cargoService = new CargoService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json", Cargo[].class));
        productService = new ProductService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/products.json", Product[].class));
        receiverService = new ReceiverService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json", Receiver[].class));
        storeService = new StoreService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json", Store[].class));

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        add(buttonPanel, BorderLayout.WEST);

        JButton ordersButton = new JButton("Orders");
        JButton cargosButton = new JButton("Cargos");
        JButton productsButton = new JButton("Products");
        JButton receiversButton = new JButton("Receivers");
        JButton storesButton = new JButton("Stores");

        buttonPanel.add(ordersButton);
        buttonPanel.add(cargosButton);
        buttonPanel.add(productsButton);
        buttonPanel.add(receiversButton);
        buttonPanel.add(storesButton);

        cardsPanel = new JPanel(new CardLayout());
        add(new JScrollPane(cardsPanel), BorderLayout.CENTER);

        ordersPanel = new OrdersPanel(orderService, receiverService, productService);
        cargosPanel = new CargosPanel(cargoService);
        productsPanel = new ProductsPanel(productService);
        receiversPanel = new ReceiversPanel(receiverService, orderService, cargoService);
        storesPanel = new StoresPanel(storeService);

        cardsPanel.add(new JScrollPane(ordersPanel), "Orders");
        cardsPanel.add(new JScrollPane(cargosPanel), "Cargos");
        cardsPanel.add(new JScrollPane(productsPanel), "Products");
        cardsPanel.add(new JScrollPane(receiversPanel), "Receivers");
        cardsPanel.add(new JScrollPane(storesPanel), "Stores");

        this.cardLayout = (CardLayout) cardsPanel.getLayout();

        ordersButton.addActionListener(e -> showPanel("Orders"));
        cargosButton.addActionListener(e -> showPanel("Cargos"));
        productsButton.addActionListener(e -> showPanel("Products"));
        receiversButton.addActionListener(e -> showPanel("Receivers"));
        storesButton.addActionListener(e -> showPanel("Stores"));

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        add(actionPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        addButton.addActionListener(e -> showAddDialog());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> showDeleteDialog());
    }

    private void showPanel(String panelName) {
        currentPanel = panelName;
        cardLayout.show(cardsPanel, panelName);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        switch (currentPanel) {
            case "Orders":
                JComboBox<Receiver> receiverComboBox = new JComboBox<>(receiverService.getAllReceivers().toArray(new Receiver[0]));
                JComboBox<Product> productComboBox = new JComboBox<>(productService.getAllProducts().toArray(new Product[0]));
                formPanel.add(new JLabel("Receiver:"));
                formPanel.add(receiverComboBox);
                formPanel.add(new JLabel("Product:"));
                formPanel.add(productComboBox);

                JButton addOrderButton = new JButton("Add");
                addOrderButton.addActionListener(e -> {
                    Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
                    Product product = (Product) productComboBox.getSelectedItem();
                    if (receiver != null && product != null) {
                        Order order = new Order(receiver, product);
                        orderService.add(order);
                        ordersPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid Receiver or Product", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(addOrderButton, BorderLayout.SOUTH);
                break;

            case "Products":
                JTextField nameField = new JTextField();
                JTextField descriptionField = new JTextField();
                JTextField sellerIdField = new JTextField();
                JTextField imageUrlField = new JTextField();
                JTextField priceField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(nameField);
                formPanel.add(new JLabel("Description:"));
                formPanel.add(descriptionField);
                formPanel.add(new JLabel("Seller ID:"));
                formPanel.add(sellerIdField);
                formPanel.add(new JLabel("Image URL:"));
                formPanel.add(imageUrlField);
                formPanel.add(new JLabel("Price:"));
                formPanel.add(priceField);

                JButton addProductButton = new JButton("Add");
                addProductButton.addActionListener(e -> {
                    String name = nameField.getText();
                    String description = descriptionField.getText();
                    String sellerId = sellerIdField.getText();
                    String imageUrl = imageUrlField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    Product product = new Product(name, description, sellerId, imageUrl, price);
                    productService.add(product);
                    productsPanel.refresh();
                    dialog.dispose();
                });
                dialog.add(addProductButton, BorderLayout.SOUTH);
                break;

            case "Receivers":
                JTextField emailField = new JTextField();
                JTextField passwordField = new JTextField();
                JTextField receiverNameField = new JTextField();
                JTextField surnameField = new JTextField();
                JTextField addressField = new JTextField();
                formPanel.add(new JLabel("Email:"));
                formPanel.add(emailField);
                formPanel.add(new JLabel("Password:"));
                formPanel.add(passwordField);
                formPanel.add(new JLabel("Name:"));
                formPanel.add(receiverNameField);
                formPanel.add(new JLabel("Surname:"));
                formPanel.add(surnameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(addressField);

                JButton addReceiverButton = new JButton("Add");
                addReceiverButton.addActionListener(e -> {
                    String email = emailField.getText();
                    String password = passwordField.getText();
                    String name = receiverNameField.getText();
                    String surname = surnameField.getText();
                    String address = addressField.getText();
                    Receiver receiver = new Receiver(email, password, name, surname, address);
                    receiverService.add(receiver);
                    receiversPanel.refresh();
                    dialog.dispose();
                });
                dialog.add(addReceiverButton, BorderLayout.SOUTH);
                break;

            case "Cargos":
                JComboBox<Order> orderComboBox = new JComboBox<>(orderService.getAllOrders().toArray(new Order[0]));
                JCheckBox isDeliveredCheckBox = new JCheckBox();
                formPanel.add(new JLabel("Order:"));
                formPanel.add(orderComboBox);
                formPanel.add(new JLabel("Is Delivered:"));
                formPanel.add(isDeliveredCheckBox);

                JButton addCargoButton = new JButton("Add");
                addCargoButton.addActionListener(e -> {
                    Order order = (Order) orderComboBox.getSelectedItem();
                    boolean isDelivered = isDeliveredCheckBox.isSelected();
                    if (order != null) {
                        Cargo cargo = new Cargo(isDelivered, order);
                        cargoService.add(cargo);
                        cargosPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid Order", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(addCargoButton, BorderLayout.SOUTH);
                break;

            case "Stores":
                JTextField storeNameField = new JTextField();
                JTextField storeAddressField = new JTextField();
                JTextField storePhoneField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(storeNameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(storeAddressField);
                formPanel.add(new JLabel("Phone:"));
                formPanel.add(storePhoneField);

                JButton addStoreButton = new JButton("Add");
                addStoreButton.addActionListener(e -> {
                    String name = storeNameField.getText();
                    String address = storeAddressField.getText();
                    String phone = storePhoneField.getText();
                    Store store = new Store(name, address, phone);
                    storeService.add(store);
                    storesPanel.refresh();
                    dialog.dispose();
                });
                dialog.add(addStoreButton, BorderLayout.SOUTH);
                break;
        }

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showUpdateDialog() {
        JDialog dialog = new JDialog((Frame) null, "Update", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField idField = new JTextField();
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);

        switch (currentPanel) {
            case "Orders":
                JComboBox<Receiver> receiverComboBox = new JComboBox<>(receiverService.getAllReceivers().toArray(new Receiver[0]));
                JComboBox<Product> productComboBox = new JComboBox<>(productService.getAllProducts().toArray(new Product[0]));
                formPanel.add(new JLabel("Receiver:"));
                formPanel.add(receiverComboBox);
                formPanel.add(new JLabel("Product:"));
                formPanel.add(productComboBox);

                JButton updateOrderButton = new JButton("Update");
                updateOrderButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    Receiver receiver = (Receiver) receiverComboBox.getSelectedItem();
                    Product product = (Product) productComboBox.getSelectedItem();
                    Order order = orderService.getOrderById(id);
                    if (order != null && receiver != null && product != null) {
                        order.setEntity1(receiver);
                        order.setEntity2(product);
                        orderService.update(order);
                        ordersPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid ID, Receiver or Product", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateOrderButton, BorderLayout.SOUTH);
                break;

            case "Products":
                JTextField nameField = new JTextField();
                JTextField descriptionField = new JTextField();
                JTextField sellerIdField = new JTextField();
                JTextField imageUrlField = new JTextField();
                JTextField priceField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(nameField);
                formPanel.add(new JLabel("Description:"));
                formPanel.add(descriptionField);
                formPanel.add(new JLabel("Seller ID:"));
                formPanel.add(sellerIdField);
                formPanel.add(new JLabel("Image URL:"));
                formPanel.add(imageUrlField);
                formPanel.add(new JLabel("Price:"));
                formPanel.add(priceField);

                JButton updateProductButton = new JButton("Update");
                updateProductButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    String description = descriptionField.getText();
                    String sellerId = sellerIdField.getText();
                    String imageUrl = imageUrlField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    Product product = productService.getProductById(id);
                    if (product != null) {
                        product.setName(name);
                        product.setDescription(description);
                        product.setSellerId(sellerId);
                        product.setImageUrl(imageUrl);
                        product.setPrice(price);
                        productService.update(product);
                        productsPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateProductButton, BorderLayout.SOUTH);
                break;

            case "Receivers":
                JTextField emailField = new JTextField();
                JTextField passwordField = new JTextField();
                JTextField receiverNameField = new JTextField();
                JTextField surnameField = new JTextField();
                JTextField addressField = new JTextField();
                formPanel.add(new JLabel("Email:"));
                formPanel.add(emailField);
                formPanel.add(new JLabel("Password:"));
                formPanel.add(passwordField);
                formPanel.add(new JLabel("Name:"));
                formPanel.add(receiverNameField);
                formPanel.add(new JLabel("Surname:"));
                formPanel.add(surnameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(addressField);

                JButton updateReceiverButton = new JButton("Update");
                updateReceiverButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    String email = emailField.getText();
                    String password = passwordField.getText();
                    String name = receiverNameField.getText();
                    String surname = surnameField.getText();
                    String address = addressField.getText();
                    Receiver receiver = receiverService.getReceiverById(id);
                    if (receiver != null) {
                        receiver.setEmail(email);
                        receiver.setPassword(password);
                        receiver.setName(name);
                        receiver.setSurname(surname);
                        receiver.setAddress(address);
                        receiverService.update(receiver);
                        receiversPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Receiver not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateReceiverButton, BorderLayout.SOUTH);
                break;

            case "Cargos":
                JTextField orderIdField = new JTextField();
                JCheckBox isDeliveredCheckBox = new JCheckBox();
                formPanel.add(new JLabel("Order ID:"));
                formPanel.add(orderIdField);
                formPanel.add(new JLabel("Is Delivered:"));
                formPanel.add(isDeliveredCheckBox);

                JButton updateCargoButton = new JButton("Update");
                updateCargoButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    int orderId = Integer.parseInt(orderIdField.getText());
                    boolean isDelivered = isDeliveredCheckBox.isSelected();
                    Cargo cargo = cargoService.getCargoById(id);
                    Order order = orderService.getOrderById(orderId);
                    if (cargo != null && order != null) {
                        cargo.setEntity(order);
                        cargo.setDelivered(isDelivered);
                        cargoService.update(cargo);
                        cargosPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid ID or Order ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateCargoButton, BorderLayout.SOUTH);
                break;

            case "Stores":
                JTextField storeNameField = new JTextField();
                JTextField storeAddressField = new JTextField();
                JTextField storePhoneField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(storeNameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(storeAddressField);
                formPanel.add(new JLabel("Phone:"));
                formPanel.add(storePhoneField);

                JButton updateStoreButton = new JButton("Update");
                updateStoreButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    String name = storeNameField.getText();
                    String address = storeAddressField.getText();
                    String phone = storePhoneField.getText();
                    Store store = storeService.getStoreById(id);
                    if (store != null) {
                        store.setName(name);
                        store.setAddress(address);
                        store.setPhone(phone);
                        storeService.update(store);
                        storesPanel.refresh();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Store not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(updateStoreButton, BorderLayout.SOUTH);
                break;
        }

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showDeleteDialog() {
        JDialog dialog = new JDialog((Frame) null, "Delete", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField idField = new JTextField();
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            switch (currentPanel) {
                case "Orders":
                    orderService.delete(id);
                    ordersPanel.refresh();
                    break;
                case "Products":
                    productService.delete(id);
                    productsPanel.refresh();
                    break;
                case "Receivers":
                    receiverService.delete(id);
                    receiversPanel.refresh();
                    break;
                case "Cargos":
                    cargoService.delete(id);
                    cargosPanel.refresh();
                    break;
                case "Stores":
                    storeService.delete(id);
                    storesPanel.refresh();
                    break;
            }
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(deleteButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
*/
/*---------düzeltmeden önce
*/
/*-----------sekizinci hali-----------------
package org.project.pages.home;

import org.project.components.*;
import org.project.data.JsonRepository;
import org.project.services.*;
import org.project.models.*;
import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {
    private OrderService orderService;
    private CargoService cargoService;
    private ProductService productService;
    private ReceiverService receiverService;
    private StoreService storeService;

    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private String currentPanel;

    public HomePage(Frame frame, CardLayout cardLayout, JPanel contentPanel) {
        orderService = new OrderService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json", Order[].class));
        cargoService = new CargoService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json", Cargo[].class));
        productService = new ProductService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/products.json", Product[].class));
        receiverService = new ReceiverService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json", Receiver[].class));
        storeService = new StoreService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json", Store[].class));

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        add(buttonPanel, BorderLayout.WEST);

        JButton ordersButton = new JButton("Orders");
        JButton cargosButton = new JButton("Cargos");
        JButton productsButton = new JButton("Products");
        JButton receiversButton = new JButton("Receivers");
        JButton storesButton = new JButton("Stores");

        buttonPanel.add(ordersButton);
        buttonPanel.add(cargosButton);
        buttonPanel.add(productsButton);
        buttonPanel.add(receiversButton);
        buttonPanel.add(storesButton);

        cardsPanel = new JPanel(new CardLayout());
        add(cardsPanel, BorderLayout.CENTER);

        OrdersPanel ordersPanel = new OrdersPanel(orderService);
        CargosPanel cargosPanel = new CargosPanel(cargoService);
        ProductsPanel productsPanel = new ProductsPanel(productService);
        ReceiversPanel receiversPanel = new ReceiversPanel(receiverService);
        StoresPanel storesPanel = new StoresPanel(storeService);

        cardsPanel.add(new JScrollPane(ordersPanel), "Orders");
        cardsPanel.add(new JScrollPane(cargosPanel), "Cargos");
        cardsPanel.add(new JScrollPane(productsPanel), "Products");
        cardsPanel.add(new JScrollPane(receiversPanel), "Receivers");
        cardsPanel.add(new JScrollPane(storesPanel), "Stores");

        this.cardLayout = (CardLayout) cardsPanel.getLayout();

        ordersButton.addActionListener(e -> showPanel("Orders"));
        cargosButton.addActionListener(e -> showPanel("Cargos"));
        productsButton.addActionListener(e -> showPanel("Products"));
        receiversButton.addActionListener(e -> showPanel("Receivers"));
        storesButton.addActionListener(e -> showPanel("Stores"));

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        add(actionPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        addButton.addActionListener(e -> showAddDialog());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> showDeleteDialog());
    }

    private void showPanel(String panelName) {
        currentPanel = panelName;
        cardLayout.show(cardsPanel, panelName);
    }

    private void showAddDialog() {
        // Implement add dialog logic based on currentPanel
    }

    private void showUpdateDialog() {
        // Implement update dialog logic based on currentPanel
    }

    private void showDeleteDialog() {
        // Implement delete dialog logic based on currentPanel
    }
}*/
/*---------------yedinci hali--------------------------
package org.project.pages.home;

import org.json.JSONArray;
import org.json.JSONObject;
import org.project.data.JsonRepository;
import org.project.models.*;
import org.project.services.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends JPanel {
    private List<Receiver> receivers;
    private List<Product> products;
    private List<Order<Receiver, Product>> orders;
    private List<Cargo<Order<Receiver, Product>>> cargos;
    private List<Store> stores;

    private OrderService orderService;
    private CargoService cargoService;
    private ProductService productService;
    private ReceiverService receiverService;
    private StoreService storeService;

    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private String currentPanel;

    public HomePage(Frame frame, CardLayout cardLayout, JPanel contentPanel) {
        receivers = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();
        cargos = new ArrayList<>();
        stores = new ArrayList<>();

        orderService = new OrderService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json", Order[].class));
        cargoService = new CargoService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json", Cargo[].class));
        productService = new ProductService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/products.json", Product[].class));
        receiverService = new ReceiverService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json", Receiver[].class));
        storeService = new StoreService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json", Store[].class));

        loadReceivers();
        loadProducts();
        loadOrders();
        loadCargos();
        loadStores();

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        add(buttonPanel, BorderLayout.WEST);

        JButton ordersButton = new JButton("Orders");
        JButton cargosButton = new JButton("Cargos");
        JButton productsButton = new JButton("Products");
        JButton receiversButton = new JButton("Receivers");
        JButton storesButton = new JButton("Stores");

        buttonPanel.add(ordersButton);
        buttonPanel.add(cargosButton);
        buttonPanel.add(productsButton);
        buttonPanel.add(receiversButton);
        buttonPanel.add(storesButton);

        cardsPanel = new JPanel(new CardLayout());
        add(cardsPanel, BorderLayout.CENTER);

        JPanel ordersPanel = createCardsPanel(orders, "Order");
        JPanel cargosPanel = createCardsPanel(cargos, "Cargo");
        JPanel productsPanel = createCardsPanel(products, "Product");
        JPanel receiversPanel = createCardsPanel(receivers, "Receiver");
        JPanel storesPanel = createCardsPanel(stores, "Store");

        cardsPanel.add(new JScrollPane(ordersPanel), "Orders");
        cardsPanel.add(new JScrollPane(cargosPanel), "Cargos");
        cardsPanel.add(new JScrollPane(productsPanel), "Products");
        cardsPanel.add(new JScrollPane(receiversPanel), "Receivers");
        cardsPanel.add(new JScrollPane(storesPanel), "Stores");

        this.cardLayout = (CardLayout) cardsPanel.getLayout();

        ordersButton.addActionListener(e -> showPanel("Orders"));
        cargosButton.addActionListener(e -> showPanel("Cargos"));
        productsButton.addActionListener(e -> showPanel("Products"));
        receiversButton.addActionListener(e -> showPanel("Receivers"));
        storesButton.addActionListener(e -> showPanel("Stores"));

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        add(actionPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        addButton.addActionListener(e -> showAddDialog());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> showDeleteDialog());
    }

    private JPanel createCardsPanel(List<?> items, String title) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        for (Object item : items) {
            addCard(panel, title, item.toString(), item);
        }
        return panel;
    }

    private void addCard(JPanel panel, String title, String content, Object data) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel(title);
        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(title, content, data));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        panel.add(card);
    }

    private void showDetailsDialog(String title, String content, Object data) {
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        if (data instanceof Receiver) {
            JPanel ordersPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            List<Order<Receiver, Product>> receiverOrders = getOrdersByReceiver((Receiver) data);
            for (Order<Receiver, Product> order : receiverOrders) {
                addCard(ordersPanel, "Order", order.toString(), order);
            }
            dialog.add(new JScrollPane(ordersPanel), BorderLayout.SOUTH);
        }

        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        switch (currentPanel) {
            case "Orders":
                JTextField receiverIdField = new JTextField();
                JTextField productIdField = new JTextField();
                formPanel.add(new JLabel("Receiver ID:"));
                formPanel.add(receiverIdField);
                formPanel.add(new JLabel("Product ID:"));
                formPanel.add(productIdField);

                JButton addOrderButton = new JButton("Add");
                addOrderButton.addActionListener(e -> {
                    int receiverId = Integer.parseInt(receiverIdField.getText());
                    int productId = Integer.parseInt(productIdField.getText());
                    Receiver receiver = receiverService.getReceiverById(receiverId);
                    Product product = productService.getProductById(productId);
                    if (receiver != null && product != null) {
                        Order<Receiver, Product> order = new Order<>(receiver, product);
                        orderService.add(order);
                        orders.add(order);
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid Receiver ID or Product ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(addOrderButton, BorderLayout.SOUTH);
                break;

            case "Products":
                JTextField nameField = new JTextField();
                JTextField descriptionField = new JTextField();
                JTextField sellerIdField = new JTextField();
                JTextField imageUrlField = new JTextField();
                JTextField priceField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(nameField);
                formPanel.add(new JLabel("Description:"));
                formPanel.add(descriptionField);
                formPanel.add(new JLabel("Seller ID:"));
                formPanel.add(sellerIdField);
                formPanel.add(new JLabel("Image URL:"));
                formPanel.add(imageUrlField);
                formPanel.add(new JLabel("Price:"));
                formPanel.add(priceField);

                JButton addProductButton = new JButton("Add");
                addProductButton.addActionListener(e -> {
                    String name = nameField.getText();
                    String description = descriptionField.getText();
                    String sellerId = sellerIdField.getText();
                    String imageUrl = imageUrlField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    Product product = new Product(name, description, sellerId, imageUrl, price);
                    productService.add(product);
                    products.add(product);
                    dialog.dispose();
                });
                dialog.add(addProductButton, BorderLayout.SOUTH);
                break;

            case "Receivers":
                JTextField emailField = new JTextField();
                JTextField passwordField = new JTextField();
                JTextField receiverNameField = new JTextField();
                JTextField surnameField = new JTextField();
                JTextField addressField = new JTextField();
                formPanel.add(new JLabel("Email:"));
                formPanel.add(emailField);
                formPanel.add(new JLabel("Password:"));
                formPanel.add(passwordField);
                formPanel.add(new JLabel("Name:"));
                formPanel.add(receiverNameField);
                formPanel.add(new JLabel("Surname:"));
                formPanel.add(surnameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(addressField);

                JButton addReceiverButton = new JButton("Add");
                addReceiverButton.addActionListener(e -> {
                    String email = emailField.getText();
                    String password = passwordField.getText();
                    String name = receiverNameField.getText();
                    String surname = surnameField.getText();
                    String address = addressField.getText();
                    Receiver receiver = new Receiver(email, password, name, surname, address);
                    receiverService.add(receiver);
                    receivers.add(receiver);
                    dialog.dispose();
                });
                dialog.add(addReceiverButton, BorderLayout.SOUTH);
                break;

            case "Cargos":
                JTextField orderIdField = new JTextField();
                JCheckBox isDeliveredCheckBox = new JCheckBox();
                formPanel.add(new JLabel("Order ID:"));
                formPanel.add(orderIdField);
                formPanel.add(new JLabel("Is Delivered:"));
                formPanel.add(isDeliveredCheckBox);

                JButton addCargoButton = new JButton("Add");
                addCargoButton.addActionListener(e -> {
                    int orderId = Integer.parseInt(orderIdField.getText());
                    boolean isDelivered = isDeliveredCheckBox.isSelected();
                    Order<Receiver, Product> order = orderService.getOrderById(orderId);
                    if (order != null) {
                        Cargo<Order<Receiver, Product>> cargo = new Cargo<>(isDelivered, order);
                        cargoService.add(cargo);
                        cargos.add(cargo);
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid Order ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(addCargoButton, BorderLayout.SOUTH);
                break;

            case "Stores":
                JTextField storeNameField = new JTextField();
                JTextField storeAddressField = new JTextField();
                JTextField storePhoneField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(storeNameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(storeAddressField);
                formPanel.add(new JLabel("Phone:"));
                formPanel.add(storePhoneField);

                JButton addStoreButton = new JButton("Add");
                addStoreButton.addActionListener(e -> {
                    String name = storeNameField.getText();
                    String address = storeAddressField.getText();
                    String phone = storePhoneField.getText();
                    Store store = new Store(name, address, phone);
                    storeService.add(store);
                    stores.add(store);
                    dialog.dispose();
                });
                dialog.add(addStoreButton, BorderLayout.SOUTH);
                break;
        }

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showUpdateDialog() {
        // Implement update dialog logic similar to showAddDialog
    }

    private void showDeleteDialog() {
        // Implement delete dialog logic similar to showAddDialog
    }

    private List<Order<Receiver, Product>> getOrdersByReceiver(Receiver receiver) {
        List<Order<Receiver, Product>> receiverOrders = new ArrayList<>();
        for (Order<Receiver, Product> order : orders) {
            if (order.getEntity1().equals(receiver)) {
                receiverOrders.add(order);
            }
        }
        return receiverOrders;
    }

    private void showPanel(String panelName) {
        currentPanel = panelName;
        cardLayout.show(cardsPanel, panelName);
    }

    private void loadReceivers() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Receiver receiver = new Receiver(
                        jsonObject.getString("email"),
                        jsonObject.getString("password"),
                        jsonObject.getString("name"),
                        jsonObject.getString("surname"),
                        jsonObject.getString("address")
                );
                receiver.setId(jsonObject.getInt("id"));
                receivers.add(receiver);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/products.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Product product = new Product(
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        jsonObject.getString("sellerId"),
                        jsonObject.getString("imageUrl"),
                        jsonObject.getDouble("price")
                );
                product.setId(jsonObject.getInt("id"));
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOrders() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entity1Object = jsonObject.getJSONObject("entity1");
                JSONObject entity2Object = jsonObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId(jsonObject.getInt("id"));
                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCargos() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entityObject = jsonObject.getJSONObject("entity");
                JSONObject entity1Object = entityObject.getJSONObject("entity1");
                JSONObject entity2Object = entityObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId((int) entityObject.getDouble("id"));

                Cargo<Order<Receiver, Product>> cargo = new Cargo<>(jsonObject.getBoolean("isdelivered"), order);
                cargo.setid(jsonObject.getInt("id"));
                cargos.add(cargo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStores() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json")));
            if (content.isEmpty()) {
                System.out.println("Stores JSON is empty.");
                return;
            }
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Store store = new Store(
                        jsonObject.getString("name"),
                        jsonObject.getString("address"),
                        jsonObject.getString("phone")
                );
                stores.add(store);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
/*--------------altıncı hali-------------------------
package org.project.pages.home;

import org.json.JSONArray;
import org.json.JSONObject;
import org.project.data.JsonRepository;
import org.project.models.*;
import org.project.services.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends JPanel {
    private List<Receiver> receivers;
    private List<Product> products;
    private List<Order<Receiver, Product>> orders;
    private List<Cargo<Order<Receiver, Product>>> cargos;
    private List<Store> stores;

    private OrderService orderService;
    private CargoService cargoService;
    private ProductService productService;
    private ReceiverService receiverService;
    private StoreService storeService;

    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private String currentPanel;

    public HomePage(Frame frame, CardLayout cardLayout, JPanel contentPanel) {
        receivers = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();
        cargos = new ArrayList<>();
        stores = new ArrayList<>();

        orderService = new OrderService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json", Order[].class));
        cargoService = new CargoService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json", Cargo[].class));
        productService = new ProductService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/products.json", Product[].class));
        receiverService = new ReceiverService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json", Receiver[].class));
        storeService = new StoreService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json", Store[].class));

        loadReceivers();
        loadProducts();
        loadOrders();
        loadCargos();
        loadStores();

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        add(buttonPanel, BorderLayout.WEST);

        JButton ordersButton = new JButton("Orders");
        JButton cargosButton = new JButton("Cargos");
        JButton productsButton = new JButton("Products");
        JButton receiversButton = new JButton("Receivers");
        JButton storesButton = new JButton("Stores");

        buttonPanel.add(ordersButton);
        buttonPanel.add(cargosButton);
        buttonPanel.add(productsButton);
        buttonPanel.add(receiversButton);
        buttonPanel.add(storesButton);

        cardsPanel = new JPanel(new CardLayout());
        add(cardsPanel, BorderLayout.CENTER);

        JPanel ordersPanel = createCardsPanel(orders, "Order");
        JPanel cargosPanel = createCardsPanel(cargos, "Cargo");
        JPanel productsPanel = createCardsPanel(products, "Product");
        JPanel receiversPanel = createCardsPanel(receivers, "Receiver");
        JPanel storesPanel = createCardsPanel(stores, "Store");

        cardsPanel.add(new JScrollPane(ordersPanel), "Orders");
        cardsPanel.add(new JScrollPane(cargosPanel), "Cargos");
        cardsPanel.add(new JScrollPane(productsPanel), "Products");
        cardsPanel.add(new JScrollPane(receiversPanel), "Receivers");
        cardsPanel.add(new JScrollPane(storesPanel), "Stores");

        this.cardLayout = (CardLayout) cardsPanel.getLayout();

        ordersButton.addActionListener(e -> showPanel("Orders"));
        cargosButton.addActionListener(e -> showPanel("Cargos"));
        productsButton.addActionListener(e -> showPanel("Products"));
        receiversButton.addActionListener(e -> showPanel("Receivers"));
        storesButton.addActionListener(e -> showPanel("Stores"));

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        add(actionPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        addButton.addActionListener(e -> showAddDialog());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> showDeleteDialog());
    }

    private JPanel createCardsPanel(List<?> items, String title) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        for (Object item : items) {
            addCard(panel, title, item.toString(), item);
        }
        return panel;
    }

    private void addCard(JPanel panel, String title, String content, Object data) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel(title);
        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(title, content, data));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        panel.add(card);
    }

    private void showDetailsDialog(String title, String content, Object data) {
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        if (data instanceof Receiver) {
            JPanel ordersPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            List<Order<Receiver, Product>> receiverOrders = getOrdersByReceiver((Receiver) data);
            for (Order<Receiver, Product> order : receiverOrders) {
                addCard(ordersPanel, "Order", order.toString(), order);
            }
            dialog.add(new JScrollPane(ordersPanel), BorderLayout.SOUTH);
        }

        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        switch (currentPanel) {
            case "Orders":
                JTextField receiverIdField = new JTextField();
                JTextField productIdField = new JTextField();
                formPanel.add(new JLabel("Receiver ID:"));
                formPanel.add(receiverIdField);
                formPanel.add(new JLabel("Product ID:"));
                formPanel.add(productIdField);

                JButton addOrderButton = new JButton("Add");
                addOrderButton.addActionListener(e -> {
                    int receiverId = Integer.parseInt(receiverIdField.getText());
                    int productId = Integer.parseInt(productIdField.getText());
                    Receiver receiver = receiverService.getReceiverById(receiverId);
                    Product product = productService.getProductById(productId);
                    if (receiver != null && product != null) {
                        Order<Receiver, Product> order = new Order<>(receiver, product);
                        orderService.add(order);
                        orders.add(order);
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid Receiver ID or Product ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(addOrderButton, BorderLayout.SOUTH);
                break;

            case "Products":
                JTextField nameField = new JTextField();
                JTextField descriptionField = new JTextField();
                JTextField sellerIdField = new JTextField();
                JTextField imageUrlField = new JTextField();
                JTextField priceField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(nameField);
                formPanel.add(new JLabel("Description:"));
                formPanel.add(descriptionField);
                formPanel.add(new JLabel("Seller ID:"));
                formPanel.add(sellerIdField);
                formPanel.add(new JLabel("Image URL:"));
                formPanel.add(imageUrlField);
                formPanel.add(new JLabel("Price:"));
                formPanel.add(priceField);

                JButton addProductButton = new JButton("Add");
                addProductButton.addActionListener(e -> {
                    String name = nameField.getText();
                    String description = descriptionField.getText();
                    String sellerId = sellerIdField.getText();
                    String imageUrl = imageUrlField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    Product product = new Product(name, description, sellerId, imageUrl, price);
                    productService.add(product);
                    products.add(product);
                    dialog.dispose();
                });
                dialog.add(addProductButton, BorderLayout.SOUTH);
                break;

            case "Receivers":
                JTextField emailField = new JTextField();
                JTextField passwordField = new JTextField();
                JTextField receiverNameField = new JTextField();
                JTextField surnameField = new JTextField();
                JTextField addressField = new JTextField();
                formPanel.add(new JLabel("Email:"));
                formPanel.add(emailField);
                formPanel.add(new JLabel("Password:"));
                formPanel.add(passwordField);
                formPanel.add(new JLabel("Name:"));
                formPanel.add(receiverNameField);
                formPanel.add(new JLabel("Surname:"));
                formPanel.add(surnameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(addressField);

                JButton addReceiverButton = new JButton("Add");
                addReceiverButton.addActionListener(e -> {
                    String email = emailField.getText();
                    String password = passwordField.getText();
                    String name = receiverNameField.getText();
                    String surname = surnameField.getText();
                    String address = addressField.getText();
                    Receiver receiver = new Receiver(email, password, name, surname, address);
                    receiverService.add(receiver);
                    receivers.add(receiver);
                    dialog.dispose();
                });
                dialog.add(addReceiverButton, BorderLayout.SOUTH);
                break;

            case "Cargos":
                JTextField orderIdField = new JTextField();
                JCheckBox isDeliveredCheckBox = new JCheckBox();
                formPanel.add(new JLabel("Order ID:"));
                formPanel.add(orderIdField);
                formPanel.add(new JLabel("Is Delivered:"));
                formPanel.add(isDeliveredCheckBox);

                JButton addCargoButton = new JButton("Add");
                addCargoButton.addActionListener(e -> {
                    int orderId = Integer.parseInt(orderIdField.getText());
                    boolean isDelivered = isDeliveredCheckBox.isSelected();
                    Order<Receiver, Product> order = orderService.getOrderById(orderId);
                    if (order != null) {
                        Cargo<Order<Receiver, Product>> cargo = new Cargo<>(isDelivered, order);
                        cargoService.add(cargo);
                        cargos.add(cargo);
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid Order ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.add(addCargoButton, BorderLayout.SOUTH);
                break;

            case "Stores":
                JTextField storeNameField = new JTextField();
                JTextField storeAddressField = new JTextField();
                JTextField storePhoneField = new JTextField();
                formPanel.add(new JLabel("Name:"));
                formPanel.add(storeNameField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(storeAddressField);
                formPanel.add(new JLabel("Phone:"));
                formPanel.add(storePhoneField);

                JButton addStoreButton = new JButton("Add");
                addStoreButton.addActionListener(e -> {
                    String name = storeNameField.getText();
                    String address = storeAddressField.getText();
                    String phone = storePhoneField.getText();
                    Store store = new Store(name, address, phone);
                    storeService.add(store);
                    stores.add(store);
                    dialog.dispose();
                });
                dialog.add(addStoreButton, BorderLayout.SOUTH);
                break;
        }

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showUpdateDialog() {
        // Implement update dialog logic similar to showAddDialog
    }

    private void showDeleteDialog() {
        // Implement delete dialog logic similar to showAddDialog
    }

    private List<Order<Receiver, Product>> getOrdersByReceiver(Receiver receiver) {
        List<Order<Receiver, Product>> receiverOrders = new ArrayList<>();
        for (Order<Receiver, Product> order : orders) {
            if (order.getEntity1().equals(receiver)) {
                receiverOrders.add(order);
            }
        }
        return receiverOrders;
    }

    private void showPanel(String panelName) {
        currentPanel = panelName;
        cardLayout.show(cardsPanel, panelName);
    }

    private void loadReceivers() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Receiver receiver = new Receiver(
                        jsonObject.getString("email"),
                        jsonObject.getString("password"),
                        jsonObject.getString("name"),
                        jsonObject.getString("surname"),
                        jsonObject.getString("address")
                );
                receiver.setId(jsonObject.getInt("id"));
                receivers.add(receiver);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/products.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Product product = new Product(
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        jsonObject.getString("sellerId"),
                        jsonObject.getString("imageUrl"),
                        jsonObject.getDouble("price")
                );
                product.setId(jsonObject.getInt("id"));
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOrders() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entity1Object = jsonObject.getJSONObject("entity1");
                JSONObject entity2Object = jsonObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId(jsonObject.getInt("id"));
                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCargos() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entityObject = jsonObject.getJSONObject("entity");
                JSONObject entity1Object = entityObject.getJSONObject("entity1");
                JSONObject entity2Object = entityObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId((int) entityObject.getDouble("id"));

                Cargo<Order<Receiver, Product>> cargo = new Cargo<>(jsonObject.getBoolean("isdelivered"), order);
                cargo.setid(jsonObject.getInt("id"));
                cargos.add(cargo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStores() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json")));
            if (content.isEmpty()) {
                System.out.println("Stores JSON is empty.");
                return;
            }
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Store store = new Store(
                        jsonObject.getString("name"),
                        jsonObject.getString("address"),
                        jsonObject.getString("phone")
                );
                stores.add(store);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
/*----------------------------beşinci hali---------------------
package org.project.pages.home;

import org.json.JSONArray;
import org.json.JSONObject;
import org.project.data.JsonRepository;
import org.project.models.*;
import org.project.services.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends JPanel {
    private List<Receiver> receivers;
    private List<Product> products;
    private List<Order<Receiver, Product>> orders;
    private List<Cargo<Order<Receiver, Product>>> cargos;
    private List<Store> stores;

    private OrderService orderService;
    private CargoService cargoService;
    private ProductService productService;
    private ReceiverService receiverService;
    private StoreService storeService;

    private JPanel cardsPanel;
    private CardLayout cardLayout;

    public HomePage(Frame frame, CardLayout cardLayout, JPanel contentPanel) {
        receivers = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();
        cargos = new ArrayList<>();
        stores = new ArrayList<>();

        orderService = new OrderService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json", Order[].class));
        cargoService = new CargoService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json", Cargo[].class));
        productService = new ProductService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/products.json", Product[].class));
        receiverService = new ReceiverService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json", Receiver[].class));
        storeService = new StoreService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json", Store[].class));

        loadReceivers();
        loadProducts();
        loadOrders();
        loadCargos();
        loadStores();

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        add(buttonPanel, BorderLayout.WEST);

        JButton ordersButton = new JButton("Orders");
        JButton cargosButton = new JButton("Cargos");
        JButton productsButton = new JButton("Products");
        JButton receiversButton = new JButton("Receivers");
        JButton storesButton = new JButton("Stores");

        buttonPanel.add(ordersButton);
        buttonPanel.add(cargosButton);
        buttonPanel.add(productsButton);
        buttonPanel.add(receiversButton);
        buttonPanel.add(storesButton);

        cardsPanel = new JPanel(new CardLayout());
        add(cardsPanel, BorderLayout.CENTER);

        JPanel ordersPanel = createCardsPanel(orders, "Order");
        JPanel cargosPanel = createCardsPanel(cargos, "Cargo");
        JPanel productsPanel = createCardsPanel(products, "Product");
        JPanel receiversPanel = createCardsPanel(receivers, "Receiver");
        JPanel storesPanel = createCardsPanel(stores, "Store");

        cardsPanel.add(new JScrollPane(ordersPanel), "Orders");
        cardsPanel.add(new JScrollPane(cargosPanel), "Cargos");
        cardsPanel.add(new JScrollPane(productsPanel), "Products");
        cardsPanel.add(new JScrollPane(receiversPanel), "Receivers");
        cardsPanel.add(new JScrollPane(storesPanel), "Stores");

        this.cardLayout = (CardLayout) cardsPanel.getLayout();

        ordersButton.addActionListener(e -> showPanel("Orders"));
        cargosButton.addActionListener(e -> showPanel("Cargos"));
        productsButton.addActionListener(e -> showPanel("Products"));
        receiversButton.addActionListener(e -> showPanel("Receivers"));
        storesButton.addActionListener(e -> showPanel("Stores"));

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        add(actionPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        addButton.addActionListener(e -> showAddDialog());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> showDeleteDialog());
    }

    private JPanel createCardsPanel(List<?> items, String title) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        for (Object item : items) {
            addCard(panel, title, item.toString(), item);
        }
        return panel;
    }

    private void addCard(JPanel panel, String title, String content, Object data) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel(title);
        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(title, content, data));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        panel.add(card);
    }

    private void showDetailsDialog(String title, String content, Object data) {
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        if (data instanceof Receiver) {
            JPanel ordersPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            List<Order<Receiver, Product>> receiverOrders = getOrdersByReceiver((Receiver) data);
            for (Order<Receiver, Product> order : receiverOrders) {
                addCard(ordersPanel, "Order", order.toString(), order);
            }
            dialog.add(new JScrollPane(ordersPanel), BorderLayout.SOUTH);
        }

        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField sellerIdField = new JTextField();
        JTextField imageUrlField = new JTextField();
        JTextField priceField = new JTextField();
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Seller ID:"));
        formPanel.add(sellerIdField);
        formPanel.add(new JLabel("Image URL:"));
        formPanel.add(imageUrlField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String description = descriptionField.getText();
            String sellerId = sellerIdField.getText();
            String imageUrl = imageUrlField.getText();
            double price = Double.parseDouble(priceField.getText());
            Product product = new Product(name, description, sellerId, imageUrl, price);
            product.setId(id);
            productService.add(product);
            products.add(product);
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog() {
        JDialog dialog = new JDialog((Frame) null, "Update", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField sellerIdField = new JTextField();
        JTextField imageUrlField = new JTextField();
        JTextField priceField = new JTextField();
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Seller ID:"));
        formPanel.add(sellerIdField);
        formPanel.add(new JLabel("Image URL:"));
        formPanel.add(imageUrlField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String description = descriptionField.getText();
            String sellerId = sellerIdField.getText();
            String imageUrl = imageUrlField.getText();
            double price = Double.parseDouble(priceField.getText());
            Product product = productService.getProductById(id);
            if (product != null) {
                product.setName(name);
                product.setDescription(description);
                product.setSellerId(sellerId);
                product.setImageUrl(imageUrl);
                product.setPrice(price);
                productService.update(product);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDeleteDialog() {
        JDialog dialog = new JDialog((Frame) null, "Delete", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField idField = new JTextField();
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            Product product = productService.getProductById(id);
            if (product != null) {
                productService.delete(id);
                products.remove(product);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(deleteButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private List<Order<Receiver, Product>> getOrdersByReceiver(Receiver receiver) {
        List<Order<Receiver, Product>> receiverOrders = new ArrayList<>();
        for (Order<Receiver, Product> order : orders) {
            if (order.getEntity1().equals(receiver)) {
                receiverOrders.add(order);
            }
        }
        return receiverOrders;
    }

    private void showPanel(String panelName) {
        cardLayout.show(cardsPanel, panelName);
    }

    private void loadReceivers() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Receiver receiver = new Receiver(
                        jsonObject.getString("email"),
                        jsonObject.getString("password"),
                        jsonObject.getString("name"),
                        jsonObject.getString("surname"),
                        jsonObject.getString("address")
                );
                receiver.setId(jsonObject.getInt("id"));
                receivers.add(receiver);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/products.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Product product = new Product(
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        jsonObject.getString("sellerId"),
                        jsonObject.getString("imageUrl"),
                        jsonObject.getDouble("price")
                );
                product.setId(jsonObject.getInt("id"));
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOrders() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entity1Object = jsonObject.getJSONObject("entity1");
                JSONObject entity2Object = jsonObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId(jsonObject.getInt("id"));
                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCargos() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entityObject = jsonObject.getJSONObject("entity");
                JSONObject entity1Object = entityObject.getJSONObject("entity1");
                JSONObject entity2Object = entityObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId((int) entityObject.getDouble("id"));

                Cargo<Order<Receiver, Product>> cargo = new Cargo<>(jsonObject.getBoolean("isdelivered"), order);
                cargo.setid(jsonObject.getInt("id"));
                cargos.add(cargo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStores() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json")));
            if (content.isEmpty()) {
                System.out.println("Stores JSON is empty.");
                return;
            }
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Store store = new Store(
                        jsonObject.getInt("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("address"),
                        jsonObject.getString("phone")
                );
                stores.add(store);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
/*----------------dördüncü hali-------------------------
package org.project.pages.home;

import org.json.JSONArray;
import org.json.JSONObject;
import org.project.data.JsonRepository;
import org.project.models.*;
import org.project.services.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends JPanel {
    private List<Receiver> receivers;
    private List<Product> products;
    private List<Order<Receiver, Product>> orders;
    private List<Cargo<Order<Receiver, Product>>> cargos;
    private List<Store> stores;

    private OrderService orderService;
    private CargoService cargoService;
    private ProductService productService;
    private ReceiverService receiverService;
    private StoreService storeService;

    public HomePage(Frame frame, CardLayout cardLayout, JPanel cardPanel) {
        receivers = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();
        cargos = new ArrayList<>();
        stores = new ArrayList<>();

        orderService = new OrderService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json", Order[].class));
        cargoService = new CargoService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json", Cargo[].class));
        productService = new ProductService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/products.json", Product[].class));
        receiverService = new ReceiverService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json", Receiver[].class));
        storeService = new StoreService(new JsonRepository<>("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json", Store[].class));

        loadReceivers();
        loadProducts();
        loadOrders();
        loadCargos();
        loadStores();

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        add(buttonPanel, BorderLayout.NORTH);

        JButton ordersButton = new JButton("Orders");
        JButton cargosButton = new JButton("Cargos");
        JButton productsButton = new JButton("Products");
        JButton receiversButton = new JButton("Receivers");
        JButton storesButton = new JButton("Stores");

        buttonPanel.add(ordersButton);
        buttonPanel.add(cargosButton);
        buttonPanel.add(productsButton);
        buttonPanel.add(receiversButton);
        buttonPanel.add(storesButton);

        JPanel cardsPanel = new JPanel(new CardLayout());
        add(cardsPanel, BorderLayout.CENTER);

        JPanel ordersPanel = createCardsPanel(orders, "Order");
        JPanel cargosPanel = createCardsPanel(cargos, "Cargo");
        JPanel productsPanel = createCardsPanel(products, "Product");
        JPanel receiversPanel = createCardsPanel(receivers, "Receiver");
        JPanel storesPanel = createCardsPanel(stores, "Store");

        cardsPanel.add(ordersPanel, "Orders");
        cardsPanel.add(cargosPanel, "Cargos");
        cardsPanel.add(productsPanel, "Products");
        cardsPanel.add(receiversPanel, "Receivers");
        cardsPanel.add(storesPanel, "Stores");

        ordersButton.addActionListener(e -> showPanel(cardsPanel, "Orders"));
        cargosButton.addActionListener(e -> showPanel(cardsPanel, "Cargos"));
        productsButton.addActionListener(e -> showPanel(cardsPanel, "Products"));
        receiversButton.addActionListener(e -> showPanel(cardsPanel, "Receivers"));
        storesButton.addActionListener(e -> showPanel(cardsPanel, "Stores"));
    }

    private JPanel createCardsPanel(List<?> items, String title) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        for (Object item : items) {
            addCard(panel, title, item.toString(), item);
        }
        return panel;
    }

    private void addCard(JPanel panel, String title, String content, Object data) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel(title);
        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(title, content, data));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        panel.add(card);
    }

    private void showDetailsDialog(String title, String content, Object data) {
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        addButton.addActionListener(e -> addItem(data));
        updateButton.addActionListener(e -> updateItem(data));
        deleteButton.addActionListener(e -> deleteItem(data));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void addItem(Object data) {
        // Implement add logic using the appropriate service
        if (data instanceof Order) {
            orderService.add((Order<Receiver, Product>) data);
        } else if (data instanceof Cargo) {
            cargoService.add((Cargo<Order<Receiver, Product>>) data);
        } else if (data instanceof Product) {
            productService.add((Product) data);
        } else if (data instanceof Receiver) {
            receiverService.add((Receiver) data);
        } else if (data instanceof Store) {
            storeService.add((Store) data);
        }
    }

    private void updateItem(Object data) {
        // Implement update logic using the appropriate service
        if (data instanceof Order) {
            orderService.update((Order<Receiver, Product>) data);
        } else if (data instanceof Cargo) {
            cargoService.update((Cargo<Order<Receiver, Product>>) data);
        } else if (data instanceof Product) {
            productService.update((Product) data);
        } else if (data instanceof Receiver) {
            receiverService.update((Receiver) data);
        } else if (data instanceof Store) {
            storeService.update((Store) data);
        }
    }

    private void deleteItem(Object data) {
        // Implement delete logic using the appropriate service
        if (data instanceof Order) {
            orderService.delete(((Order<Receiver, Product>) data).getId());
        } else if (data instanceof Cargo) {
            cargoService.delete(((Cargo<Order<Receiver, Product>>) data).getId());
        } else if (data instanceof Product) {
            productService.delete(((Product) data).getId());
        } else if (data instanceof Receiver) {
            receiverService.delete(((Receiver) data).getId());
        } else if (data instanceof Store) {
            storeService.delete(((Store) data).getId());
        }
    }

    private void showPanel(JPanel cardsPanel, String panelName) {
        CardLayout cl = (CardLayout) (cardsPanel.getLayout());
        cl.show(cardsPanel, panelName);
    }

    private void loadReceivers() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Receiver receiver = new Receiver(
                        jsonObject.getString("email"),
                        jsonObject.getString("password"),
                        jsonObject.getString("name"),
                        jsonObject.getString("surname"),
                        jsonObject.getString("address")
                );
                receiver.setId(jsonObject.getInt("id"));
                receivers.add(receiver);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/products.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Product product = new Product(
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        jsonObject.getString("sellerId"),
                        jsonObject.getString("imageUrl"),
                        jsonObject.getDouble("price")
                );
                product.setId(jsonObject.getInt("id"));
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOrders() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entity1Object = jsonObject.getJSONObject("entity1");
                JSONObject entity2Object = jsonObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId(jsonObject.getInt("id"));
                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCargos() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entityObject = jsonObject.getJSONObject("entity");
                JSONObject entity1Object = entityObject.getJSONObject("entity1");
                JSONObject entity2Object = entityObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId((int) entityObject.getDouble("id"));

                Cargo<Order<Receiver, Product>> cargo = new Cargo<>(jsonObject.getBoolean("isdelivered"), order);
                cargo.setid(jsonObject.getInt("id"));
                cargos.add(cargo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStores() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json")));
            if (content.isEmpty()) {
                System.out.println("Stores JSON is empty.");
                return;
            }
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Store store = new Store(
                        jsonObject.getInt("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("address"),
                        jsonObject.getString("phone")
                );
                stores.add(store);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
/*----------üçüncü hali-------------------------------
package org.project.pages.home;

import org.json.JSONArray;
import org.json.JSONObject;
import org.project.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends JPanel {
    private List<Receiver> receivers;
    private List<Product> products;
    private List<Order<Receiver, Product>> orders;
    private List<Cargo<Order<Receiver, Product>>> cargos;
    private List<Store> stores;

    public HomePage(Frame frame, CardLayout cardLayout, JPanel cardPanel) {
        receivers = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();
        cargos = new ArrayList<>();
        stores = new ArrayList<>();

        loadReceivers();
        loadProducts();
        loadOrders();
        loadCargos();
        loadStores();

        setLayout(new BorderLayout());
        JPanel cardsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        add(scrollPane, BorderLayout.CENTER);

        addCards(cardsPanel);
    }

    private void addCards(JPanel cardsPanel) {
        for (Receiver receiver : receivers) {
            addCard(cardsPanel, "Receiver", receiver.toString(), receiver);
        }
        for (Product product : products) {
            addCard(cardsPanel, "Product", product.toString(), product);
        }
        for (Order<Receiver, Product> order : orders) {
            addCard(cardsPanel, "Order", order.toString(), order);
        }
        for (Cargo<Order<Receiver, Product>> cargo : cargos) {
            addCard(cardsPanel, "Cargo", cargo.toString(), cargo);
        }
        for (Store store : stores) {
            addCard(cardsPanel, "Store", store.toString(), store);
        }
    }

    private void addCard(JPanel cardsPanel, String title, String content, Object data) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel(title);
        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDetailsDialog(title, content);
            }
        });

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        cardsPanel.add(card);
    }

    private void showDetailsDialog(String title, String content) {
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea));
        dialog.setVisible(true);
    }

    private void loadReceivers() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Receiver receiver = new Receiver(
                        jsonObject.getString("email"),
                        jsonObject.getString("password"),
                        jsonObject.getString("name"),
                        jsonObject.getString("surname"),
                        jsonObject.getString("address")
                );
                receiver.setId(jsonObject.getInt("id"));
                receivers.add(receiver);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/products.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Product product = new Product(
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        jsonObject.getString("sellerId"),
                        jsonObject.getString("imageUrl"),
                        jsonObject.getDouble("price")
                );
                product.setId(jsonObject.getInt("id"));
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOrders() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entity1Object = jsonObject.getJSONObject("entity1");
                JSONObject entity2Object = jsonObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId(jsonObject.getInt("id"));
                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCargos() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entityObject = jsonObject.getJSONObject("entity");
                JSONObject entity1Object = entityObject.getJSONObject("entity1");
                JSONObject entity2Object = entityObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId((int) entityObject.getDouble("id"));

                Cargo<Order<Receiver, Product>> cargo = new Cargo<>(jsonObject.getBoolean("isdelivered"), order);
                cargo.setid(jsonObject.getInt("id"));
                cargos.add(cargo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStores() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json")));
            if (content.isEmpty()) {
                System.out.println("Stores JSON is empty.");
                return;
            }
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Store store = new Store(
                        jsonObject.getInt("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("address"),
                        jsonObject.getString("phone")
                );
                stores.add(store);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/
/*--------------------ikinci hali-----------------------------------------
package org.project.pages.home;

import org.json.JSONArray;
import org.json.JSONObject;
import org.project.models.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends JPanel {
    private List<Receiver> receivers;
    private List<Product> products;
    private List<Order<Receiver, Product>> orders;
    private List<Cargo<Order<Receiver, Product>>> cargos;
    private List<Store> stores;

    public HomePage(Frame frame, CardLayout cardLayout, JPanel cardPanel) {
        receivers = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();
        cargos = new ArrayList<>();
        stores = new ArrayList<>();

        loadReceivers();
        loadProducts();
        loadOrders();
        loadCargos();
        loadStores();

        setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        StringBuilder sb = new StringBuilder();
        sb.append("Receivers:\n");
        for (Receiver receiver : receivers) {
            sb.append(receiver.toString()).append("\n");
        }
        sb.append("\nProducts:\n");
        for (Product product : products) {
            sb.append(product.toString()).append("\n");
        }
        sb.append("\nOrders:\n");
        for (Order<Receiver, Product> order : orders) {
            sb.append(order.toString()).append("\n");
        }
        sb.append("\nCargos:\n");
        for (Cargo<Order<Receiver, Product>> cargo : cargos) {
            sb.append(cargo.toString()).append("\n");
        }
        sb.append("\nStores:\n");
        for (Store store : stores) {
            sb.append(store.toString()).append("\n");
        }
        textArea.setText(sb.toString());
    }

    private void loadReceivers() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/receivers.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Receiver receiver = new Receiver(
                        jsonObject.getString("email"),
                        jsonObject.getString("password"),
                        jsonObject.getString("name"),
                        jsonObject.getString("surname"),
                        jsonObject.getString("address")
                );
                receiver.setId(jsonObject.getInt("id"));
                receivers.add(receiver);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/products.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Product product = new Product(
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        jsonObject.getString("sellerId"),
                        jsonObject.getString("imageUrl"),
                        jsonObject.getDouble("price")
                );
                product.setId(jsonObject.getInt("id"));
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOrders() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/orders.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entity1Object = jsonObject.getJSONObject("entity1");
                JSONObject entity2Object = jsonObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId(jsonObject.getInt("id"));
                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCargos() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/cargos.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject entityObject = jsonObject.getJSONObject("entity");
                JSONObject entity1Object = entityObject.getJSONObject("entity1");
                JSONObject entity2Object = entityObject.getJSONObject("entity2");

                Receiver receiver = new Receiver(
                        entity1Object.getString("email"),
                        entity1Object.getString("password"),
                        entity1Object.getString("name"),
                        entity1Object.getString("surname"),
                        entity1Object.getString("address")
                );
                receiver.setId((int) entity1Object.getDouble("id"));

                Product product = new Product(
                        entity2Object.getString("name"),
                        entity2Object.getString("description"),
                        entity2Object.getString("sellerId"),
                        entity2Object.getString("imageUrl"),
                        entity2Object.getDouble("price")
                );
                product.setId((int) entity2Object.getDouble("id"));

                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId((int) entityObject.getDouble("id"));

                Cargo<Order<Receiver, Product>> cargo = new Cargo<>(jsonObject.getBoolean("isdelivered"), order);
                cargo.setid(jsonObject.getInt("id"));
                cargos.add(cargo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStores() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:/Users/ahmet/AppData/Roaming/.myapp/stores.json")));
            if (content.isEmpty()) {
                System.out.println("Stores JSON is empty.");
                return;
            }
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Store store = new Store(
                        jsonObject.getInt("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("address"),
                        jsonObject.getString("phone")
                );
                stores.add(store);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Receiver findReceiverById(int id) {
        for (Receiver receiver : receivers) {
            if (receiver.getId().equals(id)) {
                return receiver;
            }
        }
        return null;
    }

    private Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    private Order<Receiver, Product> findOrderById(int id) {
        for (Order<Receiver, Product> order : orders) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }
}
*/
/*----------------ilk hali------------------------------
package org.project.pages.home;

import org.json.JSONArray;
import org.json.JSONObject;
import org.project.models.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends JPanel {
    private List<Receiver> receivers;
    private List<Product> products;
    private List<Order<Receiver, Product>> orders;
    private List<Cargo<Order<Receiver, Product>>> cargos;

    public HomePage(Frame frame, CardLayout cardLayout, JPanel cardPanel) {
        receivers = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();
        cargos = new ArrayList<>();

        loadReceivers();
        loadProducts();
        loadOrders();
        loadCargos();
    }

    private void loadReceivers() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("/c:/Users/ahmet/AppData/Roaming/.myapp/receivers.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Receiver receiver = new Receiver(
                        jsonObject.getString("email"),
                        jsonObject.getString("password"),
                        jsonObject.getString("name"),
                        jsonObject.getString("surname"),
                        jsonObject.getString("address")
                );
                receiver.setId(jsonObject.getInt("id"));
                receivers.add(receiver);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("/c:/Users/ahmet/AppData/Roaming/.myapp/products.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Product product = new Product(
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        jsonObject.getString("sellerId"),
                        jsonObject.getString("imageUrl"),
                        jsonObject.getDouble("price")
                );
                product.setId(jsonObject.getInt("id"));
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOrders() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("/c:/Users/ahmet/AppData/Roaming/.myapp/orders.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Receiver receiver = findReceiverById(jsonObject.getInt("receiverId"));
                Product product = findProductById(jsonObject.getInt("productId"));
                Order<Receiver, Product> order = new Order<>(receiver, product);
                order.setId(jsonObject.getInt("id"));
                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCargos() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("/c:/Users/ahmet/AppData/Roaming/.myapp/cargos.json")));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Order<Receiver, Product> order = findOrderById(jsonObject.getInt("orderId"));
                Cargo<Order<Receiver, Product>> cargo = new Cargo<>(jsonObject.getBoolean("isDelivered"), order);
                cargo.setid(jsonObject.getInt("id"));
                cargos.add(cargo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Receiver findReceiverById(int id) {
        for (Receiver receiver : receivers) {
            if (receiver.getId().equals(id)) {
                return receiver;
            }
        }
        return null;
    }

    private Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    private Order<Receiver, Product> findOrderById(int id) {
        for (Order<Receiver, Product> order : orders) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }
}
*/