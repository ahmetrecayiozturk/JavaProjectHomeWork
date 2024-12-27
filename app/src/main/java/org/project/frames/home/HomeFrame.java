package org.project.frames.home;

import org.project.frames.home.home.HomePage;
import org.project.frames.home.home.panels.AddProduct;
import org.project.frames.home.home.panels.Inventory;
import org.project.frames.home.home.panels.Orders;
import org.project.frames.home.home.panels.Products;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class HomeFrame extends JFrame {
    public HomeFrame() {
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(500, 400));
        addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        int w = getWidth();
                        int h = getHeight();
                        setTitle(w + "x" + h);
                        revalidate();
                        repaint();
                    }
                }
        );
        HomePage homePage =new HomePage();
        add(homePage);
        setVisible(true);
    }
}





