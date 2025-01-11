package org.project.frames.entry;

import org.project.frames.entry.login.LoginPage;
import org.project.frames.entry.register.RegisterPage;

import javax.swing.*;
import java.awt.*;

public class EntryFrame extends JFrame {
    public EntryFrame() {
        setTitle("Store Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(500, 400));
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        LoginPage loginPage = new LoginPage(this,cardLayout,cardPanel);
        RegisterPage registerPage = new RegisterPage(this,cardLayout,cardPanel);

        cardPanel.add(loginPage, "loginPage");
        cardPanel.add(registerPage, "registerPage");

        add(cardPanel);

        setVisible(true);
    }
}
