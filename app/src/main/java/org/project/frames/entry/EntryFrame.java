package org.project.frames.entry;

import org.project.frames.entry.login.LoginPage;
import org.project.frames.entry.register.RegisterPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class EntryFrame extends JFrame {
    // Constructor to initialize the EntryFrame
    public EntryFrame() {
        // Set the title of the frame
        setTitle("App");
        // Set the default close operation to exit the application
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set the initial size of the frame
        setSize(1000, 800);
        // Set the minimum size of the frame
        setMinimumSize(new Dimension(500, 400));
        // Add a component listener to handle resizing events
        addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        // Get the new width and height of the frame
                        int w = getWidth();
                        int h = getHeight();
                        // Update the title to display the current window size
                        setTitle(w + "x" + h);  // Window size display
                        // Revalidate and repaint the frame to apply changes
                        revalidate();
                        repaint();
                    }
                }
        );

        // Create a CardLayout for switching between login and register pages
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        // Create instances of LoginPage and RegisterPage
        LoginPage loginPage = new LoginPage(this, cardLayout, cardPanel);
        RegisterPage registerPage = new RegisterPage(this, cardLayout, cardPanel);

        // Add the login and register pages to the card panel
        cardPanel.add(loginPage, "loginPage");
        cardPanel.add(registerPage, "registerPage");

        // Add the card panel to the frame
        add(cardPanel);

        // Make the frame visible
        setVisible(true);
    }
}