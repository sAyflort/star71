package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class EnemyShip extends Ship {
    private Circle fightArea;
    private Vector2 direction;
    private float dirAngle;
    private boolean die;

    public EnemyShip(GameController gc, float enginePower, int hpMax, int level) {
        super(gc, enginePower, hpMax);

        createWeapons();
        if(weapons.length >= level) {
            this.weaponNum = level-1;
        } else {
            this.weaponNum = weapons.length-1;
        }
        this.currentWeapon = weapons[weaponNum];

        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(MathUtils.random(-ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_WIDTH),
                MathUtils.random(-ScreenManager.SCREEN_HEIGHT, ScreenManager.SCREEN_HEIGHT));
        this.fightArea = new Circle(position, 450);
        this.hitArea = new Circle(position, 28);
        this.direction = new Vector2();
    }

    public void render(SpriteBatch batch) {
        if (!die) {
            batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                    64, 64, 1, 1, angle);
        }
    }

    public void update(float dt) {
        //поиск игрока
        if (hp > 0) {
            direction.set(position).scl(-1).add(gc.getHero().getPosition());
            dirAngle = MathUtils.atan2(direction.y, direction.x) * MathUtils.radDeg;
            if ((Math.abs(angle + 180 * dt - dirAngle) < Math.abs(angle - 180 * dt - dirAngle) ||
                    ((angle > 90) && (angle < 180) && (dirAngle < -90) && (dirAngle > -180))) &&
                    !((dirAngle > 90) && (dirAngle < 180) && (angle < -90) && (angle > -180))) {
                angle += 180 * dt;
                if (angle > 180) {
                    angle -= 360;
                }
            } else {
                angle -= 180 * dt;
                if (angle < -180) {
                    angle += 360;
                }
            }

            if (!fightArea.overlaps(gc.getHero().getHitArea())) {
                velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
                velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;

                float bx = position.x + MathUtils.cosDeg(angle + 180) * 25;
                float by = position.y + MathUtils.sinDeg(angle + 180) * 25;

                for (int i = 0; i < 3; i++) {
                    gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                            velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20),
                            0.4f,
                            1.2f, 0.2f,
                            1.0f, 0.5f, 0.0f, 1.0f,
                            1.0f, 1.0f, 1.0f, 0.0f);
                }
            } else {
                tryToFire();
            }

            super.update(dt);
            fightArea.setPosition(position);
        } else {
            die = true;
        }
    }


    private void createWeapons() {
        weapons = new Weapon[]{
                new Weapon(gc, this, 0.2f, 1, 500, 100000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, 90, 10),
                        }),
                new Weapon(gc, this, 0.1f, 1, 700, 100000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, 90, 10),
                        }),
                new Weapon(gc, this, 0.1f, 1, 700, 100000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                        }),
                new Weapon(gc, this, 0.1f, 2, 700, 100000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                        }),
                new Weapon(gc, this, 0.2f, 10, 700, 100000,
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
}
