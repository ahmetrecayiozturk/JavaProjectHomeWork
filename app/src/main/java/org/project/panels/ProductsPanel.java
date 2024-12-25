package org.project.panels;

import org.project.models.Product;
import org.project.services.ImageService;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProductsPanel extends JPanel {
    private ProductService productService;
    private ImageService imageService;
    private List<Product> products;

    public ProductsPanel(ProductService productService, ImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
        this.products = productService.getAllProducts();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadProducts();
    }

    private void loadProducts() {
        removeAll(); // Mevcut tüm bileşenleri kaldır
        for (Product product : products) {
            addCard(product);
        }
        revalidate(); // Bileşenleri yeniden doğrula
        repaint(); // Paneli yeniden çiz
    }

    private void addCard(Product product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Product ID: " + product.getId());
        JTextArea contentArea = new JTextArea(product.toString());
        contentArea.setEditable(false);

        JButton detailsButton = new JButton("Details");
        detailsButton.addActionListener(e -> showDetailsDialog(product));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            productService.delete(product.getId());
            refresh();
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> showUpdateDialog(product));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Product product) {
        JDialog dialog = new JDialog((Frame) null, "Product Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(product.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Product product) {
        JDialog dialog = new JDialog((Frame) null, "Update Product", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField nameField = new JTextField(product.getName());
        JTextField descriptionField = new JTextField(product.getDescription());
        JTextField sellerIdField = new JTextField(product.getSellerId());
        JTextField imageUrlField = new JTextField(product.getImageUrl());
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()));

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
            String name = nameField.getText();
            String description = descriptionField.getText();
            String sellerId = sellerIdField.getText();
            String imageUrl = imageUrlField.getText();
            double price = Double.parseDouble(priceField.getText());
            if (!name.isEmpty() && !description.isEmpty() && !sellerId.isEmpty() && !imageUrl.isEmpty()) {
                product.setName(name);
                product.setDescription(description);
                product.setSellerId(sellerId);
                product.setImageUrl(imageUrl);
                File imageFile = null;
                try {
                    imageFile = imageService.chooseImage();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                productService.update(product, imageFile);
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

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Product", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
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

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String description = descriptionField.getText();
            String sellerId = sellerIdField.getText();
            String imageUrl = imageUrlField.getText();
            double price = Double.parseDouble(priceField.getText());
            if (!name.isEmpty() && !description.isEmpty() && !sellerId.isEmpty() && !imageUrl.isEmpty()) {
                Product product = new Product(name, description, sellerId, imageUrl, price);
                File imageFile = null;
                try {
                    imageFile = imageService.chooseImage();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                productService.add(product, imageFile);
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

    public void refresh() {
        this.products = productService.getAllProducts();
        loadProducts();
    }
}
/*package org.project.components;

import org.project.models.Product;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductsPanel extends JPanel {
    private ProductService productService;
    private List<Product> products;

    public ProductsPanel(ProductService productService) {
        this.productService = productService;
        this.products = productService.getAllProducts();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadProducts();
    }

    private void loadProducts() {
        for (Product product : products) {
            addCard(product);
        }
    }

    private void addCard(Product product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Product ID: " + product.getId());
        JTextArea contentArea = new JTextArea(product.toString());
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(product));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Product product) {
        JDialog dialog = new JDialog((Frame) null, "Product Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(product.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}*/