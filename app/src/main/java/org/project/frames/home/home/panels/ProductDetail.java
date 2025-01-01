package org.project.frames.home.home.panels;

import javax.swing.*;
import java.io.File;

import org.project.models.Product;
import org.project.services.ProductService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductDetail extends JPanel {
    private JLabel nameLabel;
    private JLabel descriptionLabel;
    private JLabel priceLabel;
    private JLabel imageLabel;
    private ProductService productService=new ProductService();

    private Product product;

    private final int innerPanelWidth = 500;

    public ProductDetail() {
        product=new Product("","","","",0.0,0);
        initialze();
    }
    public void initialze(){
        removeAll();
        setLayout(null);
        setBounds(0, 0, innerPanelWidth, 430);

        imageLabel = new JLabel();
        imageLabel.setBounds(50, 40, 400, 300);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(imageLabel);

        setImage(product.getImageUrl());

        nameLabel = new JLabel("Product Name: " + product.getName());
        nameLabel.setBounds(50, 360, 400, 30);
        add(nameLabel);

        descriptionLabel = new JLabel( product.getDescription() );
        descriptionLabel.setBounds(50, 400, 400, 60);
        //descriptionLabel.setLineWrap(true);
        //descriptionLabel.setWrapStyleWord(true);
        add(descriptionLabel);

        priceLabel = new JLabel("Price: $" + product.getPrice());
        priceLabel.setBounds(50, 480, 400, 30);
        add(priceLabel);

        JButton editButton = new JButton("Edit");
        editButton.setBounds(200, 550, 100, 30);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        add(editButton);

        // Delete Button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(320, 550, 100, 30);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this product?",
                        "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    productService.delete(product.getId()); // Assuming ProductService.delete() method exists
                    JOptionPane.showMessageDialog(null, "Product deleted!");
                }
            }
        });
        add(deleteButton);
        revalidate();
        repaint();
    }

    public void setImage(String imageUrl) {
        File imageFile = new File(imageUrl);
        if (imageFile.exists()) {
            Image image = new ImageIcon(imageFile.getAbsolutePath()).getImage();
            image = image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(image);
            imageLabel.setIcon(icon);
            imageLabel.setText("");
            imageLabel.setBorder(null);
        } else {
            imageLabel.setText("Image not available");
        }
    }
    public void setProduct(Product product) {
        this.product = product;

        initialze();
    }
}


