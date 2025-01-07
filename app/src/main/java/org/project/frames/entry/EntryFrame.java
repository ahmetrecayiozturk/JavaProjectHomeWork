package org.project.frames.entry;

import org.project.frames.entry.login.LoginPage;
import org.project.frames.entry.register.RegisterPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class EntryFrame extends JFrame {
    public EntryFrame() {
        setTitle("App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(500, 400));
        addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e){
                        int w = getWidth();
                        int h = getHeight();
                        setTitle(w + "x" + h);  // Window size display
                        revalidate();
                        repaint();
                    }
                }
        );
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
