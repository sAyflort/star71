package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.ScreenManager;

public class Ship {
    protected GameController gc;
    protected TextureRegion texture;
    protected Vector2 position;
    protected Vector2 velocity;
    protected float angle;
    protected float enginePower;
    protected float fireTimer;
    protected int hp;
    protected int hpMax;
    protected Circle hitArea;
    protected Weapon currentWeapon;
    protected int weaponNum;
    protected Weapon[] weapons;

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getAngle() {
        return angle;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public Ship(GameController gc, float enginePower, int hpMax) {
        this.gc = gc;
        this.enginePower = enginePower;
        this.hpMax = hpMax;
        this.hp = hpMax;
        this.velocity = new Vector2(0, 0);
        createWeapons();
        this.weaponNum = 0;
        this.currentWeapon = weapons[weaponNum];
    }

    public void update(float dt) {
        fireTimer += dt;
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);

        float stopKoef = 1.0f - dt;
        if (stopKoef < 0.0f) {
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);
        checkBorders();
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }

    private void createWeapons() {
        weapons = new Weapon[]{
                new Weapon(gc, this, 0.2f, 1, 400, 100,
                        new Vector3[]{
                                new Vector3(28, -90, -10),
                                new Vector3(28, 90, 10),
                        }),
                new Weapon(gc, this, 0.2f, 1, 500, 200,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, 90, 10),
                        }),
                new Weapon(gc, this, 0.1f, 1, 700, 500,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, 90, 10),
                        }),
                new Weapon(gc, this, 0.1f, 1, 700, 800,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                        }),
                new Weapon(gc, this, 0.1f, 2, 700, 1000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                        }),
                new Weapon(gc, this, 0.2f, 10, 700, 1000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                                new Vector3(28, -90, -30),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                                new Vector3(28, 90, 30),
                        })
        };
    }

    public void checkBorders() {
        if (position.x < 32) {
            position.x = 32f;
            velocity.x *= -0.5f;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32) {
            position.x = ScreenManager.SCREEN_WIDTH - 32f;
            velocity.x *= -0.5f;
        }
        if (position.y < 32) {
            position.y = 32f;
            velocity.y *= -0.5f;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32f;
            velocity.y *= -0.5f;
        }
    }

    public void tryToFire() {
        if (fireTimer > 0.2) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
    }
}
