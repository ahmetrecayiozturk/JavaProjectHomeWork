package org.project.frames.home.home.panels;

import org.project.models.Product;
import org.project.services.ImageService;
import org.project.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class AddProduct extends JPanel {
    private JTextField nameField;
    private JTextArea descriptionField;
    private JTextField priceField;
    private JLabel imageLabel;

    public String name;
    public String description;
    public Double price;
    public File image;
    public Path imagePath;

    public final int innerPanelWith=500;

    public AddProduct() {

        setLayout(null);
        setBounds(0, 0, innerPanelWith, 430);
        imageLabel = new JLabel();
        imageLabel.setBounds(50, 40, 400, 300);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imageLabel.setText("Upload an image");
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(imageLabel);

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    image=ImageService.chooseImage();
                    assert image != null;
                    if (!image.exists()){
                        JOptionPane.showMessageDialog(null, "Image is null!");
                        return;
                    }
                    setImage(image);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        JLabel nameLabel = new JLabel("Product name:");
        nameLabel.setBounds(50, 360, 100, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(200, 360, 250, 30);
        add(nameField);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(50, 410, 100, 30);
        add(descriptionLabel);

        descriptionField = new JTextArea();
        descriptionField.setBounds(200, 410, 250, 60);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        add(descriptionField);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(50, 490, 100, 30);
        add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(200, 490, 250, 30);
        add(priceField);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(200, 550, 100, 30);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = nameField.getText();
                description = descriptionField.getText();
                try{
                price = Double.parseDouble(priceField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Price error!");
                    return;
                }
                if(name.isEmpty()||description.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Fill all blanks!");
                } else if (!image.exists()) {
                    JOptionPane.showMessageDialog(null, "Upload an image!");
                }else{
                    try {
                        imagePath=ImageService.saveImage(image);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Product newProduct=new Product();
                    newProduct.setName(name);
                    newProduct.setDescription(description);
                    newProduct.setPrice(price);
                    newProduct.setImageUrl(imagePath.toString());
                    newProduct.setProductCount(0);
                    ProductService.add(newProduct);

                    JOptionPane.showMessageDialog(null, "Product saved!");
                }
            }
        });
        add(saveButton);
    }

    public void setImage(File file) {
        Image image = new ImageIcon(file.getAbsolutePath()).getImage();
        image = image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);

        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
        imageLabel.setText("");
        imageLabel.setBorder(null);
    }

}
