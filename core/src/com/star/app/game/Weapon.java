package com.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.utils.Assets;

public class Weapon {
    private GameController gc;
    private Ship ship;
    private float firePeriod;
    private int damage;
    private float bulletSpeed;
    private int maxBullets;
    private int curBullets;
    private Sound shootSound;

    private Vector3[] slots;

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public int getCurBullets() {
        return curBullets;
    }

    public Weapon(GameController gc, Ship ship, float firePeriod, int damage,
                  float bulletSpeed, int maxBullets, Vector3[] slots) {
        this.gc = gc;
        this.ship = ship;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBullets = maxBullets;
        this.curBullets = maxBullets;
        this.slots = slots;
        this.shootSound = Assets.getInstance().getAssetManager().get("audio/shoot.mp3");
    }

    public void fire() {
        if (curBullets > 0) {
            curBullets--;
            shootSound.play();

            for (int i = 0; i < slots.length; i++) {
                float x, y, vx, vy;
                x = ship.getPosition().x + slots[i].x * MathUtils.cosDeg(ship.getAngle() + slots[i].y);
                y = ship.getPosition().y + slots[i].x * MathUtils.sinDeg(ship.getAngle() + slots[i].y);
                vx = ship.getVelocity().x + bulletSpeed * MathUtils.cosDeg(ship.getAngle() + slots[i].z);
                vy = ship.getVelocity().y + bulletSpeed * MathUtils.sinDeg(ship.getAngle() + slots[i].z);

                gc.getBulletController().setup(x, y, vx, vy);
            }
        }
    }

    public int addAmmos(int amount) {
        int oldCurBullets = curBullets;
        curBullets += amount;
        if (curBullets > maxBullets) {
            curBullets = maxBullets;
        }
        return curBullets - oldCurBullets;
    }
}
