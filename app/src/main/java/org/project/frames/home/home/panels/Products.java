package org.project.frames.home.home.panels;

import org.project.models.Product;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class Products extends JPanel {
    private int numColumns;
    private int remainedWith;
    private static final int PRODUCT_SIZE = 250;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private List<Product> products;
    private ProductDetail productDetail;

    public Products(CardLayout cardLayout,JPanel cardPanel,ProductDetail productDetail) {
        this.products=ProductService.getAllProducts();
        this.numColumns = 2;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.productDetail=productDetail;
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

    private void updateNumColumns() {
        int panelWidth = getWidth();
        numColumns = Math.max(1, panelWidth / (PRODUCT_SIZE + 10));
        remainedWith = panelWidth-(numColumns * (PRODUCT_SIZE + 10)-10);
    }

    private void addProducts() {
        for (Product product : products) {
            add(createProductPanel(product));
        }
        add(addNewProduct());
    }

    private JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel();
        productPanel.setPreferredSize(new Dimension(PRODUCT_SIZE, PRODUCT_SIZE));
        productPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        productPanel.setLayout(null);
        JLabel productLabel =new JLabel(product.getName());
        productLabel.setBounds(10, 10, 150, 30);
        productPanel.add(productLabel);

        ImageIcon productImageIcon = new ImageIcon(product.getImageUrl());
        Image productImage = productImageIcon.getImage();
        Image resizedImage = productImage.getScaledInstance(PRODUCT_SIZE, PRODUCT_SIZE, Image.SCALE_SMOOTH);
        productImageIcon = new ImageIcon(resizedImage);
        JLabel productImageLabel = new JLabel(productImageIcon);
        productImageLabel.setBounds(10, 50, PRODUCT_SIZE - 20, PRODUCT_SIZE - 60);

        productPanel.add(productImageLabel);



        productPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                productDetail.setProduct(product);
                cardLayout.show(cardPanel, "productDetail");
            }
        });
        return productPanel;
    }
    private JPanel addNewProduct() {
        JPanel productPanel = new JPanel();
        productPanel.setPreferredSize(new Dimension(PRODUCT_SIZE, PRODUCT_SIZE));
        productPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        productPanel.setLayout(null);
        JLabel productNameLabel = new JLabel("+");
        productNameLabel.setFont(new Font("Arial", Font.BOLD, 60));
        productNameLabel.setBounds(110, 90, 50, 30);
        productPanel.add(productNameLabel);
        JLabel text=new JLabel("add new product");
        text.setBounds(80, 140, 150, 30);
        productPanel.add(text);
        productPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(cardPanel, "createProduct");
            }
        });
        return productPanel;
    }

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
            component.setBounds(x * columnWidth+remainedWith/2, y * (PRODUCT_SIZE + 10)+PRODUCT_SIZE/6, PRODUCT_SIZE, PRODUCT_SIZE);
            x++;
        }
    }

}