package org.project.frames.home.home;

import org.project.frames.home.home.panels.*;
import org.project.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public HomePage() {
        super(new BorderLayout());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel menuPanel = createMenuPanel();
        menuPanel.setBackground(Color.BLUE);

        Inventory inventory = new Inventory(cardLayout, contentPanel);
        Orders orders = new Orders();
        ProductDetail productDetail = new ProductDetail();
        Products products = new Products(cardLayout, contentPanel, productDetail);
        Store store = new Store();
        JPanel addProduct = new AddProduct();
        Cargos cargos = new Cargos();
        ReceiversPanel receiversPanel = new ReceiversPanel();
        OrdersPanel ordersPanel = new OrdersPanel();

        contentPanel.add(inventory, "Inventory");
        contentPanel.add(orders, "Orders");
        contentPanel.add(products, "Products");
        contentPanel.add(store, "Store");
        contentPanel.add(productDetail, "productDetail");
        contentPanel.add(addProduct, "createProduct");
        contentPanel.add(cargos, "Cargos");
        contentPanel.add(receiversPanel, "Receivers");
        contentPanel.add(ordersPanel, "OrdersPanel");
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JButton menuItem1 = new JButton("Inventory");
        JButton menuItem2 = new JButton("Orders");
        JButton menuItem3 = new JButton("Products");
        JButton menuItem4 = new JButton("Store");
        JButton menuItem5 = new JButton("Cargos");
        JButton menuItem6 = new JButton("Receivers");
        JButton menuItem7 = new JButton("OrdersPanel");

        menuItem1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem6.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem7.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Inventory");
            }
        });

        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Orders");
            }
        });

        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Products");
            }
        });

        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Store");
            }
        });

        menuItem5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Cargos");
            }
        });

        menuItem6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Receivers");
            }
        });
        menuItem7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "OrdersPanel");
            }
        });


        menuPanel.add(menuItem1);
        menuPanel.add(menuItem2);
        menuPanel.add(menuItem3);
        menuPanel.add(menuItem4);
        menuPanel.add(menuItem5);
        menuPanel.add(menuItem6);
        menuPanel.add(menuItem7);
        return menuPanel;
    }
}