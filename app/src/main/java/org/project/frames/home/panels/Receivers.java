package org.project.frames.home.panels;

import org.project.models.Receiver;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Receivers extends JPanel {
    private static final int RECEIVER_MAX_WIDTH = 1000;
    private static final int RECEIVER_HEIGHT = 50;
    private List<Receiver> receivers;
    private JPanel receiversPanel;
    private JPanel mainPanel;

    public Receivers() {
        this.receivers = ReceiverService.getAllReceivers();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        
        mainPanel = new JPanel(new BorderLayout());
        
        receiversPanel = new JPanel();
        receiversPanel.setLayout(null);
        addReceivers();

        JScrollPane scrollPane = new JScrollPane(receiversPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setPreferredSize(new Dimension(1000, 50));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(1000, 50));
        styleAddButton(addButton);
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        JPanel buttonWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapperPanel.add(buttonPanel);
        mainPanel.add(buttonWrapperPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    private void addReceivers() {
        for (Receiver receiver : receivers) {
            receiversPanel.add(createReceiverPanel(receiver));
        }
    }

    private JPanel createReceiverPanel(Receiver receiver) {
        JPanel receiverPanel = new JPanel(new BorderLayout());
        receiverPanel.setPreferredSize(new Dimension(RECEIVER_MAX_WIDTH, RECEIVER_HEIGHT));
        receiverPanel.setBackground(new Color(100, 150, 100, 255));

        JLabel receiverLabel = new JLabel("Receiver: " + receiver.getName() + " " + receiver.getSurname());
        receiverLabel.setHorizontalAlignment(SwingConstants.LEFT);
        receiverPanel.add(receiverLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        styleButton(detailsButton);
        styleButton(updateButton);
        styleButton(deleteButton, new Color(255, 69, 0));

        detailsButton.addActionListener(e -> showDetailsDialog(receiver));
        updateButton.addActionListener(e -> showUpdateDialog(receiver));
        deleteButton.addActionListener(e -> {
            ReceiverService.delete(receiver.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        receiverPanel.add(buttonPanel, BorderLayout.EAST);

        return receiverPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        if (receiversPanel != null) {
            int y = 0;
            int x = (receiversPanel.getWidth() - RECEIVER_MAX_WIDTH) / 2;

            for (Component component : receiversPanel.getComponents()) {
                component.setBounds(x, y * (RECEIVER_HEIGHT + 10) + RECEIVER_HEIGHT / 4, RECEIVER_MAX_WIDTH, RECEIVER_HEIGHT);
                y++;
            }
            receiversPanel.setPreferredSize(new Dimension(
                RECEIVER_MAX_WIDTH,
                (y * (RECEIVER_HEIGHT + 10)) + RECEIVER_HEIGHT
            ));
        }
    }

    private void showDetailsDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Receiver Details", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);

        ReceiverDetail receiverDetail = new ReceiverDetail();
        receiverDetail.setReceiver(receiver);

        dialog.add(receiverDetail);
        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Receiver", true);
        dialog.setSize(600, 300);
        dialog.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        
        JTextField emailField = new JTextField();
        JTextField receiverNameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField addressField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 30, 80, 25);
        emailField.setBounds(120, 30, 400, 30);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 80, 80, 25);
        receiverNameField.setBounds(120, 80, 400, 30);

        JLabel surnameLabel = new JLabel("Surname:");
        surnameLabel.setBounds(30, 130, 80, 25);
        surnameField.setBounds(120, 130, 400, 30);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(30, 180, 80, 25);
        addressField.setBounds(120, 180, 400, 30);

        mainPanel.add(emailLabel);
        mainPanel.add(emailField);
        mainPanel.add(nameLabel);
        mainPanel.add(receiverNameField);
        mainPanel.add(surnameLabel);
        mainPanel.add(surnameField);
        mainPanel.add(addressLabel);
        mainPanel.add(addressField);

        JButton addButton = new JButton("Add Receiver");
        addButton.setBounds(240, 230, 120, 35);
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);

        addButton.addActionListener(e -> {
            String email = emailField.getText();
            String name = receiverNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            if (!email.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
                Receiver receiver = new Receiver(email, name, surname, address);
                ReceiverService.add(receiver);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(addButton);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Update Receiver", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField emailField = new JTextField(receiver.getEmail());
        JTextField receiverNameField = new JTextField(receiver.getName());
        JTextField surnameField = new JTextField(receiver.getSurname());
        JTextField addressField = new JTextField(receiver.getAddress());

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(receiverNameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton updateButton = new JButton("Update");
        styleButton(updateButton);
        updateButton.addActionListener(e -> {
            String email = emailField.getText();
            String name = receiverNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            if (!email.isEmpty()  && !name.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
                receiver.setEmail(email);
                receiver.setName(name);
                receiver.setSurname(surname);
                receiver.setAddress(address);
                ReceiverService.update(receiver);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(updateButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refresh() {
        this.receivers = ReceiverService.getAllReceivers();
        receiversPanel.removeAll();
        addReceivers();
        revalidate();
        repaint();
    }
    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
    }
    private void styleAddButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
    }
}