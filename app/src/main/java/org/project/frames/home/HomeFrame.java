package org.project.frames.home;

import org.project.frames.home.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private Inventory inventory ;
    private ProductDetail productDetail ;
    private Products products ;
    private Store store;
    private AddProduct addProduct ;
    private Cargos cargos;
    private Receivers receivers;
    private Orders orders ;
    private JPanel menuPanel;
    private int panelWidth;
    private int panelHeight;
    private JButton selectedButton;

    public HomeFrame() {
        setSize(1300, 800);
        setMinimumSize(new Dimension(1300, 800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setTitle("Store Management System");
        createMenuPanel();
        menuPanel.setBackground(Color.BLUE);
        createContentPanel();
        add(menuPanel);
        add(contentPanel);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelWidth = getWidth();
                panelHeight = getHeight();
                menuPanel.setBounds(0, 0, 200, panelHeight);
                contentPanel.setBounds(200, 0, panelWidth-200, panelHeight);
                revalidate();
                repaint();
            }
        });
        setVisible(true);
    }
    private void createContentPanel(){
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBounds(200, 0, panelWidth-200, panelHeight);
        productDetail = new ProductDetail(cardLayout, contentPanel);
        inventory = new Inventory(cardLayout, contentPanel,productDetail);
        addProduct = new AddProduct(cardLayout, contentPanel);
        products = new Products(cardLayout, contentPanel, productDetail,addProduct);
        store = new Store();
        cargos = new Cargos();
        orders = new Orders();
        receivers = new Receivers();

        contentPanel.add(inventory, "Inventory");
        contentPanel.add(products, "Products");
        contentPanel.add(store, "Store");
        contentPanel.add(productDetail, "productDetail");
        contentPanel.add(addProduct, "createProduct");
        contentPanel.add(cargos, "Cargos");
        contentPanel.add(receivers, "Receivers");
        contentPanel.add(orders, "Orders");
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setBounds(0, 0, 200, 600);

        JButton menuItem1 = new JButton("Inventory");
        JButton menuItem3 = new JButton("Products");
        JButton menuItem4 = new JButton("Store");
        JButton menuItem5 = new JButton("Cargos");
        JButton menuItem6 = new JButton("Receivers");
        JButton menuItem7 = new JButton("Orders");

        menuItem1.setBounds(25, 100, 150, 40);
        menuItem3.setBounds(25, 150, 150, 40);
        menuItem4.setBounds(25, 200, 150, 40);
        menuItem5.setBounds(25, 250, 150, 40);
        menuItem6.setBounds(25, 300, 150, 40);
        menuItem7.setBounds(25, 350, 150, 40);

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectButton(menuItem1);
                inventory.refresh();
                cardLayout.show(contentPanel, "Inventory");
            }
        });

        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectButton(menuItem3);
                products.refresh();
                cardLayout.show(contentPanel, "Products");
            }
        });

        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectButton(menuItem4);
                store.refresh();
                cardLayout.show(contentPanel, "Store");
            }
        });

        menuItem5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectButton(menuItem5);
                cargos.refresh();
                cardLayout.show(contentPanel, "Cargos");
            }
        });

        menuItem6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectButton(menuItem6);
                receivers.refresh();
                cardLayout.show(contentPanel, "Receivers");
            }
        });
        menuItem7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectButton(menuItem7);
                orders.refresh();
                cardLayout.show(contentPanel, "Orders");
            }
        });

        menuPanel.add(menuItem1);
        menuPanel.add(menuItem3);
        menuPanel.add(menuItem4);
        menuPanel.add(menuItem5);
        menuPanel.add(menuItem6);
        menuPanel.add(menuItem7);
        setButtonsColor();
        selectButton(menuItem1);
    }
    private void setButtonsColor(){
        for (Component component : menuPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setBackground(Color.BLUE);
                button.setFont(new Font("Arial", Font.BOLD, 15));
                button.setForeground(Color.WHITE);
            }
        }
    }
    private void selectButton(JButton button){
        if(this.selectedButton!=null) {
            this.selectedButton.setBackground(Color.BLUE);
            this.selectedButton.setForeground(Color.WHITE);
        }
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        this.selectedButton=button;
    }
}




