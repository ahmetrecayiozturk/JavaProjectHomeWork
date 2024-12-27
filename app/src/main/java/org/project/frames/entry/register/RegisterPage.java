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

        updatePanelPositionToCenter(this, registerPanel);

        add(registerPanel);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = frame.getWidth();
                int h = frame.getHeight();

                setScreenWidth(w);
                setScreenHeight(h);
                updatePanelPositionToCenter(RegisterPage.this, registerPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    private void updatePanelPositionToCenter(JPanel outerPanel, JPanel innerPanel) {
        Dimension outerPanelSize = outerPanel.getPreferredSize();
        Dimension innerPanelSize = innerPanel.getPreferredSize();
        //System.out.println(innerPanelSize+" "+screenheight+" "+screenwidth); //it works even if the panel closed
        int x = (screenwidth - innerPanelSize.width) / 2;
        int y = (screenheight - innerPanelSize.height) / 2;

        innerPanel.setBounds(x, y, innerPanelSize.width, innerPanelSize.height);
    }

    public Integer getScreenWidth() {
        return screenwidth;
    }

    public void setScreenWidth(Integer screenwidth) {
        this.screenwidth = screenwidth;
    }

    public Integer getScreenHeight() {
        return screenheight;
    }

    public void setScreenHeight(Integer screenheight) {
        this.screenheight = screenheight;
    }
}

