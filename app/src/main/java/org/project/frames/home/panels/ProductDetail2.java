package org.project.frames.home.panels;

import org.project.models.Product;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ProductDetail2 extends JPanel {
    private JLabel nameLabel;
    private JLabel descriptionLabel;
    private JLabel priceLabel;
    private JLabel imageLabel;
    private JLabel orderProductCountLabel;
    private Product product;

    public ProductDetail2() {
        initialize();
    }

    public void initialize() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(400, 300));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        detailsPanel.add(imageLabel, gbc);

        nameLabel = createLabel("");
        gbc.gridy++;
        gbc.gridwidth = 1;
        detailsPanel.add(nameLabel, gbc);

        descriptionLabel = createLabel("");
        gbc.gridy++;
        detailsPanel.add(descriptionLabel, gbc);

        priceLabel = createLabel("");
        gbc.gridy++;
        detailsPanel.add(priceLabel, gbc);

        orderProductCountLabel = createLabel("");
        gbc.gridy++;
        detailsPanel.add(orderProductCountLabel, gbc);

        add(detailsPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    public void setProduct(Integer productId, int orderQuantity) {
        this.product = ProductService.getProductById(productId);
        nameLabel.setText("Name: " + product.getName());
        descriptionLabel.setText("<html><body style='width: 300px'>Description: " + product.getDescription() + "</body></html>");
        priceLabel.setText("Price: $" + product.getPrice());
        orderProductCountLabel.setText("Order Product Count: " + orderQuantity);
        SwingUtilities.invokeLater(() -> setImage(product.getImageUrl()));
        revalidate();
        repaint();
    }

    private void setImage(String imageUrl) {
        if (imageLabel.getWidth() > 0 && imageLabel.getHeight() > 0) {
            File imageFile = new File(imageUrl);
            if (imageFile.exists()) {
                Image image = new ImageIcon(imageFile.getAbsolutePath()).getImage();
                image = image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(image);
                imageLabel.setIcon(icon);
                imageLabel.setText("");
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            } else {
                imageLabel.setText("Image not available");
            }
        } else {
            imageLabel.setText("Image not available");
        }
    }
}