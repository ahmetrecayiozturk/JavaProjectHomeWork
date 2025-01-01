package org.project.frames.home.home.panels;

import org.project.models.Product;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.List;

public class Inventory extends JPanel {
    private int numRows;
    private static final int PRODUCT_MAX_WIDTH = 1000;
    private static final int PRODUCT_HEIGHT = 60;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private List<Product> products;
    private ProductService productService=new ProductService();

    public Inventory(CardLayout cardLayout,JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        initialize();
    }

    private void initialize(){
        this.products=productService.getAllProducts();
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumRows();
                revalidate();
                repaint();
            }
        });
        addProducts();
    }

    private void updateNumRows() {
        int panelHeight = getHeight();
        numRows = Math.max(1, panelHeight / (PRODUCT_HEIGHT + 10));
    }

    private void addProducts() {
        for (Product product:products) {
            add(createProductPanel(product));
        }
    }

    private JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel();
        //50 1000
        productPanel.setPreferredSize(new Dimension(PRODUCT_MAX_WIDTH, PRODUCT_HEIGHT));
        productPanel.setBackground(new Color(100 + (5 % 10) * 15, 100, 255));
        productPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        productPanel.setLayout(null);

        JLabel productLabel = new JLabel(product.getName().toUpperCase());
        productLabel.setBounds(70, 3, 400, 30);
        JLabel productLabel2 = new JLabel(product.getDescription());
        productLabel2.setBounds(70, 20, 400, 30);

        JLabel imageLabel = new JLabel();
        File imageFile = new File(product.getImageUrl());
        Image image = new ImageIcon(imageFile.getAbsolutePath()).getImage();
        image = image.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
        imageLabel.setBounds(0, 0, 60, 60);

        productPanel.add(imageLabel);
        productPanel.add(productLabel2);
        productPanel.add(productLabel);

        productPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(cardPanel, "productDetail");
            }
        });

        return productPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int y = 0;
        int x = (getWidth() - PRODUCT_MAX_WIDTH) / 2;

        for (Component component : getComponents()) {
            component.setBounds(x, y * (PRODUCT_HEIGHT + 10)+PRODUCT_HEIGHT/4 , PRODUCT_MAX_WIDTH, PRODUCT_HEIGHT);
            y++;
        }
    }
    public void refresh(){
        removeAll();
        initialize();
    }
}
