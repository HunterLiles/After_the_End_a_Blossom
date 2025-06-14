package io.github.HunterLiles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.HunterLiles.logic.ParallaxLayer;

public class Main extends Game {
    SpriteBatch batch;
    OrthographicCamera camera;
    ParallaxLayer[] layers;
    FitViewport viewport;

    // Runs when the game starts
    @Override public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(8, 5);

        layers =  new ParallaxLayer[12];
        for (int i = 1; i <= 12; i++) {
            layers[i - 1] = new ParallaxLayer(new Texture("images/background/"+ i +".png"), i/20f, true, false);
        }
        for (ParallaxLayer layer : layers) {
            layer.setCamera(camera);
        }
    }

    @Override public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    @Override public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (ParallaxLayer layer : layers) {
            layer.render(batch);
        }
        batch.end();
    }
    @Override public void pause() {}
    @Override public void resume() {}
    // Runs when the game closes
    @Override public void dispose() { super.dispose(); }
}
