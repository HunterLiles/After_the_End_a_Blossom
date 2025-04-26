package io.github.HunterLiles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.HunterLiles.screens.GameScreen;

public class Main extends Game {
    SpriteBatch batch;
    public Screen gameScreen;

    @Override public void create() {
        gameScreen = new GameScreen(this);
        setScreen(gameScreen); }}
