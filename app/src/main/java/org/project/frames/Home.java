package org.project.frames;

import org.project.pages.home.HomePage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Home extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public Home() {
        setSize(1000, 800);
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
        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel menuPanel = createMenuPanel();
        menuPanel.setBackground(Color.BLUE);

        HomePage homePage = new HomePage(this, cardLayout, contentPanel);

        contentPanel.add(homePage, "HomePage");

        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JButton menuItem1 = new JButton("Home Page");

        menuItem1.addActionListener(e -> cardLayout.show(contentPanel, "HomePage"));

        menuPanel.add(menuItem1);

        return menuPanel;
    }
}
/*
package org.project.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    public Home() {
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(500, 400));
        addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e){
                        int w = getWidth();
                        int h = getHeight();
                        setTitle(w+"x"+h);
                        revalidate();
                        repaint();
                    }
                }
        );
        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel menuPanel = createMenuPanel();
        menuPanel.setBackground(Color.BLUE);

        JPanel panel1 = new JPanel();
        JLabel label1 = new JLabel("Panel 1");
        panel1.add(label1);

        JPanel panel2 = new JPanel();
        JLabel label2 = new JLabel("Panel 2");
        panel2.add(label2);

        JPanel panel3 = new JPanel();
        JLabel label3 = new JLabel("Panel 3");
        panel3.add(label3);

        contentPanel.add(panel1, "Panel 1");
        contentPanel.add(panel2, "Panel 2");
        contentPanel.add(panel3, "Panel 3");

        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JButton menuItem1 = new JButton("panel 1");
        JButton menuItem2 = new JButton("panel 2");
        JButton menuItem3 = new JButton("panel 3");

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Panel 1");
            }
        });

        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Panel 2");
            }
        });

        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Panel 3");
            }
        });

        menuPanel.add(menuItem1);
        menuPanel.add(menuItem2);
        menuPanel.add(menuItem3);

        return menuPanel;
    }


}
*/




