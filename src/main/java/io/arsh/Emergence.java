package io.arsh;

import io.arsh.models.Particle;
import io.arsh.models.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Emergence extends JFrame {

    private static final double PARTICLE_RADIUS = 3.0;
    private static final double FRICTION = 0.5;

    private final List<Particle> particles = new ArrayList<>();
    private final List<Rule> rules = new ArrayList<>();

    private final Panel panel = new Panel();

    public Emergence() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        panel.setBackground(Color.BLACK);
        add(panel);

        GraphicsDevice gd = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }

        initParticles();
        initRules();
        new Timer(2, e -> update()).start();
    }

    private void initParticles() {
        addParticles(Color.WHITE, 300);
        addParticles(Color.PINK, 200);
    }

    private void initRules() {
        rules.add(new Rule(Color.WHITE, Color.WHITE, 1));
        rules.add(new Rule(Color.PINK, Color.PINK, 0.8));

        rules.add(new Rule(Color.WHITE, Color.PINK, 0.7));
        rules.add(new Rule(Color.PINK, Color.WHITE, -0.05));

    }

    public void addParticles(Color color, int amount) {
        Random r = new Random();
        for (int i = 0; i < amount; i++) {
            particles.add(new Particle(
                    color,
                    new Vector(r.nextDouble() * 800 - 400, r.nextDouble() * 800 - 400),
                    new Vector(0, 0)
            ));
        }
    }

    private void update() {
        double width = getWidth();
        double height = getHeight();
        double halfW = width / 2.0;
        double halfH = height / 2.0;

        for (int i = 0; i < particles.size(); i++) {
            double totalForceX = 0;
            double totalForceY = 0;
            Particle a = particles.get(i);

            for (int j = 0; j < particles.size(); j++) {
                if (i == j) continue;
                Particle b = particles.get(j);

                double strength = getRuleStrength(a.getColor(), b.getColor());

                double dx = b.getPosition().x - a.getPosition().x;
                double dy = b.getPosition().y - a.getPosition().y;

                if (dx > halfW) dx -= width;
                if (dx < -halfW) dx += width;
                if (dy > halfH) dy -= height;
                if (dy < -halfH) dy += height;

                double d = Math.sqrt(dx * dx + dy * dy);
                if (d > 0 && d < 90) {
                    double force = 0;
                    if (d < 20) {
                        force = -1.0 * (1.0 - d / 20.0);
                    } else {
                        force = strength * (1.0 - Math.abs(d - 55) / 25.0);
                    }
                    totalForceX += (dx / d) * force;
                    totalForceY += (dy / d) * force;
                }
            }

            Vector vel = a.getVelocity();
            vel.x = (vel.x + totalForceX) * FRICTION;
            vel.y = (vel.y + totalForceY) * FRICTION;
        }

        for (Particle p : particles) {
            p.getPosition().x += p.getVelocity().x;
            p.getPosition().y += p.getVelocity().y;

            if (p.getPosition().x < -halfW) p.getPosition().x += width;
            if (p.getPosition().x > halfW)  p.getPosition().x -= width;
            if (p.getPosition().y < -halfH) p.getPosition().y += height;
            if (p.getPosition().y > halfH)  p.getPosition().y -= height;
        }
        panel.repaint();
    }

    private double getRuleStrength(Color a, Color b) {
        for (Rule r : rules) {
            if (r.a == a && r.b == b) return r.strength;
        }
        return 0;
    }

    private class Panel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            for (Particle p : particles) {
                Vector pos = p.getPosition();
                Color c = p.getColor();

                int x = (int) (cx + pos.x);
                int y = (int) (cy - pos.y);

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                float glowRadius = 6f;

                RadialGradientPaint glow = new RadialGradientPaint(
                        new Point(x, y),
                        glowRadius,
                        new float[]{0f, 1f},
                        new Color[]{
                                new Color(c.getRed(), c.getGreen(), c.getBlue(), 120),
                                new Color(c.getRed(), c.getGreen(), c.getBlue(), 0)
                        }
                );

                g2.setPaint(glow);
                g2.fillOval(
                        (int) (x - glowRadius),
                        (int) (y - glowRadius),
                        (int) (glowRadius * 2),
                        (int) (glowRadius * 2)
                );

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_OFF);

                g2.setComposite(AlphaComposite.SrcOver);
                g2.setColor(c);

                int r = (int) PARTICLE_RADIUS;
                g2.fillOval(x - r, y - r, r * 2, r * 2);
            }

            g2.setColor(Color.WHITE);
            g2.drawString("Particles: " + particles.size(), 10, 15);
        }

    }

    private static class Rule {
        Color a; Color b; double strength;
        Rule(Color a, Color b, double strength) {
            this.a = a; this.b = b; this.strength = strength;
        }
    }
}