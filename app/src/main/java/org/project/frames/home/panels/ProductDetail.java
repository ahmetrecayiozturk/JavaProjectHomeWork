package org.project.frames.home.panels;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

import org.project.models.Product;
import org.project.services.ImageService;
import org.project.services.ProductService;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

import static java.lang.Integer.valueOf;

public class ProductDetail extends JPanel {
    private JTextField nameField;
    private JLabel imageLabel;
    private JTextArea descriptionField;
    private JTextField priceField;
    private JTextField stockField;
    private boolean isEditable=false;
    private Product product=new Product("","",-1,"",0.0,0);
    private final int innerPanelWidth = 600;
    private int panelWidth;
    private int panelHeight;
    private JPanel innerPanel=new JPanel();
    private Path imagePath;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public ProductDetail(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        refresh();
        add(innerPanel);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelWidth = getWidth();
                panelHeight = getHeight();
                updatePanelPositionToCenter(innerPanel);
                innerPanel.setPreferredSize(new Dimension(innerPanelWidth, panelHeight));
                revalidate();
                repaint();
            }
        });
    }
    public void initializeInnerPanel(){
        innerPanel.removeAll();
        innerPanel.setLayout(null);
        innerPanel.setBackground(Color.WHITE);

        imageLabel = new JLabel();
        imageLabel.setBounds((innerPanelWidth-400)/2, 30, 400, 300);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        innerPanel.add(imageLabel);

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(isEditable) {
                    try {
                        File image = ImageService.chooseImage();
                        assert image != null;
                        if (!image.exists()) {
                            JOptionPane.showMessageDialog(null, "Image is null!");
                            return;
                        }
                        imagePath = ImageService.saveImage(image);

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    product.setImageUrl(imagePath.toString());
                    setImage(imagePath.toString());
                }

            }
        });
        setImage(product.getImageUrl());

        JLabel nameLabel = new JLabel("Name: " );
        nameLabel.setBounds(50, 360, 75, 30);
        innerPanel.add(nameLabel);

        nameField = new JTextField(product.getName());
        nameField.setBounds(150, 360, 400, 30);
        nameField.setEditable(false);
        innerPanel.add(nameField);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(50, 400, 75, 60);
        innerPanel.add(descriptionLabel);

        descriptionField = new JTextArea(product.getDescription());
        descriptionField.setBounds(150, 400, 400, 60);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descriptionField.setEditable(false);
        descriptionField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        innerPanel.add(descriptionField);

        JLabel priceLabel = new JLabel("Price: ");
        priceLabel.setBounds(50, 480, 75, 30);
        innerPanel.add(priceLabel);

        priceField = new JTextField(String.valueOf(product.getPrice()));
        priceField.setBounds(150, 480, 400, 30);
        priceField.setEditable(false);
        innerPanel.add(priceField);

        JLabel stockLabel = new JLabel("Stock: " );
        stockLabel.setBounds(50, 520, 75, 30);
        innerPanel.add(stockLabel);

        stockField = new JTextField(String.valueOf(product.getProductCount()));
        stockField.setBounds(150, 520, 400, 30);
        stockField.setEditable(false);
        innerPanel.add(stockField);

        JButton editButton = new JButton("Edit");
        editButton.setBounds(200, 600, 100, 30);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isEditable) {
                    nameField.setEditable(true);
                    descriptionField.setEditable(true);
                    priceField.setEditable(true);
                    stockField.setEditable(true);
                    editButton.setText("Save");
                    imageLabel.setText("Editable");
                    isEditable=true;
                }else{
                    nameField.setEditable(false);
                    descriptionField.setEditable(false);
                    priceField.setEditable(false);
                    stockField.setEditable(false);
                    editButton.setText("Edit");
                    isEditable=false;
                    product.setName(nameField.getText());
                    product.setDescription(descriptionField.getText());
                    product.setPrice(Double.valueOf(priceField.getText()));
                    product.setProductCount(Integer.parseInt(stockField.getText()));
                    ProductService.update(product);
                }
            }
        });
        innerPanel.add(editButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(320, 600, 100, 30);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this product?",
                        "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    ProductService.delete(product.getId());
                    JOptionPane.showMessageDialog(null, "Product deleted!");
                    cardLayout.show(cardPanel, "Products");
                }
            }
        });
        innerPanel.add(deleteButton);
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
        refresh();
    }
    public void refresh() {
        innerPanel.removeAll();
        initializeInnerPanel();
        revalidate();
        repaint();
    }
    private void updatePanelPositionToCenter( JPanel innerPanel) {
        Dimension innerPanelSize = innerPanel.getPreferredSize();

        int x = (panelWidth - innerPanelSize.width) / 2;
        int y = (panelHeight - innerPanelSize.height) / 2;

        innerPanel.setBounds(x, y, innerPanelSize.width, innerPanelSize.height);
    }
}


