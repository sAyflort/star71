package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private InfoController infoController;
    private Hero hero;
    private EnemyShip[] enemyShips;
    private Vector2 tempVec;
    private Stage stage;
    private boolean pause;
    private int level;
    private float timer;
    private Music music;


    public float getTimer() {
        return timer;
    }

    public int getLevel() {
        return level;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public Stage getStage() {
        return stage;
    }

    public InfoController getInfoController() {
        return infoController;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public EnemyShip[] getEnemy() {
        return enemyShips;
    }

    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController(this);
        this.particleController = new ParticleController();
        this.powerUpsController = new PowerUpsController(this);
        this.infoController = new InfoController();
        this.hero = new Hero(this);
      //  this.enemy = new EnemyShip(this, 500, 100, level);
        this.tempVec = new Vector2();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        Gdx.input.setInputProcessor(stage);
        this.level = 1;
        generateBigAsteroids(2);
        this.enemyShips = new EnemyShip[]{new EnemyShip(this, 500, 100, level)};

        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.music.setLooping(true);
        this.music.play();

    }

    public void generateBigAsteroids(int count) {
        for (int i = 0; i < count; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-150, 150), MathUtils.random(-150, 150), 1.0f);
        }
    }

    public void update(float dt) {
        if (pause) {
            return;
        }
        timer += dt;
        background.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        particleController.update(dt);
        powerUpsController.update(dt);
        infoController.update(dt);
        hero.update(dt);
        for (int i = 0; i < enemyShips.length; i++) {
            enemyShips[i].update(dt);
        }

        stage.act(dt);
        checkCollisions();
        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
        }
        if (asteroidController.getActiveList().size() == 0) {
            level++;
            generateBigAsteroids(level + 2);
            if (level <= 5) {
                enemyShips = new EnemyShip[]{new EnemyShip(this, 500, 100, level)};
            } else {
                enemyShips = new EnemyShip[level-5+1];
                for (int i = 0; i < enemyShips.length; i++) {
                    enemyShips[i] = new EnemyShip(this, 500, 100, level);
                }
            }
            timer = 0;
        }
    }


    public void checkCollisions() {
        //столкновение астероидов и героя
        collisionsAstShip(hero);
        //столкновение астероидов и врага, пуль и врага
        for (int i = 0; i < enemyShips.length; i++) {
            collisionsAstShip(enemyShips[i]);
            collisionsBulShip(enemyShips[i]);
        }
        // столкновение пуль и героя
        collisionsBulShip(hero);
        //столкновение пуль и астероидов
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    particleController.setup(b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            2.5f, 1.2f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.1f, 1.0f, 0.0f);

                    b.deactivate();
                    if (a.takeDamage(hero.getCurrentWeapon().getDamage())) {
                        hero.addScore(a.getHpMax() * 100);
                        for (int k = 0; k < 3; k++) {
                            powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() * 0.25f);
                        }
                    }
                    break;
                }
            }
        }

        // Столкновение поверапсов и героя
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp pu = powerUpsController.getActiveList().get(i);
            if (hero.getMagneticField().contains(pu.getPosition())) {
                tempVec.set(hero.getPosition()).sub(pu.getPosition()).nor();
                pu.getVelocity().mulAdd(tempVec, 100);
            }

            if (hero.getHitArea().contains(pu.getPosition())) {
                hero.consume(pu);
                particleController.getEffectBuilder().takePowerUpsEffect(pu);
                pu.deactivate();
            }
        }
    }

    public void collisionsAstShip(Ship ship) {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (ship.getHitArea().overlaps(a.getHitArea())) {
                float dst = a.getPosition().dst(ship.getPosition());
                float halfOverLen = (a.getHitArea().radius + ship.getHitArea().radius - dst) / 2.0f;
                tempVec.set(ship.getPosition()).sub(a.getPosition()).nor();
                ship.getPosition().mulAdd(tempVec, halfOverLen);
                a.getPosition().mulAdd(tempVec, -halfOverLen);

                float sumScl = ship.getHitArea().radius * 2 + a.getHitArea().radius;
                ship.getVelocity().mulAdd(tempVec, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tempVec, -200.0f * ship.getHitArea().radius / sumScl);

                if (a.takeDamage(2)) {
                    if(ship instanceof Hero) {
                        hero.addScore(a.getHpMax() * 50);
                    }
                }
                ship.takeDamage(2 * level);
            }
        }
    }

    public void collisionsBulShip (Ship ship) {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            if(ship.getHitArea().contains(b.getPosition())) {
                if (ship instanceof EnemyShip && b.getShip() instanceof EnemyShip ) {
                    continue;
                }
                if (ship instanceof Hero && b.getShip() instanceof Hero)  {
                    continue;
                }
                ship.takeDamage(ship instanceof EnemyShip ? hero.getCurrentWeapon().getDamage() :
                        enemyShips[0].getCurrentWeapon().getDamage());
                b.deactivate();
            }
        }
    }

    public void dispose() {
        background.dispose();
    }
}
