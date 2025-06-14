package io.github.HunterLiles.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.HunterLiles.logic.ParallaxLayer;

public class GameScreen extends InputAdapter implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera gameCamera, hudCamera;
    private ParallaxLayer[] layers;
    private BitmapFont font;

    @Override public void show() {
        batch = new SpriteBatch();

        gameCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameCamera.position.set(gameCamera.viewportWidth / 2.0f, gameCamera.viewportHeight / 2.0f, 0.0f);
        hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudCamera.position.set(hudCamera.viewportWidth / 2.0f, hudCamera.viewportHeight / 2.0f, 1.0f);

        Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/Movie_Theme.mp3"));
        music.setLooping(true);
        music.play();

        font = new BitmapFont(Gdx.files.internal("default.fnt"));

        layers = new ParallaxLayer[12];
        for (int i = 1; i <= layers.length; i++) {
            layers[i - 1] = new ParallaxLayer(new Texture("images/background/" + i + ".png"), i/32f, true, true); }

        for (ParallaxLayer layer : layers) { layer.setCamera(gameCamera); }
    }
    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        gameCamera.update();
        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, hudCamera.viewportHeight);
        for (ParallaxLayer layer : layers) { layer.render(batch); }
        input();
        batch.end();
        hudCamera.update();
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, hudCamera.viewportHeight);
        batch.end();
    }

    private void input() {
        int speed = 500;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { Gdx.app.exit(); }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            gameCamera.position.x -= speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            gameCamera.position.x += speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            gameCamera.position.y -= speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            gameCamera.position.y += speed * Gdx.graphics.getDeltaTime();
        }
    }

    //I shouldn't need to touch any of this.
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() {  }}
