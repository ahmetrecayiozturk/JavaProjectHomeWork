package org.project.frames.home.home.panels;

import org.project.models.Product;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class Inventory extends JPanel {
    private int numRows;
    private static final int PRODUCT_MAX_WIDTH = 1000;
    private static final int PRODUCT_HEIGHT = 50;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private List<Product> products;

    public Inventory(CardLayout cardLayout,JPanel cardPanel) {
        this.products=ProductService.getAllProducts();
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
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
        productPanel.setPreferredSize(new Dimension(PRODUCT_MAX_WIDTH, PRODUCT_HEIGHT));
        productPanel.setBackground(new Color(100 + (5 % 10) * 15, 100, 255));

        JLabel productLabel = new JLabel(product.getName());
        productLabel.setHorizontalAlignment(SwingConstants.CENTER);
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
}
