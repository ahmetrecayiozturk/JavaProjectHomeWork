package org.project.frames.home.panels;

import org.project.App;
import org.project.models.Product;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class Products extends JPanel {
    // Number of columns to display products
    private int numColumns;
    // Remaining width after calculating columns
    private int remainedWith;
    // Constant for product panel size
    private static final int PRODUCT_SIZE = 250;
    // CardLayout and JPanel for navigation
    private CardLayout cardLayout;
    private JPanel cardPanel;
    // List to store products
    private List<Product> products;
    // Panel to display product details
    private ProductDetail productDetail;

    // Constructor to initialize the Products panel
    public Products(CardLayout cardLayout, JPanel cardPanel, ProductDetail productDetail) {
        this.numColumns = 2;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.productDetail = productDetail;
        initialize();
    }

    // Method to initialize the panel components
    public void initialize() {
        this.products = ProductService.getAllStoreProducts(App.getCurrentStore().getId());
        setLayout(null);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumColumns();
                revalidate();
                repaint();
            }
        });

        addProducts();
    }

    // Method to update the number of columns based on panel width
    private void updateNumColumns() {
        int panelWidth = getWidth();
        numColumns = Math.max(1, panelWidth / (PRODUCT_SIZE + 10));
        remainedWith = panelWidth - (numColumns * (PRODUCT_SIZE + 10) - 10);
    }

    // Method to add products to the panel
    private void addProducts() {
        for (Product product : products) {
            add(createProductPanel(product));
        }
        add(addNewProduct());
    }

    // Method to create a panel for each product
    private JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel();
        productPanel.setPreferredSize(new Dimension(PRODUCT_SIZE, PRODUCT_SIZE));
        productPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        productPanel.setLayout(new BorderLayout(10, 10));
        productPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        JLabel productLabel = new JLabel(product.getName());
        productLabel.setFont(new Font("Arial", Font.BOLD, 14));
        productLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        topPanel.add(productLabel, BorderLayout.CENTER);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        ImageIcon productImageIcon = new ImageIcon(product.getImageUrl());
        Image productImage = productImageIcon.getImage();
        Image resizedImage = productImage.getScaledInstance(PRODUCT_SIZE - 40, PRODUCT_SIZE - 100, Image.SCALE_SMOOTH);
        JLabel productImageLabel = new JLabel(new ImageIcon(resizedImage));
        productImageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        imagePanel.add(productImageLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        JLabel priceLabel = new JLabel(String.format("$ %.2f", product.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(0, 100, 0));
        priceLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPanel.add(priceLabel, BorderLayout.CENTER);

        productPanel.add(topPanel, BorderLayout.NORTH);
        productPanel.add(imagePanel, BorderLayout.CENTER);
        productPanel.add(bottomPanel, BorderLayout.SOUTH);

        productPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                productPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2));
                productPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                productPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                productPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                productDetail.setProduct(product);
                cardLayout.show(cardPanel, "productDetail");
            }
        });

        return productPanel;
    }

    // Method to create a panel for adding a new product
    private JPanel addNewProduct() {
        JPanel productPanel = new JPanel();
        productPanel.setPreferredSize(new Dimension(PRODUCT_SIZE, PRODUCT_SIZE));
        productPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        productPanel.setLayout(new BorderLayout());
        productPanel.setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        JLabel plusLabel = new JLabel("+");
        plusLabel.setFont(new Font("Arial", Font.BOLD, 60));
        plusLabel.setForeground(new Color(0, 120, 215));

        JLabel textLabel = new JLabel("Add New Product");
        textLabel.setFont(new Font("Arial", Font.BOLD, 14));
        textLabel.setForeground(new Color(100, 100, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        centerPanel.add(plusLabel, gbc);

        gbc.gridy = 1;
        centerPanel.add(textLabel, gbc);

        productPanel.add(centerPanel, BorderLayout.CENTER);

        // Hover effect
        productPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                productPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2));
                productPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                productPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                productPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(cardPanel, "createProduct");
            }
        });

        return productPanel;
    }

    // Override the doLayout method to arrange components
    @Override
    public void doLayout() {
        super.doLayout();
        int panelWidth = getWidth();
        int columnWidth = PRODUCT_SIZE + 10;
        int x = 0;
        int y = 0;

        for (Component component : getComponents()) {
            if (x >= numColumns) {
                x = 0;
                y++;
            }
            component.setBounds(x * columnWidth + remainedWith / 2, y * (PRODUCT_SIZE + 10) + PRODUCT_SIZE / 6, PRODUCT_SIZE, PRODUCT_SIZE);
            x++;
        }
    }

    // Method to refresh the panel
    public void refresh() {
        removeAll();
        initialize();
    }
}