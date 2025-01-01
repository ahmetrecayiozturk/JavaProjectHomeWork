package org.project.frames.home.home.panels;

import org.project.App;
import javax.swing.*;
import java.awt.*;

public class Store extends JPanel {
    public Store() {
        initialize();
    }
    public void initialize(){
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(300, 300));
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        try {
            ImageIcon storeImageIcon = new ImageIcon(App.getCurrentStore().getImageUrl());
            Image image = storeImageIcon.getImage().getScaledInstance(280, 280, Image.SCALE_SMOOTH);
            JLabel storeImageLabel = new JLabel(new ImageIcon(image));
            imagePanel.add(storeImageLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel storeInfoContainer = new JPanel();
        storeInfoContainer.setLayout(new BoxLayout(storeInfoContainer, BoxLayout.Y_AXIS));
        storeInfoContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel storeNameLabel = new JLabel(App.getCurrentStore().getName());
        storeNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        storeNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        storeInfoContainer.add(storeNameLabel);
        storeInfoContainer.add(Box.createVerticalStrut(15));

        headerPanel.add(imagePanel, BorderLayout.WEST);
        headerPanel.add(storeInfoContainer, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel addressLabel = new JLabel(  App.getCurrentStore().getAddress());
        JLabel phoneLabel = new JLabel( App.getCurrentStore().getPhone());

        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        infoPanel.add(addressLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(phoneLabel);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBorder(BorderFactory.createTitledBorder("About"));

        JTextArea storeDescriptionArea = new JTextArea(App.getCurrentStore().getDescription());
        storeDescriptionArea.setWrapStyleWord(true);
        storeDescriptionArea.setLineWrap(true);
        storeDescriptionArea.setEditable(false);
        storeDescriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        storeDescriptionArea.setBackground(new Color(245, 245, 245));
        storeDescriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(storeDescriptionArea);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        descriptionPanel.add(scrollPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(descriptionPanel, BorderLayout.SOUTH);
    }
    public void refresh(){
        removeAll();
        initialize();
    }
}



