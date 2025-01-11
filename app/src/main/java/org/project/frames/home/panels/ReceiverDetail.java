package org.project.frames.home.panels;

import org.project.models.Receiver;

import javax.swing.*;

public class ReceiverDetail extends JPanel {
    private JLabel receiverIdLabel;
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel addressLabel;
    private JLabel emailLabel;
    private Receiver receiver;

    public ReceiverDetail() {
        receiver = new Receiver( "", "", "", "");
        initialize();
    }

    public void initialize() {
        setLayout(null);
        setBounds(0, 0, 500, 300);

        receiverIdLabel = new JLabel("Receiver ID: " + receiver.getId());
        receiverIdLabel.setBounds(50, 40, 400, 30);
        add(receiverIdLabel);

        nameLabel = new JLabel("Name: " + receiver.getName());
        nameLabel.setBounds(50, 80, 400, 30);
        add(nameLabel);

        surnameLabel = new JLabel("Surname: " + receiver.getSurname());
        surnameLabel.setBounds(50, 120, 400, 30);
        add(surnameLabel);

        addressLabel = new JLabel("Address: " + receiver.getAddress());
        addressLabel.setBounds(50, 160, 400, 30);
        add(addressLabel);

        emailLabel = new JLabel("Email: " + receiver.getEmail());
        emailLabel.setBounds(50, 200, 400, 30);
        add(emailLabel);

        revalidate();
        repaint();
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
        receiverIdLabel.setText("Receiver ID: " + receiver.getId());
        nameLabel.setText("Name: " + receiver.getName());
        surnameLabel.setText("Surname: " + receiver.getSurname());
        addressLabel.setText("Address: " + receiver.getAddress());
        emailLabel.setText("Email: " + receiver.getEmail());
        revalidate();
        repaint();
    }
}