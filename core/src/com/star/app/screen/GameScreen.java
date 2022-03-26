package com.star.app.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.GameController;
import com.star.app.game.WorldRenderer;
import com.star.app.screen.utils.Assets;

public class GameScreen extends AbstractScreen{

    private GameController gc;
    private WorldRenderer worldRenderer;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.gc = new GameController(batch);
        this.worldRenderer = new WorldRenderer(gc, batch);
    }

    @Override
    public void render(float delta) {
        gc.update(delta);
        worldRenderer.render();
    }

    @Override
    public void dispose() {
        gc.dispose();
    }
}
