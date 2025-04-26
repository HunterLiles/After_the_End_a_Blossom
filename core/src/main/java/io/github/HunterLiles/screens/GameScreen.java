package io.github.HunterLiles.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.HunterLiles.Main;
import io.github.HunterLiles.logic.ParallaxLayer;

public class GameScreen extends InputAdapter implements Screen {
    Stage stage;
    Main game;
    SpriteBatch batch;
    Texture player;
    float Speed = 300.0f;
    float playerX = 0, playerY = 0;
    ParallaxLayer[] layers = new ParallaxLayer[12];
    Camera camera;
    Viewport viewport;
    private static final int WORLD_WIDTH = Gdx.graphics.getWidth(), WORLD_HEIGHT = Gdx.graphics.getHeight();

    public GameScreen(Main game) {
        this.game = game;

        //Music
        Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/Movie_Theme.mp3"));
        music.setLooping(true);
        music.play();

        //Setting each parallax layer individually.
        layers [0] = new ParallaxLayer(new Texture("images/background/1.png"), 0.1f, true, false);
        layers [1] = new ParallaxLayer(new Texture("images/background/2.png"), 0.2f, true, false);
        layers [2] = new ParallaxLayer(new Texture("images/background/3.png"), 0.3f, true, false);
        layers [3] = new ParallaxLayer(new Texture("images/background/4.png"), 0.4f, true, false);
        layers [4] = new ParallaxLayer(new Texture("images/background/5.png"), 0.5f, true, false);
        layers [5] = new ParallaxLayer(new Texture("images/background/6.png"), 0.6f, true, false);
        layers [6] = new ParallaxLayer(new Texture("images/background/7.png"), 0.7f, true, false);
        layers [7] = new ParallaxLayer(new Texture("images/background/8.png"), 0.8f, true, false);
        layers [8] = new ParallaxLayer(new Texture("images/background/9.png"), 0.9f, true, false);
        layers [9] = new ParallaxLayer(new Texture("images/background/10.png"), 0.10f, true, false);
        layers [10] = new ParallaxLayer(new Texture("images/background/11.png"), 0.11f, true, false);
        layers [11] = new ParallaxLayer(new Texture("images/background/12.png"), 0.12f, true, false);

        for (ParallaxLayer layer : layers) { layer.setCamera(camera); }}

    //With my current knowledge this is used like the create method?
    @Override public void show() {
        stage = new Stage(new ScreenViewport());
        player = new Texture("images/Player.png");
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        //All actual renderings happen between "begin" and "end".
        batch.begin();
        input();
        logic();
        draw();
        batch.end(); }

    //Neat little methods to keep things organized in render.
    //Movement, I still need to add jumping.
    private void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) { playerX -= Gdx.graphics.getDeltaTime() * Speed; }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) { playerX += Gdx.graphics.getDeltaTime() * Speed; }}

    //Don't know what I'm using this for yet.
    private void logic() { }

    //Actual drawing of things.
    private void draw() {
        batch.draw(player, playerX, playerY);}

    //I shouldn't need to touch any of this.
    @Override public void resize(int width, int height) {
        camera.viewportWidth = WORLD_WIDTH;
        camera.viewportHeight = (float) (WORLD_HEIGHT * height) / width;
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { stage.dispose(); }}
