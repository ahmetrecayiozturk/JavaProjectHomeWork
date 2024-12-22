package org.project.components;

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