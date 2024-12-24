package org.project.panels;

import org.project.models.Cargo;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CargosPanel extends JPanel {
    private CargoService cargoService;
    private List<Cargo> cargos;

    public CargosPanel(CargoService cargoService) {
        this.cargoService = cargoService;
        this.cargos = cargoService.getAllCargos();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadCargos();
    }

    private void loadCargos() {
        for (Cargo cargo : cargos) {
            addCard(cargo);
        }
    }

    private void addCard(Cargo cargo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Cargo ID: " + cargo.getId());
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(cargo));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Cargo Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}
/*package org.project.components;

import org.project.models.Cargo;
import org.project.services.CargoService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CargosPanel extends JPanel {
    private CargoService cargoService;
    private List<Cargo> cargos;

    public CargosPanel(CargoService cargoService) {
        this.cargoService = cargoService;
        this.cargos = cargoService.getAllCargos();
        setLayout(new GridLayout(0, 1, 10, 10));
        loadCargos();
    }

    private void loadCargos() {
        for (Cargo cargo : cargos) {
            addCard(cargo);
        }
    }

    private void addCard(Cargo cargo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel("Cargo ID: " + cargo.getId());
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> showDetailsDialog(cargo));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.SOUTH);

        add(card);
    }

    private void showDetailsDialog(Cargo cargo) {
        JDialog dialog = new JDialog((Frame) null, "Cargo Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        JTextArea contentArea = new JTextArea(cargo.toString());
        contentArea.setEditable(false);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}*/