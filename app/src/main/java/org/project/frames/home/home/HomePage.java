package org.project.frames.home.home;

import org.project.frames.home.home.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private Inventory inventory;
    private ProductDetail productDetail;
    private Products products;
    private StorePanel store;
    private AddProduct addProduct;
    private Cargos cargos;
    private ReceiversPanel receiversPanel;
    private OrdersPanel ordersPanel;

    public HomePage() {
        super(new BorderLayout());
        JPanel menuPanel = createMenuPanel();
        menuPanel.setBackground(Color.BLUE);
        contentPanel = createContentPanel();

        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        inventory = new Inventory(cardLayout, contentPanel);
        productDetail = new ProductDetail();
        products = new Products(cardLayout, contentPanel, productDetail);
        store = new StorePanel();
        addProduct = new AddProduct(cardLayout, contentPanel);
        cargos = new Cargos();
        receiversPanel = new ReceiversPanel();
        ordersPanel = new OrdersPanel();

        contentPanel.add(inventory, "Inventory");
        contentPanel.add(products, "Products");
        contentPanel.add(store, "Store");
        contentPanel.add(productDetail, "productDetail");
        contentPanel.add(addProduct, "createProduct");
        contentPanel.add(cargos, "Cargos");
        contentPanel.add(receiversPanel, "Receivers");
        contentPanel.add(ordersPanel, "OrdersPanel");

        return contentPanel;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JButton menuItem1 = new JButton("Inventory");
        JButton menuItem3 = new JButton("Products");
        JButton menuItem4 = new JButton("Store");
        JButton menuItem5 = new JButton("Cargos");
        JButton menuItem6 = new JButton("Receivers");
        JButton menuItem7 = new JButton("Orders");

        menuItem1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem6.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem7.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inventory.refresh();
                cardLayout.show(contentPanel, "Inventory");
            }
        });

        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                products.refresh();
                cardLayout.show(contentPanel, "Products");
            }
        });

        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                store.refresh();
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
        menuPanel.add(menuItem3);
        menuPanel.add(menuItem4);
        menuPanel.add(menuItem5);
        menuPanel.add(menuItem6);
        menuPanel.add(menuItem7);

        return menuPanel;
    }
}
/*
package org.project.frames.home.home;

import org.project.frames.home.home.panels.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private Inventory inventory ;
    private ProductDetail productDetail ;
    private Products products ;
    //private Store store;
    private StorePanel store;
    private AddProduct addProduct ;
    private Cargos cargos;
    private ReceiversPanel receiversPanel;
    private OrdersPanel ordersPanel ;

    public HomePage() {
        super(new BorderLayout());
        JPanel menuPanel = createMenuPanel();
        menuPanel.setBackground(Color.BLUE);
        JPanel contentPanel = createContentPanel();

        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
    private JPanel createContentPanel(){
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        inventory = new Inventory(cardLayout, contentPanel);
        //orders = new Orders();
        productDetail = new ProductDetail();
        products = new Products(cardLayout, contentPanel, productDetail);
        //store = new Store();
        store = new StorePanel();
        addProduct = new AddProduct(cardLayout, contentPanel);
        cargos = new Cargos();
        receiversPanel = new ReceiversPanel();
        ordersPanel = new OrdersPanel();

        contentPanel.add(inventory, "Inventory");
        contentPanel.add(products, "Products");
        contentPanel.add(store, "Store");
        contentPanel.add(productDetail, "productDetail");
        contentPanel.add(addProduct, "createProduct");
        contentPanel.add(cargos, "Cargos");
        contentPanel.add(receiversPanel, "Receivers");
        contentPanel.add(ordersPanel, "OrdersPanel");
        return contentPanel;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JButton menuItem1 = new JButton("Inventory");
        JButton menuItem3 = new JButton("Products");
        JButton menuItem4 = new JButton("Store");
        JButton menuItem5 = new JButton("Cargos");
        JButton menuItem6 = new JButton("Receivers");
        JButton menuItem7 = new JButton("Orders");

        menuItem1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem6.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuItem7.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inventory.refresh();
                cardLayout.show(contentPanel, "Inventory");
            }
        });


        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                products.refresh();
                cardLayout.show(contentPanel, "Products");
            }
        });

        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                store.refresh();
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
        menuPanel.add(menuItem3);
        menuPanel.add(menuItem4);
        menuPanel.add(menuItem5);
        menuPanel.add(menuItem6);
        menuPanel.add(menuItem7);
        return menuPanel;
    }
}
*/