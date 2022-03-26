package com.star.app.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;

public class InfoText implements Poolable {
    private Color color;
    private StringBuilder text;
    private boolean active;
    private Vector2 position;
    private Vector2 velocity;
    private float time;
    private float maxTime;

    @Override
    public boolean isActive() {
        return active;
    }

    public StringBuilder getText() {
        return text;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public float getLifetime() {
        return time / maxTime;
    }

    public InfoText() {
        this.text = new StringBuilder();
        this.active = false;
        this.position = new Vector2(0.0f, 0.0f);
        this.velocity = new Vector2(10.0f, 50.0f);
        this.time = 0.0f;
        this.maxTime = 1.5f;
        this.color = Color.GREEN;
    }

    public void setup(float x, float y, String text, Color color) {
        this.position.set(x, y);
        this.active = true;
        this.text.setLength(0);
        this.text.append(text);
        this.time = 0.0f;
        this.color = color;
    }

    public void setup(float x, float y, StringBuilder text, Color color) {
        this.position.set(x, y);
        this.active = true;
        this.text.setLength(0);
        this.text.append(text);
        this.time = 0.0f;
        this.color = color;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        time += dt;
        if (time >= maxTime) {
            active = false;
        }
    }
}
