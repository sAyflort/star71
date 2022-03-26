package com.star.app.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.helpers.ObjectPool;

public class InfoController extends ObjectPool<InfoText> {
    @Override
    protected InfoText newObject() {
        return new InfoText();
    }

    public void setup(float x, float y, String text, Color color) {
        InfoText infoText = getActiveElement();
        infoText.setup(x, y, text, color);
    }

    public void setup(float x, float y, StringBuilder text, Color color) {
        InfoText infoText = getActiveElement();
        infoText.setup(x, y, text.toString(), color);
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        for (int i = 0; i < activeList.size(); i++) {
            InfoText infoText = activeList.get(i);
            font.setColor(infoText.getColor());
            font.draw(batch, infoText.getText(), infoText.getPosition().x, infoText.getPosition().y);
        }
        font.setColor(Color.WHITE);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
