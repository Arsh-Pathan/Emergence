package io.arsh;

import javax.swing.*;
import java.awt.*;

public class Emergence extends JFrame {

    private final Panel panel;

    public Emergence() {
        panel = new Panel();
        setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setBackground(Color.BLACK);
        add(panel);
        setVisible(true);
        new Timer(8, e -> update()).start();
    }

    public void update() {

        panel.repaint();
    }

    private class Panel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }

    private static class Config {
        public static final int WINDOW_WIDTH = 1280;
        public static final int WINDOW_HEIGHT = 720;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Emergence::new);
    }
}
