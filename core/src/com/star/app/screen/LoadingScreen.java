package com.star.app.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.utils.Assets;

public class LoadingScreen extends AbstractScreen {
    private Texture texture;

    public LoadingScreen(SpriteBatch batch) {
        super(batch);
        Pixmap pixmap = new Pixmap(1280, 20, Pixmap.Format.RGB888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        this.texture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        if (Assets.getInstance().getAssetManager().update()) {
            Assets.getInstance().makeLinks();
            ScreenManager.getInstance().goToTarget();
        }
        batch.begin();
        batch.draw(texture, 0, 50, 1280 *
                Assets.getInstance().getAssetManager().getProgress(), 20);
        batch.end();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
