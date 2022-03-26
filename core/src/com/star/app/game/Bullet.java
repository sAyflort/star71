package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class Bullet implements Poolable {
    private GameController gc;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Bullet(GameController gc) {
        this.gc = gc;
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.active = false;
    }

    public void deactivate() {
        active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < -20 || position.x > ScreenManager.SCREEN_WIDTH + 20 ||
                position.y < -20 || position.y > ScreenManager.SCREEN_HEIGHT + 20) {
            deactivate();
        }

        float bx = position.x ;
        float by = position.y ;

        for (int i = 0; i < 2; i++) {
            gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                    velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20),
                    0.1f,
                    1.5f, 0.2f,
                    0.0f, 0.5f, 1.0f, 1.0f,
                    0.0f, 0.7f, 1.0f, 0.0f);
        }
    }

    public void activate(float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }
}
