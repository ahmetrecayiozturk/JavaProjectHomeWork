package org.project.frames.home.home.panels;

import org.project.models.Receiver;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class ReceiversPanel extends JPanel {
    private int numRows;
    private static final int RECEIVER_MAX_WIDTH = 1000;
    private static final int RECEIVER_HEIGHT = 50;
    private final ReceiverService receiverService = new ReceiverService();
    private List<Receiver> receivers;

    public ReceiversPanel() {
        this.receivers = receiverService.getAllReceivers();
        initialize();
    }

    private void initialize() {
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumRows();
                revalidate();
                repaint();
            }
        });
        addReceivers();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateNumRows() {
        int panelHeight = getHeight();
        numRows = Math.max(1, panelHeight / (RECEIVER_HEIGHT + 10));
    }

    private void addReceivers() {
        for (Receiver receiver : receivers) {
            add(createReceiverPanel(receiver));
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

        detailsButton.addActionListener(e -> showDetailsDialog(receiver));
        updateButton.addActionListener(e -> showUpdateDialog(receiver));
        deleteButton.addActionListener(e -> {
            receiverService.delete(receiver.getId());
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
        int y = 0;
        int x = (getWidth() - RECEIVER_MAX_WIDTH) / 2;

        for (Component component : getComponents()) {
            component.setBounds(x, y * (RECEIVER_HEIGHT + 10) + RECEIVER_HEIGHT / 4, RECEIVER_MAX_WIDTH, RECEIVER_HEIGHT);
            y++;
        }
    }

    private void showDetailsDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Receiver Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(receiver.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Receiver", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField emailField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField receiverNameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField addressField = new JTextField();

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(receiverNameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = receiverNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
                Receiver receiver = new Receiver(email, password, name, surname, address);
                receiverService.add(receiver);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Update Receiver", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField emailField = new JTextField(receiver.getEmail());
        JTextField passwordField = new JTextField(receiver.getPassword());
        JTextField receiverNameField = new JTextField(receiver.getName());
        JTextField surnameField = new JTextField(receiver.getSurname());
        JTextField addressField = new JTextField(receiver.getAddress());

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(receiverNameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = receiverNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
                receiver.setEmail(email);
                receiver.setPassword(password);
                receiver.setName(name);
                receiver.setSurname(surname);
                receiver.setAddress(address);
                receiverService.update(receiver);
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
        this.receivers = receiverService.getAllReceivers();
        removeAll();
        initialize();
    }
}
/*
package org.project.frames.home.home.panels;

import org.project.models.Receiver;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class ReceiversPanel extends JPanel {
    private int numRows;
    private static final int RECEIVER_MAX_WIDTH = 1000;
    private static final int RECEIVER_HEIGHT = 50;
    private final ReceiverService receiverService = new ReceiverService();
    private List<Receiver> receivers;

    public ReceiversPanel() {
        this.receivers = receiverService.getAllReceivers();
        initialize();
    }

    private void initialize() {
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumRows();
                revalidate();
                repaint();
            }
        });
        addReceivers();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateNumRows() {
        int panelHeight = getHeight();
        numRows = Math.max(1, panelHeight / (RECEIVER_HEIGHT + 10));
    }

    private void addReceivers() {
        for (Receiver receiver : receivers) {
            add(createReceiverPanel(receiver));
        }
    }

    private JPanel createReceiverPanel(Receiver receiver) {
        JPanel receiverPanel = new JPanel();
        receiverPanel.setPreferredSize(new Dimension(RECEIVER_MAX_WIDTH, RECEIVER_HEIGHT));
        receiverPanel.setBackground(new Color(100, 150, 100, 255));

        JLabel receiverLabel = new JLabel("Receiver: " + receiver.getName() + " " + receiver.getSurname());
        receiverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        receiverPanel.add(receiverLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.addActionListener(e -> showDetailsDialog(receiver));
        updateButton.addActionListener(e -> showUpdateDialog(receiver));
        deleteButton.addActionListener(e -> {
            receiverService.delete(receiver.getId());
            refresh();
        });

        receiverPanel.add(detailsButton);
        receiverPanel.add(updateButton);
        receiverPanel.add(deleteButton);

        return receiverPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int y = 0;
        int x = (getWidth() - RECEIVER_MAX_WIDTH) / 2;

        for (Component component : getComponents()) {
            component.setBounds(x, y * (RECEIVER_HEIGHT + 10) + RECEIVER_HEIGHT / 4, RECEIVER_MAX_WIDTH, RECEIVER_HEIGHT);
            y++;
        }
    }

    private void showDetailsDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Receiver Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(receiver.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Receiver", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField emailField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField receiverNameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField addressField = new JTextField();

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(receiverNameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = receiverNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
                Receiver receiver = new Receiver(email, password, name, surname, address);
                receiverService.add(receiver);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Update Receiver", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField emailField = new JTextField(receiver.getEmail());
        JTextField passwordField = new JTextField(receiver.getPassword());
        JTextField receiverNameField = new JTextField(receiver.getName());
        JTextField surnameField = new JTextField(receiver.getSurname());
        JTextField addressField = new JTextField(receiver.getAddress());

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(receiverNameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = receiverNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
                receiver.setEmail(email);
                receiver.setPassword(password);
                receiver.setName(name);
                receiver.setSurname(surname);
                receiver.setAddress(address);
                receiverService.update(receiver);
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
        this.receivers = receiverService.getAllReceivers();
        removeAll();
        initialize();
    }
}
*/
/*
package org.project.frames.home.home.panels;

import org.project.models.Cargo;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.services.CargoService;
import org.project.services.OrderService;
import org.project.services.ReceiverService;
import org.project.data.JsonRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReceiversPanel extends JPanel {
    private List<Receiver> receivers;
    private JPanel receiversListPanel;
    private ReceiverService receiverService=new ReceiverService();

    public ReceiversPanel() {
        this.receivers = receiverService.getAllReceivers();
        setLayout(new BorderLayout());

        receiversListPanel = new JPanel(new GridBagLayout());
        loadReceivers();

        JScrollPane scrollPane = new JScrollPane(receiversListPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> showAddDialog());

        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadReceivers() {
        receiversListPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH; // Kartların üstten başlamasını sağlar
        gbc.insets = new Insets(10, 10, 10, 10);

        for (Receiver receiver : receivers) {
            receiversListPanel.add(createReceiverPanel(receiver), gbc);
            gbc.gridy++;
        }
        revalidate();
        repaint();
    }

    private JPanel createReceiverPanel(Receiver receiver) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("Receiver: " + receiver.getName() + " " + receiver.getSurname());
        JTextArea contentArea = new JTextArea(receiver.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        infoPanel.add(titleLabel);
        infoPanel.add(new JScrollPane(contentArea));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton detailsButton = new JButton("Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        detailsButton.addActionListener(e -> showDetailsDialog(receiver));
        updateButton.addActionListener(e -> showUpdateDialog(receiver));
        deleteButton.addActionListener(e -> {
            receiverService.delete(receiver.getId());
            refresh();
        });

        buttonPanel.add(detailsButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showDetailsDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Receiver Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(receiver.toString());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Receiver", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField emailField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField receiverNameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField addressField = new JTextField();

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(receiverNameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = receiverNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
                Receiver receiver = new Receiver(email, password, name, surname, address);
                receiverService.add(receiver);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(Receiver receiver) {
        JDialog dialog = new JDialog((Frame) null, "Update Receiver", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField emailField = new JTextField(receiver.getEmail());
        JTextField passwordField = new JTextField(receiver.getPassword());
        JTextField receiverNameField = new JTextField(receiver.getName());
        JTextField surnameField = new JTextField(receiver.getSurname());
        JTextField addressField = new JTextField(receiver.getAddress());

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(receiverNameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = receiverNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
                receiver.setEmail(email);
                receiver.setPassword(password);
                receiver.setName(name);
                receiver.setSurname(surname);
                receiver.setAddress(address);
                receiverService.update(receiver);
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
        this.receivers = receiverService.getAllReceivers();
        loadReceivers();
    }
}*/