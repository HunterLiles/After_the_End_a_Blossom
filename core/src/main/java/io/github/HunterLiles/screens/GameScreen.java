package io.github.HunterLiles.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.HunterLiles.Main;
import io.github.HunterLiles.logic.ParallaxLayer;

public class GameScreen extends InputAdapter implements Screen {
    Stage stage;
    Main game;
    SpriteBatch batch;
    Texture player;
    float Speed = 200.0f;
    float playerX = 400, playerY = 200;

    public GameScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        //Music
        Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/Movie_Theme.mp3"));
        music.setLooping(true);
        music.play();

        //Why the FLIP is this not working.
        Array<Texture> textures = new Array<>();
        for (int i = 1; i <= 12; i++) {
            textures.add(new Texture("images/Background layers/" + i + ".png"));
            textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat); }

        ParallaxLayer parallaxLayer = new ParallaxLayer(textures);
        parallaxLayer.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        parallaxLayer.setSpeed(0.5);
        stage.addActor(parallaxLayer); }

    @Override public void show() {
        player = new Texture("images/Player.png");
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch(); }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        //All actual renderings happen between "begin" and "end".
        batch.begin();
        input();
        logic();
        draw();
        batch.end();
         }

    //Neat little methods to keep things organized in render.
    //Movement, I still need to add jumping.
    private void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) { playerX -= Gdx.graphics.getDeltaTime() * Speed; }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) { playerX += Gdx.graphics.getDeltaTime() * Speed; }}

    //Don't know what I'm using this for yet.
    private void logic() { }

    //Actual drawing of things.
    private void draw() {
        batch.draw(player, playerX, playerY); }

    //I shouldn't need to touch any of this.
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { stage.dispose(); }}
