package org.example.gui;

import org.example.data.JsonRepository;
import org.example.models.Product;
import org.example.models.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    public MainFrame(User user) {
        setTitle("Sipariş Takip Uygulaması");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Siparişler", new OrderPanel());
        tabbedPane.addTab("Ürünler", new ProductPanel());
        tabbedPane.addTab("Müşteriler", new CustomerPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // JSON dosyasının yolu
        String filePath = "src/main/resources/products.json";

        // JsonRepository örneği oluşturma
        JsonRepository<Product> productRepository = new JsonRepository<>(filePath, Product[].class);

        // Yeni bir ürün oluşturma
        Product product = new Product();
        product.setId("1");
        product.setName("Laptop");
        product.setPrice(999.99);

        // Ürünü kaydetme
        productRepository.save(product);

        // Tüm ürünleri listeleme
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            System.out.println(p.getName() + ": " + p.getPrice());
        }

    }
}
/*
package org.example.gui;

import org.example.data.JsonRepository;
import org.example.models.Product;
import org.example.models.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(User user) {
        setTitle("Sipariş Takip Uygulaması");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Siparişler", new OrderPanel());
        tabbedPane.addTab("Ürünler", new ProductPanel());
        tabbedPane.addTab("Müşteriler", new CustomerPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // JSON dosyasının yolu
        String filePath = "src/main/resources/products.json";

        // Product türü için TypeToken oluşturma
        Type productListType = new TypeToken<List<Product>>() {}.getType();

        // JsonRepository örneği oluşturma
        JsonRepository<Product> productRepository = new JsonRepository<>(filePath, Product.class);

        // Yeni bir ürün oluşturma
        Product product = new Product();
        //product.setId("1");
        product.setName("Laptop");
        product.setPrice(999.99);

        // Ürünü kaydetme
        productRepository.save(product);

        // Tüm ürünleri listeleme
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            System.out.println(p.getName() + ": " + p.getPrice());
        }
        SwingUtilities.invokeLater(() -> {
            User user = null;
            MainFrame frame = new MainFrame(user);
            frame.setVisible(true);
        });
    }
}
*/