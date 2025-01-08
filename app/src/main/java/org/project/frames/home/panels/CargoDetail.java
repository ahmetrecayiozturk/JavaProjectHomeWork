package org.project.frames.home.panels;

import org.project.models.Cargo;
import org.project.models.Order;
import org.project.models.Receiver;
import org.project.models.Product;
import org.project.services.OrderService;
import org.project.services.ProductService;
import org.project.services.ReceiverService;

import javax.swing.*;
import java.awt.*;

public class CargoDetail extends JDialog {
    // Cargo object to display details for
    private Cargo cargo;

    // Constructor to initialize the CargoDetail dialog
    public CargoDetail(Frame owner, Cargo cargo) {
        super(owner, "Cargo Details", true);
        this.cargo = cargo;
        initialize();
    }

    // Method to initialize the dialog components
    private void initialize() {
        // Set the size and location of the dialog
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Retrieve the order, receiver, and product details associated with the cargo
        Order order = OrderService.getOrderById(cargo.getOrderId());
        Receiver receiver = ReceiverService.findReceiverById(order.getReceiverId());
        Product product = ProductService.getProductById(order.getProductId());

        // Create the content panel and set its layout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add sections for cargo, order, receiver, and product details
        contentPanel.add(createSectionPanel("Cargo Details", new String[]{
                "Cargo ID: " + cargo.getId(),
                "Is Delivered: " + cargo.isDelivered(),
                "Is Returned: " + cargo.isReturned(),
        }));

        contentPanel.add(createSectionPanel("Order Details", new String[]{
                "Order ID: " + order.getId(),
                "Quantity: " + order.getQuantity()
        }));

        contentPanel.add(createSectionPanel("Receiver Details", new String[]{
                "Name: " + receiver.getName() + " " + receiver.getSurname(),
                "Address: " + receiver.getAddress()
        }));

        contentPanel.add(createSectionPanel("Product Details", new String[]{
                "Name: " + product.getName(),
                "Price: " + product.getPrice(),
                "Description: " + product.getDescription()
        }));

        // Add the content panel to the dialog with a scroll pane
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    }

    // Method to create a section panel with a title and details
    private JPanel createSectionPanel(String title, String[] details) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBorder(BorderFactory.createTitledBorder(title));

        // Add each detail as a label to the section panel
        for (String detail : details) {
            JLabel detailLabel = new JLabel(detail);
            detailLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 500));
            sectionPanel.add(detailLabel);
        }

        return sectionPanel;
    }
}