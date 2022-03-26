package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Asteroid implements Poolable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int hpMax;
    private int hp;
    private float angle;
    private float rotationSpeed;
    private Circle hitArea;
    private float scale;

    private final float BASE_SIZE = 256.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public float getScale() {
        return scale;
    }

    public int getHpMax() {
        return hpMax;
    }

    public Circle getHitArea() {
        return hitArea;
    }

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

    public Asteroid(GameController gc) {
        this.gc = gc;
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.texture = Assets.getInstance().getAtlas().findRegion("asteroid");
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,
                256, 256, scale, scale, angle);
    }

    public void deactivate() {
        active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;

        if (position.x < -hitArea.radius) {
            position.x = ScreenManager.SCREEN_WIDTH + hitArea.radius;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + hitArea.radius) {
            position.x = -hitArea.radius;
        }
        if (position.y < -hitArea.radius) {
            position.y = ScreenManager.SCREEN_HEIGHT + hitArea.radius;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + hitArea.radius) {
            position.y = -hitArea.radius;
        }
        hitArea.setPosition(position);
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        hpMax = (int) ((gc.getLevel() * 3 + 7) * scale);
        hp = hpMax;
        angle = MathUtils.random(0.0f, 360.0f);
        rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        hitArea.setPosition(x, y);
        this.scale = scale;
        hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            if (scale > 0.41f) {
                for (int i = 0; i < 5; i++) {
                    if (MathUtils.random() < 0.5) {
                        gc.getAsteroidController().setup(position.x, position.y,
                                MathUtils.random(-150, 150), MathUtils.random(-150, 150), scale - 0.3f);
                    }
                }
//                gc.getAsteroidController().setup(position.x, position.y,
//                        MathUtils.random(-150, 150), MathUtils.random(-150, 150), scale - 0.3f);
//                gc.getAsteroidController().setup(position.x, position.y,
//                        MathUtils.random(-150, 150), MathUtils.random(-150, 150), scale - 0.3f);
//                gc.getAsteroidController().setup(position.x, position.y,
//                        MathUtils.random(-150, 150), MathUtils.random(-150, 150), scale - 0.3f);
            }
            return true;
        } else {
            return false;
        }
    }
}
