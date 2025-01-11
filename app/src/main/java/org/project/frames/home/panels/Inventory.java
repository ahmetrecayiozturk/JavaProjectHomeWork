package org.project.frames.home.panels;

import org.project.App;
import org.project.models.Product;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class Inventory extends JPanel {
    private static final int PRODUCT_MAX_WIDTH = 1000;
    private static final int PRODUCT_HEIGHT = 60;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private List<Product> products;
    private ProductDetail productDetail;
    private JPanel productsPanel;

    public Inventory(CardLayout cardLayout, JPanel cardPanel, ProductDetail productDetail) {
        this.productDetail = productDetail;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        initialize();
    }

    private void initialize() {
        this.products = ProductService.getAllStoreProducts(App.getCurrentStore().getId());
        setLayout(new BorderLayout());
        
        productsPanel = new JPanel();
        productsPanel.setLayout(null);
        addProducts();

        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addProducts() {
        for (Product product : products) {
            productsPanel.add(createProductPanel(product));
        }
    }

    private JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel();
        productPanel.setPreferredSize(new Dimension(PRODUCT_MAX_WIDTH, PRODUCT_HEIGHT));
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        productPanel.setLayout(null);

        JLabel productLabel = new JLabel("Product Name: "+product.getName().toUpperCase());
        productLabel.setBounds(75, 3, 400, 30);
        JLabel productLabel2 = new JLabel("Description: "+product.getDescription());
        productLabel2.setBounds(75, 23, 400, 30);

        JLabel imageLabel = new JLabel();
        File imageFile = new File(product.getImageUrl());
        Image image = new ImageIcon(imageFile.getAbsolutePath()).getImage();
        image = image.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
        imageLabel.setBounds(1, 1, 58, 58);

        productPanel.add(imageLabel);
        productPanel.add(productLabel2);
        productPanel.add(productLabel);

        productPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                productDetail.setProduct(product);
                cardLayout.show(cardPanel, "productDetail");
            }
        });

        JLabel productPriceLabel = new JLabel("$" + product.getPrice().toString());
        productPriceLabel.setBounds(70, 65, 100, 30);

        JLabel productCountLabel = new JLabel("Stock: " + product.getProductCount());
        productCountLabel.setBounds(800, 18, 100, 30);

        JButton increaseStockButton = new JButton("Change Stock");
        increaseStockButton.setBounds(880, 0, 120, 60);

        increaseStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newStockString = JOptionPane.showInputDialog(
                        null,
                        "Enter new stock number:",
                        "Increase Stock",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (newStockString != null && !newStockString.isEmpty()) {
                    try {
                        int newStock = Integer.parseInt(newStockString);
                            product.setProductCount(newStock);
                            ProductService.updateProduct(product);
                            productCountLabel.setText("Stok: " + product.getProductCount());
                            JOptionPane.showMessageDialog(null, "Stock successfully updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid number!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        productPanel.add(imageLabel);
        productPanel.add(productLabel);
        productPanel.add(productLabel2);
        productPanel.add(productPriceLabel);
        productPanel.add(productCountLabel);
        productPanel.add(increaseStockButton);

        return productPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        if (productsPanel != null) {
            int y = 0;
            int x = (productsPanel.getWidth() - PRODUCT_MAX_WIDTH) / 2;

            for (Component component : productsPanel.getComponents()) {
                component.setBounds(x, y * (PRODUCT_HEIGHT + 10) + PRODUCT_HEIGHT / 4, PRODUCT_MAX_WIDTH, PRODUCT_HEIGHT);
                y++;
            }
            productsPanel.setPreferredSize(new Dimension(
                PRODUCT_MAX_WIDTH,
                (y * (PRODUCT_HEIGHT + 10)) + PRODUCT_HEIGHT
            ));
        }
    }

    public void refresh() {
        productsPanel.removeAll();
        this.products = ProductService.getAllStoreProducts(App.getCurrentStore().getId());
        addProducts();
        revalidate();
        repaint();
    }
}
