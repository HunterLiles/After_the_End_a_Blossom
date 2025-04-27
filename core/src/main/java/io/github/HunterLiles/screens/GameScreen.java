package io.github.HunterLiles.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    TextureRegion[] animationFrames;
    Animation animation;
    float Speed = 300.0f;
    float playerX = -100, playerY = -250;
    ParallaxLayer[] layers;
    Camera camera;
    float elapsedTime;

    public GameScreen(Main game) {
        this.game = game;

        //Music
        Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/Movie_Theme.mp3"));
        music.setLooping(true);
        music.play(); }

    //With my current knowledge this is used like the create method?
    @Override public void show() {
        stage = new Stage(new ScreenViewport());
        player = new Texture("images/Player.png");
        TextureRegion[][] tempFrames = TextureRegion.split(player, 350, 407);
        animationFrames = new TextureRegion[77];
        int index = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 11; j++) {
                animationFrames[index++] = tempFrames[j][i]; }}
        animation = new Animation(0.25f, animationFrames);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Setting each parallax layer individually.
        layers = new ParallaxLayer[12];
        layers [0] = new ParallaxLayer(new Texture("images/background/1.png"), 0.1f, true, false);
        layers [1] = new ParallaxLayer(new Texture("images/background/2.png"), 0.2f, true, false);
        layers [2] = new ParallaxLayer(new Texture("images/background/3.png"), 0.3f, true, false);
        layers [3] = new ParallaxLayer(new Texture("images/background/4.png"), 0.4f, true, false);
        layers [4] = new ParallaxLayer(new Texture("images/background/5.png"), 0.5f, true, false);
        layers [5] = new ParallaxLayer(new Texture("images/background/6.png"), 0.6f, true, false);
        layers [6] = new ParallaxLayer(new Texture("images/background/7.png"), 0.7f, true, false);
        layers [7] = new ParallaxLayer(new Texture("images/background/8.png"), 0.8f, true, false);
        layers [8] = new ParallaxLayer(new Texture("images/background/9.png"), 0.9f, true, false);
        layers [9] = new ParallaxLayer(new Texture("images/background/10.png"), 0.9f, true, false);
        layers [10] = new ParallaxLayer(new Texture("images/background/11.png"), 0.11f, true, false);
        layers [11] = new ParallaxLayer(new Texture("images/background/12.png"), 0.12f, true, false);

        for (ParallaxLayer layer : layers) {
            layer.setCamera(camera);
            layer.setPosition(0, 0);
            layer.setSize(50, 25); }}

    @Override public void render(float delta) {
        input();
        logic();
        draw(); }

    //Neat little methods to keep things organized in render.
    //Movement, I still need to add jumping/physics.
    private void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerX -= Gdx.graphics.getDeltaTime() * Speed;
            camera.position.x -= Gdx.graphics.getDeltaTime() * Speed;}
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerX += Gdx.graphics.getDeltaTime() * Speed;
            camera.position.x += Gdx.graphics.getDeltaTime() * Speed;}
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerX -= Gdx.graphics.getDeltaTime() * Speed;
            camera.position.x -= Gdx.graphics.getDeltaTime() * Speed;}
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerX += Gdx.graphics.getDeltaTime() * Speed;
            camera.position.x += Gdx.graphics.getDeltaTime() * Speed;}}

    //Don't know what I'm using this for yet.
    private void logic() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); }

    //Actual drawing of things.
    private void draw() {
        //All actual renderings happen between "begin" and "end".
        batch.begin();
            for (ParallaxLayer layer : layers) { layer.render(batch);}
            batch.draw((Texture) animation.getKeyFrame(elapsedTime, true), playerX, playerY);
        batch.end(); }

    //I shouldn't need to touch any of this.
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { stage.dispose(); }}
