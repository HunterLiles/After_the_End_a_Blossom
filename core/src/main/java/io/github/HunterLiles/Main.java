package io.github.HunterLiles;

import com.badlogic.gdx.Game;
import io.github.HunterLiles.screens.GameScreen;

public class Main extends Game {
    // Runs when the game starts
    @Override public void create() { this.setScreen(new GameScreen()); }

    // Runs when the game closes
    @Override
    public void dispose() {
        super.dispose();
    }
}
