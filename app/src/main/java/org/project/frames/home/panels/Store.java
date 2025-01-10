package org.project.frames.home.panels;

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
                innerPanel.setPreferredSize(new Dimension(600, panelHeight));
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
        innerPanel.setPreferredSize(new Dimension(600, panelHeight));
        innerPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        imagePanel.setPreferredSize(new Dimension(400, 300));
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        imagePanel.setBounds(100, 10, 400, 300);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setBounds(50, 340, 75, 30);

        JTextField storeNameLabel = new JTextField(App.getCurrentStore().getName());
        storeNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        storeNameLabel.setBounds(150, 340, 400, 30);
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

        JLabel aboutLabel = new JLabel("About: ");
        JTextArea descriptionField = new JTextArea(App.getCurrentStore().getDescription());
        descriptionField.setEditable(false);

        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        aboutLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addressField.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionField.setFont(new Font("Arial", Font.PLAIN, 14));

        addressLabel.setBounds(50, 400, 75, 30);
        phoneLabel.setBounds(50, 440, 75, 30);
        aboutLabel.setBounds(50, 500, 75, 30);
        addressField.setBounds(150, 400, 400, 30);
        phoneField.setBounds(150, 440, 400, 30);
        descriptionField.setBounds(150, 500, 400, 120);

        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descriptionField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        descriptionField.setBackground(new Color(238, 238, 238));

        innerPanel.add(addressLabel);
        innerPanel.add(phoneLabel);
        innerPanel.add(aboutLabel);
        innerPanel.add(addressField);
        innerPanel.add(phoneField);
        innerPanel.add(descriptionField);


        JButton editButton = new JButton();
        editButton.setText("Edit");
        editButton.setBounds(250, 680, 100, 30);
        editButton.addActionListener(e -> {
            if(isEditable) {
                App.getCurrentStore().setDescription(descriptionField.getText());
                App.getCurrentStore().setName(storeNameLabel.getText());
                App.getCurrentStore().setPhone(phoneField.getText());
                App.getCurrentStore().setAddress(addressField.getText());
                editButton.setText("Edit");
                descriptionField.setBackground(new Color(238, 238, 238));
                descriptionField.setEditable(false);
                storeNameLabel.setEditable(false);
                phoneField.setEditable(false);
                addressField.setEditable(false);
                isEditable = false;
                StoreService.update(App.getCurrentStore());
            }else {
                editButton.setText("Save");
                descriptionField.setBackground(Color.WHITE);
                storeNameLabel.setEditable(true);
                phoneField.setEditable(true);
                addressField.setEditable(true);
                descriptionField.setEditable(true);
                isEditable = true;
            }
        });

        innerPanel.add(editButton);
        innerPanel.add(imagePanel);
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



