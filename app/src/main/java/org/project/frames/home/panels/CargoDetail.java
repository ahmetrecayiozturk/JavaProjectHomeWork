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
    private Cargo cargo;

    public CargoDetail(Frame owner, Cargo cargo) {
        super(owner, "Cargo Details", true);
        this.cargo = cargo;
        initialize();
    }

    private void initialize() {
        setSize(800, 550);
        setLocationRelativeTo(null);

        Order order = OrderService.getOrderById(cargo.getOrderId());
        Receiver receiver = ReceiverService.findReceiverById(order.getReceiverId());
        Product product = ProductService.getProductById(order.getProductId());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    }

    private JPanel createSectionPanel(String title, String[] details) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBorder(BorderFactory.createTitledBorder(title));

        for (String detail : details) {
            JLabel detailLabel = new JLabel(detail);
            detailLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 500));
            sectionPanel.add(detailLabel);
        }

        return sectionPanel;
    }
}
