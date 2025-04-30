package io.github.HunterLiles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.HunterLiles.screens.*;
import io.github.HunterLiles.screens.MedievalScene;

public class Main extends Game {
    SpriteBatch batch;

    // Runs when the game starts
    @Override
    public void create() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.initialize(this);

        // Add our game screens to the SceneManager
        sceneManager.registerScene("stone_age", new StoneAgeScene(this));
        sceneManager.registerScene("ancient_age", new AncientScene(this));
        sceneManager.registerScene("medieval_age", new MedievalScene(this));

        // Start the game with the Stone Age screen
        sceneManager.changeScene("stone_age");
    }

    // Runs when the game closes
    @Override
    public void dispose() {
        super.dispose();

        // Clean up the SceneManager to prevent memory leaks
        SceneManager.getInstance().dispose();
    }
}
