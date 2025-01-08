package org.project.frames.entry.register;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class RegisterPage extends JPanel {
    // Variables to store the screen width and height
    private int screenwidth;
    private int screenheight;

    // Constructor to initialize the RegisterPage
    public RegisterPage(JFrame frame, CardLayout cardLayout, JPanel cardPanel) {
        // Set the background color of the panel
        setBackground(new Color(227, 214, 214));
        // Set the layout manager to null for absolute positioning
        setLayout(null);
        // Set the preferred size of the panel
        setPreferredSize(new Dimension(400, 500));

        // Create an instance of RegisterPanel
        RegisterPanel registerPanel = new RegisterPanel(cardLayout, cardPanel);

        // Center the registerPanel within the RegisterPage
        updatePanelPositionToCenter(this, registerPanel);

        // Add the registerPanel to the RegisterPage
        add(registerPanel);

        // Add a component listener to the frame to handle resizing
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Get the new width and height of the frame
                int w = frame.getWidth();
                int h = frame.getHeight();

                // Update the screen width and height
                setScreenWidth(w);
                setScreenHeight(h);
                // Center the registerPanel within the RegisterPage
                updatePanelPositionToCenter(RegisterPage.this, registerPanel);
                // Revalidate and repaint the frame to apply changes
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    // Method to center the innerPanel within the outerPanel
    private void updatePanelPositionToCenter(JPanel outerPanel, JPanel innerPanel) {
        // Get the preferred sizes of the outer and inner panels
        Dimension outerPanelSize = outerPanel.getPreferredSize();
        Dimension innerPanelSize = innerPanel.getPreferredSize();
        // Calculate the x and y coordinates to center the innerPanel
        int x = (screenwidth - innerPanelSize.width) / 2;
        int y = (screenheight - innerPanelSize.height) / 2;

        // Set the bounds of the innerPanel to center it
        innerPanel.setBounds(x, y, innerPanelSize.width, innerPanelSize.height);
    }

    // Getter method for screen width
    public Integer getScreenWidth() {
        return screenwidth;
    }

    // Setter method for screen width
    public void setScreenWidth(Integer screenwidth) {
        this.screenwidth = screenwidth;
    }

    // Getter method for screen height
    public Integer getScreenHeight() {
        return screenheight;
    }

    // Setter method for screen height
    public void setScreenHeight(Integer screenheight) {
        this.screenheight = screenheight;
    }
}