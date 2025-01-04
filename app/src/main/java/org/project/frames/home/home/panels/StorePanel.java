package org.project.frames.home.home.panels;

import org.project.App;
import org.project.models.Store;
import org.project.services.ImageService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class StorePanel extends JPanel {
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextArea descriptionArea;
    private JTextField imageUrlField;
    private JLabel storeImageLabel;
    private ImageService imageService;

    public StorePanel() {
        imageService = new ImageService();
        initialize();
    }

    public void initialize() {
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
            storeImageLabel = new JLabel(new ImageIcon(image));
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

        JLabel addressLabel = new JLabel("Address: " + App.getCurrentStore().getAddress());
        JLabel phoneLabel = new JLabel("Phone: " + App.getCurrentStore().getPhone());
        JLabel descriptionLabel = new JLabel("Description: " + App.getCurrentStore().getDescription());

        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        infoPanel.add(addressLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(phoneLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(descriptionLabel);

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

        // Add update button
        JButton updateButton = new JButton("Update Store");
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setBackground(new Color(70, 130, 180));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        updateButton.addActionListener(e -> showUpdateDialog());
        add(updateButton, BorderLayout.SOUTH);
    }

    private void showUpdateDialog() {
        JDialog dialog = new JDialog((Frame) null, "Update Store", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        nameField = new JTextField(App.getCurrentStore().getName());
        addressField = new JTextField(App.getCurrentStore().getAddress());
        phoneField = new JTextField(App.getCurrentStore().getPhone());
        descriptionArea = new JTextArea(App.getCurrentStore().getDescription());
        imageUrlField = new JTextField(App.getCurrentStore().getImageUrl());

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(nameField, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(addressField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(phoneField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setPreferredSize(new Dimension(200, 100));
        formPanel.add(descriptionScrollPane, gbc);

        // Image URL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        formPanel.add(new JLabel("Image URL:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(imageUrlField, gbc);

        // Image Upload Button
        JButton uploadButton = new JButton("Upload Image");
        uploadButton.setFont(new Font("Arial", Font.BOLD, 16));
        uploadButton.setBackground(new Color(70, 130, 180));
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFocusPainted(false);
        uploadButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        uploadButton.addActionListener(e -> {
            try {
                File selectedFile = imageService.chooseImage();
                if (selectedFile != null) {
                    Path savedImagePath = imageService.saveImage(selectedFile);
                    imageUrlField.setText(savedImagePath.toString());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        formPanel.add(uploadButton, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.addActionListener(e -> {
            App.getCurrentStore().setName(nameField.getText());
            App.getCurrentStore().setAddress(addressField.getText());
            App.getCurrentStore().setPhone(phoneField.getText());
            App.getCurrentStore().setDescription(descriptionArea.getText());
            App.getCurrentStore().setImageUrl(imageUrlField.getText());
            App.saveCurrentStore(); // Store'u kaydet
            refresh();
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        removeAll();
        initialize();
        revalidate();
        repaint();
    }
}
/*
package org.project.frames.home.home.panels;

import org.project.App;
import org.project.models.Store;

import javax.swing.*;
import java.awt.*;

public class StorePanel extends JPanel {
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextArea descriptionArea;
    private JTextField imageUrlField;

    public StorePanel() {
        initialize();
    }

    public void initialize() {
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

        JLabel addressLabel = new JLabel("Address: " + App.getCurrentStore().getAddress());
        JLabel phoneLabel = new JLabel("Phone: " + App.getCurrentStore().getPhone());
        JLabel descprictionLabel = new JLabel("Phone: " + App.getCurrentStore().getDescription());

        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descprictionLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        infoPanel.add(addressLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(phoneLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(descprictionLabel);

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

        // Add update button
        JButton updateButton = new JButton("Update Store");
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setBackground(new Color(70, 130, 180));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        updateButton.addActionListener(e -> showUpdateDialog());
        add(updateButton, BorderLayout.SOUTH);
    }

    private void showUpdateDialog() {
        JDialog dialog = new JDialog((Frame) null, "Update Store", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        nameField = new JTextField(App.getCurrentStore().getName());
        addressField = new JTextField(App.getCurrentStore().getAddress());
        phoneField = new JTextField(App.getCurrentStore().getPhone());
        descriptionArea = new JTextArea(App.getCurrentStore().getDescription());
        imageUrlField = new JTextField(App.getCurrentStore().getImageUrl());

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(nameField, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(addressField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(phoneField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setPreferredSize(new Dimension(200, 100));
        formPanel.add(descriptionScrollPane, gbc);

        // Image URL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        formPanel.add(new JLabel("Image URL:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(imageUrlField, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.addActionListener(e -> {
            App.getCurrentStore().setName(nameField.getText());
            App.getCurrentStore().setAddress(addressField.getText());
            App.getCurrentStore().setPhone(phoneField.getText());
            App.getCurrentStore().setDescription(descriptionArea.getText());
            App.getCurrentStore().setImageUrl(imageUrlField.getText());
            refresh();
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        removeAll();
        initialize();
        revalidate();
        repaint();
    }
}
*/
/*
package org.project.frames.home.home.panels;

import org.project.App;
import org.project.models.Store;

import javax.swing.*;
import java.awt.*;

public class StorePanel extends JPanel {
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextArea descriptionArea;
    private JTextField imageUrlField;

    public StorePanel() {
        initialize();
    }

    public void initialize() {
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

        JLabel addressLabel = new JLabel(App.getCurrentStore().getAddress());
        JLabel phoneLabel = new JLabel(App.getCurrentStore().getPhone());

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

        // Add update button
        JButton updateButton = new JButton("Update Store");
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setBackground(new Color(70, 130, 180));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        updateButton.addActionListener(e -> showUpdateDialog());
        add(updateButton, BorderLayout.SOUTH);
    }

    private void showUpdateDialog() {
        JDialog dialog = new JDialog((Frame) null, "Update Store", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        nameField = new JTextField(App.getCurrentStore().getName());
        addressField = new JTextField(App.getCurrentStore().getAddress());
        phoneField = new JTextField(App.getCurrentStore().getPhone());
        descriptionArea = new JTextArea(App.getCurrentStore().getDescription());
        imageUrlField = new JTextField(App.getCurrentStore().getImageUrl());

        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx++;
        formPanel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx++;
        formPanel.add(addressField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx++;
        formPanel.add(phoneField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setPreferredSize(new Dimension(200, 100));
        formPanel.add(descriptionScrollPane, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;

        formPanel.add(new JLabel("Image URL:"), gbc);
        gbc.gridx++;
        formPanel.add(imageUrlField, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.addActionListener(e -> {
            App.getCurrentStore().setName(nameField.getText());
            App.getCurrentStore().setAddress(addressField.getText());
            App.getCurrentStore().setPhone(phoneField.getText());
            App.getCurrentStore().setDescription(descriptionArea.getText());
            App.getCurrentStore().setImageUrl(imageUrlField.getText());
            refresh();
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        removeAll();
        initialize();
        revalidate();
        repaint();
    }
}
*/

/*
package org.project.frames.home.home.panels;

import org.project.App;
import org.project.models.Store;

import javax.swing.*;
import java.awt.*;

public class StorePanel extends JPanel {
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextArea descriptionArea;
    private JTextField imageUrlField;

    public StorePanel() {
        initialize();
    }

    public void initialize() {
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

        JLabel addressLabel = new JLabel(App.getCurrentStore().getAddress());
        JLabel phoneLabel = new JLabel(App.getCurrentStore().getPhone());

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

        // Add update button
        JButton updateButton = new JButton("Update Store");
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setBackground(new Color(70, 130, 180));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        updateButton.addActionListener(e -> showUpdateDialog());
        add(updateButton, BorderLayout.SOUTH);
    }

    private void showUpdateDialog() {
        JDialog dialog = new JDialog((Frame) null, "Update Store", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        nameField = new JTextField(App.getCurrentStore().getName());
        addressField = new JTextField(App.getCurrentStore().getAddress());
        phoneField = new JTextField(App.getCurrentStore().getPhone());
        descriptionArea = new JTextArea(App.getCurrentStore().getDescription());
        imageUrlField = new JTextField(App.getCurrentStore().getImageUrl());

        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx++;
        formPanel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx++;
        formPanel.add(addressField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx++;
        formPanel.add(phoneField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setPreferredSize(new Dimension(200, 100));
        formPanel.add(descriptionScrollPane, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;

        formPanel.add(new JLabel("Image URL:"), gbc);
        gbc.gridx++;
        formPanel.add(imageUrlField, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.addActionListener(e -> {
            App.getCurrentStore().setName(nameField.getText());
            App.getCurrentStore().setAddress(addressField.getText());
            App.getCurrentStore().setPhone(phoneField.getText());
            App.getCurrentStore().setDescription(descriptionArea.getText());
            App.getCurrentStore().setImageUrl(imageUrlField.getText());
            refresh();
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        removeAll();
        initialize();
    }
}
*/

/*
package org.project.frames.home.home.panels;

import org.project.App;
import org.project.models.Store;

import javax.swing.*;
import java.awt.*;

public class StorePanel extends JPanel {
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextArea descriptionArea;
    private JTextField imageUrlField;

    public StorePanel() {
        initialize();
    }

    public void initialize() {
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

        JLabel addressLabel = new JLabel(App.getCurrentStore().getAddress());
        JLabel phoneLabel = new JLabel(App.getCurrentStore().getPhone());

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

        // Add update button
        JButton updateButton = new JButton("Update Store");
        updateButton.addActionListener(e -> showUpdateDialog());
        add(updateButton, BorderLayout.SOUTH);
    }

    private void showUpdateDialog() {
        JDialog dialog = new JDialog((Frame) null, "Update Store", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        nameField = new JTextField(App.getCurrentStore().getName());
        addressField = new JTextField(App.getCurrentStore().getAddress());
        phoneField = new JTextField(App.getCurrentStore().getPhone());
        descriptionArea = new JTextArea(App.getCurrentStore().getDescription());
        imageUrlField = new JTextField(App.getCurrentStore().getImageUrl());

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(new JScrollPane(descriptionArea));
        formPanel.add(new JLabel("Image URL:"));
        formPanel.add(imageUrlField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            App.getCurrentStore().setName(nameField.getText());
            App.getCurrentStore().setAddress(addressField.getText());
            App.getCurrentStore().setPhone(phoneField.getText());
            App.getCurrentStore().setDescription(descriptionArea.getText());
            App.getCurrentStore().setImageUrl(imageUrlField.getText());
            refresh();
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        removeAll();
        initialize();
    }
}
*/
/*
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
*/


