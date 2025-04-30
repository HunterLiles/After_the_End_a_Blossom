package io.github.HunterLiles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.HunterLiles.screens.*;

public class Main extends Game {
    SpriteBatch batch;

    @Override
    public void create() {
        // Initialize the SceneManager
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.initialize(this);

        // Register scenes
        sceneManager.registerScene("stone_age", new StoneAgeScene(this));
        sceneManager.registerScene("ancient_age", new AncientScene(this));

        // Start with the Stone Age scene
        sceneManager.changeScene("stone_age");
    }

    @Override
    public void dispose() {
        super.dispose();

        // Dispose of the SceneManager
        SceneManager.getInstance().dispose();
    }
}
