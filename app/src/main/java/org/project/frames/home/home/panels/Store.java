package org.project.frames.home.home.panels;

import org.project.App;

import javax.swing.*;
import java.awt.*;


public class Store extends JPanel {
    public Store() {
        //ImageIcon storeImageIcon = new ImageIcon(App.getCurrentStore().getImageUrl());
        //JLabel storeImageLabel = new JLabel(storeImageIcon);
        //storeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        //storeImageLabel.setBounds(0, 0, 300, 300);

        JLabel storeNameLabel = new JLabel(App.getCurrentStore().getName());

        JLabel storeAddressLabel = new JLabel(App.getCurrentStore().getAddress());
        JLabel storePhoneLabel = new JLabel(App.getCurrentStore().getPhone());
        JTextArea storeDescriptionArea = new JTextArea(App.getCurrentStore().getDescription());
        storeDescriptionArea.setWrapStyleWord(true);
        storeDescriptionArea.setLineWrap(true);
        storeDescriptionArea.setEditable(false);
        storeDescriptionArea.setPreferredSize(new Dimension(300, 100));

        add(storeNameLabel);
        add(storeAddressLabel);
        add(storePhoneLabel);
        add(storeDescriptionArea);
    }
}



