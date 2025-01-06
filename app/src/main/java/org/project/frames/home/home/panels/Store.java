package org.project.frames.home.home.panels;

import org.project.App;
import org.project.services.ImageService;
import org.project.services.StoreService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Store extends JPanel {
    private int panelWidth;
    private int panelHeight;
    private JPanel innerPanel=new JPanel();
    private boolean isEditable=false;
    private Path imagePath;
    private JPanel imagePanel = new JPanel();
    private JLabel storeImageLabel;
    public Store() {
        initializeInnerPanel();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelWidth = getWidth();
                panelHeight = getHeight();
                updatePanelPositionToCenter(innerPanel);
                revalidate();
                repaint();
            }
        });
        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isEditable) {
                    try {
                        File image = ImageService.chooseImage();
                        if (!(image != null && image.exists())) {
                            JOptionPane.showMessageDialog(null, "Image is null!");
                            return;
                        }
                        imagePath = ImageService.saveImage(image);
                        App.getCurrentStore().setImageUrl(imagePath.toString());
                        setImage();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

    }
    public void initializeInnerPanel(){
        innerPanel.setLayout(null);
        innerPanel.setBounds(0, 0, 600, 600);
        innerPanel.setPreferredSize(new Dimension(600, 800));
        innerPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));


        imagePanel.setPreferredSize(new Dimension(400, 300));
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        imagePanel.setBounds(100, 10, 400, 300);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setBounds(30, 340, 70, 30);

        JTextField storeNameLabel = new JTextField(App.getCurrentStore().getName());
        storeNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        storeNameLabel.setBounds(100, 340, 380, 30);
        storeNameLabel.setEditable(false);

        setImage();
        storeImageLabel.setPreferredSize(new Dimension(400, 300));

        innerPanel.add(nameLabel);
        innerPanel.add(storeNameLabel);
        imagePanel.add(storeImageLabel);

        JLabel addressLabel = new JLabel("Adress: ");
        JTextField addressField = new JTextField(App.getCurrentStore().getAddress());
        addressField.setEditable(false);

        JLabel phoneLabel = new JLabel("Phone: ");
        JTextField phoneField = new JTextField(App.getCurrentStore().getPhone());
        phoneField.setEditable(false);

        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addressField.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));

        addressLabel.setBounds(30, 400, 70, 30);
        phoneLabel.setBounds(30, 440, 70, 30);
        addressField.setBounds(100, 400, 380, 30);
        phoneField.setBounds(100, 440, 380, 30);

        innerPanel.add(addressLabel);
        innerPanel.add(phoneLabel);
        innerPanel.add(addressField);
        innerPanel.add(phoneField);

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setBorder(BorderFactory.createTitledBorder("About"));
        descriptionPanel.setBounds(30, 520, 540, 120);

        JTextField storeDescriptionArea = new JTextField(App.getCurrentStore().getDescription());
        storeDescriptionArea.setEditable(false);
        descriptionPanel.add(storeDescriptionArea);

        JButton editButton = new JButton();
        editButton.setText("Edit");
        editButton.setBounds(230, 680, 100, 30);
        editButton.addActionListener(e -> {
            if(isEditable) {
                App.getCurrentStore().setDescription(storeDescriptionArea.getText());
                App.getCurrentStore().setName(storeNameLabel.getText());
                App.getCurrentStore().setPhone(phoneField.getText());
                App.getCurrentStore().setAddress(addressField.getText());
                editButton.setText("Edit");
                storeDescriptionArea.setEditable(false);
                storeNameLabel.setEditable(false);
                phoneField.setEditable(false);
                addressField.setEditable(false);
                isEditable = false;
                StoreService.update(App.getCurrentStore());
            }else {
                editButton.setText("Save");
                storeNameLabel.setEditable(true);
                phoneField.setEditable(true);
                addressField.setEditable(true);
                storeDescriptionArea.setEditable(true);
                isEditable = true;
            }
        });

        innerPanel.add(editButton);
        innerPanel.add(imagePanel);
        innerPanel.add(descriptionPanel);
        innerPanel.setBackground(Color.WHITE);
        add(innerPanel);
    }
    public void refresh(){
        removeAll();
        initializeInnerPanel();
    }
    private void updatePanelPositionToCenter( JPanel innerPanel) {
        Dimension innerPanelSize = innerPanel.getPreferredSize();

        int x = (panelWidth - innerPanelSize.width) / 2;
        int y = (panelHeight - innerPanelSize.height) / 2;

        innerPanel.setBounds(x, y, innerPanelSize.width, innerPanelSize.height);
    }
    public void setImage(){
        imagePanel.removeAll();
        if(App.getCurrentStore().getImageUrl().equals("")){
            storeImageLabel = new JLabel("Upload an image");
            storeImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            storeImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        } else{
            try {
                ImageIcon storeImageIcon = new ImageIcon(App.getCurrentStore().getImageUrl());
                Image image = storeImageIcon.getImage().getScaledInstance(storeImageLabel.getWidth(), storeImageLabel.getHeight(), Image.SCALE_SMOOTH);
                storeImageLabel = new JLabel(new ImageIcon(image));
                storeImageLabel.setText("");
                storeImageLabel.setBorder(null);
            } catch (Exception e) {
                storeImageLabel = new JLabel("Image not available");
            }
        }
        imagePanel.add(storeImageLabel);
        revalidate();
        repaint();
    }
}



