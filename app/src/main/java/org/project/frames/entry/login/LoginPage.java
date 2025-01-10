package org.project.frames.entry.login;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class LoginPage extends JPanel {
    private int screenwidth;
    private int screenheight;

    public LoginPage(JFrame frame, CardLayout cardLayout, JPanel cardPanel) {
        setBackground(Color.YELLOW);
        setLayout(null);
        setPreferredSize(new Dimension(400, 500));

        LoginPanel loginPanel = new LoginPanel( frame,cardLayout, cardPanel);

        updatePanelPositionToCenter(loginPanel);

        add(loginPanel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                screenwidth = getWidth();
                screenheight = getHeight();
                updatePanelPositionToCenter(loginPanel);
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
