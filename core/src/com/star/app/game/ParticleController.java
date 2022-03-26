package com.star.app.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.utils.Assets;

public class ParticleController extends ObjectPool<Particle> {
    public class EffectBuilder {
        public void buildMonsterSplash(float x, float y) {
            for (int i = 0; i < 15; i++) {
                float randomAngle = MathUtils.random(0, 6.28f);
                float randomSpeed = MathUtils.random(0, 50.0f);
                setup(x, y, (float) Math.cos(randomAngle) * randomSpeed, (float) Math.sin(randomAngle) * randomSpeed, 1.2f, 2.0f, 1.8f, 1, 0, 0, 1, 1, 0, 0, 0.2f);
            }
        }

        public void takePowerUpsEffect(PowerUp p) {
            switch (p.getType()) {
                case MEDKIT:
                    for (int i = 0; i < 16; i++) {
                        float angle = 6.28f / 16.0f * i;
                        setup(p.getPosition().x, p.getPosition().y,
                                (float) Math.cos(angle) * 100.0f, (float) Math.sin(angle) * 100.0f,
                                0.8f, 3.0f, 2.5f,
                                0, 1, 0, 1,
                                0.2f, 1, 0, 0.4f);
                    }
                    break;
                case MONEY:
                    for (int i = 0; i < 16; i++) {
                        float angle = 6.28f / 16.0f * i;
                        setup(p.getPosition().x, p.getPosition().y,
                                (float) Math.cos(angle) * 100.0f, (float) Math.sin(angle) * 100.0f,
                                0.8f, 3.0f, 2.5f,
                                1, 1, 0, 1,
                                1, 0.7f, 0, 0.4f);
                    }
                    break;
                case AMMOS:
                    for (int i = 0; i < 16; i++) {
                        float angle = 6.28f / 16.0f * i;
                        setup(p.getPosition().x, p.getPosition().y,
                                (float) Math.cos(angle) * 100.0f, (float) Math.sin(angle) * 100.0f,
                                0.8f, 3.0f, 2.5f,
                                1, 0, 0, 1,
                                1, 0, 0.8f, 0.4f);
                    }
                    break;
            }


        }
    }

    private TextureRegion oneParticle;
    private EffectBuilder effectBuilder;

    public EffectBuilder getEffectBuilder() {
        return effectBuilder;
    }

    public ParticleController() {
        this.oneParticle = Assets.getInstance().getAtlas().findRegion("star16");
        this.effectBuilder = new EffectBuilder();
    }

    @Override
    protected Particle newObject() {
        return new Particle();
    }

    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < activeList.size(); i++) {
            Particle o = activeList.get(i);
            float t = o.getTime() / o.getTimeMax();
            float scale = lerp(o.getSize1(), o.getSize2(), t);
            batch.setColor(lerp(o.getR1(), o.getR2(), t), lerp(o.getG1(), o.getG2(), t),
                    lerp(o.getB1(), o.getB2(), t), lerp(o.getA1(), o.getA2(), t));
            batch.draw(oneParticle, o.getPosition().x - 8, o.getPosition().y - 8,
                    8, 8, 16, 16, scale, scale, 0);
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i = 0; i < activeList.size(); i++) {
            Particle o = activeList.get(i);
            float t = o.getTime() / o.getTimeMax();
            float scale = lerp(o.getSize1(), o.getSize2(), t);
            if (MathUtils.random(0, 300) < 3) {
                scale *= 5;
            }
            batch.setColor(lerp(o.getR1(), o.getR2(), t), lerp(o.getG1(), o.getG2(), t),
                    lerp(o.getB1(), o.getB2(), t), lerp(o.getA1(), o.getA2(), t));
            batch.draw(oneParticle, o.getPosition().x - 8, o.getPosition().y - 8,
                    8, 8, 16, 16, scale, scale, 0);
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setup(float x, float y, float vx, float vy,
                      float timeMax, float size1, float size2,
                      float r1, float g1, float b1, float a1,
                      float r2, float g2, float b2, float a2) {
        Particle item = getActiveElement();
        item.init(x, y, vx, vy, timeMax, size1, size2, r1, g1, b1, a1, r2, g2, b2, a2);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public float lerp(float value1, float value2, float point) {
        return value1 + (value2 - value1) * point;
    }
}
