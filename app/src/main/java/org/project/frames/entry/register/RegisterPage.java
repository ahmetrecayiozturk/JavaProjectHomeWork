package org.project.frames.entry.register;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class RegisterPage extends JPanel {
    private int screenwidth;
    private int screenheight;

    public RegisterPage(JFrame frame, CardLayout cardLayout, JPanel cardPanel) {
        setBackground(Color.YELLOW);
        setLayout(null);
        setPreferredSize(new Dimension(400, 500));

        RegisterPanel registerPanel = new RegisterPanel( cardLayout, cardPanel);

        updatePanelPositionToCenter(registerPanel);

        add(registerPanel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                screenwidth = getWidth();
                screenheight = getHeight();
                updatePanelPositionToCenter( registerPanel);
                revalidate();
                repaint();
            }
        });
    }

    private void updatePanelPositionToCenter(JPanel innerPanel) {
        Dimension innerPanelSize = innerPanel.getPreferredSize();
        int x = (screenwidth - innerPanelSize.width) / 2;
        int y = (screenheight - innerPanelSize.height) / 2;

        innerPanel.setBounds(x, y, innerPanelSize.width, innerPanelSize.height);
    }

}

