package io.arsh.models;

import java.awt.Color;

public class Particle {
    private final Color color;
    private Vector position;
    private Vector velocity;

    public Particle(Color color, Vector position, Vector velocity) {
        this.color = color;
        this.position = position;
        this.velocity = velocity;
    }

    public Color getColor() {
        return color;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

}
