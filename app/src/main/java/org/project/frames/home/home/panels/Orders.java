package org.project.frames.home.home.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Orders extends JPanel {
    private int numRows;
    private static final int PRODUCT_MAX_WIDTH = 1000;
    private static final int PRODUCT_HEIGHT = 50;

    public Orders() {
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateNumRows();
                revalidate();
                repaint();
            }
        });
        addProducts();
    }

    private void updateNumRows() {
        int panelHeight = getHeight();
        numRows = Math.max(1, panelHeight / (PRODUCT_HEIGHT + 10));
    }

    private void addProducts() {
        for (int i = 0; i < 20; i++) {
            add(createProductPanel(i));
        }
    }

    private JPanel createProductPanel(int index) {
        JPanel productPanel = new JPanel();
        productPanel.setPreferredSize(new Dimension(PRODUCT_MAX_WIDTH, PRODUCT_HEIGHT));
        productPanel.setBackground(new Color(100 + (index % 10) * 15, 100, 255));

        JLabel productLabel = new JLabel("Order " + (index + 1));
        productLabel.setHorizontalAlignment(SwingConstants.CENTER);
        productPanel.add(productLabel);

        return productPanel;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int y = 0;
        int x = (getWidth() - PRODUCT_MAX_WIDTH) / 2;

        for (Component component : getComponents()) {

            component.setBounds(x, y * (PRODUCT_HEIGHT + 10)+PRODUCT_HEIGHT/4 , PRODUCT_MAX_WIDTH, PRODUCT_HEIGHT);
            y++;
        }
    }
}
