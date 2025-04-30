package io.github.HunterLiles.screens;

import com.badlogic.gdx.Screen;
import io.github.HunterLiles.Main;

import java.util.HashMap;
import java.util.Map;

// This class helps us manage different game screens and switch between them
// We use the "singleton" pattern so there's only one SceneManager in the game
public class SceneManager {
    // This stores the single instance of SceneManager
    private static SceneManager instance;

    // A reference to our main game
    private Main game;

    // A map that stores all our game screens with their names
    private Map<String, BaseScene> scenes;

    // The name of the screen that's currently showing
    private String activeSceneId;

    // Private constructor so no one can create new SceneManagers
    private SceneManager() {
        scenes = new HashMap<>();
    }

    // This method lets us get the one SceneManager instance
    public static SceneManager getInstance() {
        // If the instance doesn't exist yet, create it
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    // Connect the SceneManager to our main game
    public void initialize(Main game) {
        this.game = game;
    }

    // Add a new screen to our collection
    // sceneId: the name we'll use to refer to this screen
    // scene: the actual screen object
    public void registerScene(String sceneId, BaseScene scene) {
        scenes.put(sceneId, scene);
    }

    // Switch to a different screen
    // sceneId: the name of the screen we want to show
    public void changeScene(String sceneId) {
        // Check if the screen exists
        if (!scenes.containsKey(sceneId)) {
            throw new IllegalArgumentException("Scene not registered: " + sceneId);
        }

        // Get the screen and show it
        BaseScene scene = scenes.get(sceneId);
        game.setScreen(scene);
        activeSceneId = sceneId;
    }

    // Get the screen that's currently showing
    public BaseScene getActiveScene() {
        if (activeSceneId == null) {
            return null;
        }
        return scenes.get(activeSceneId);
    }

    // Get the name of the screen that's currently showing
    public String getActiveSceneId() {
        return activeSceneId;
    }

    // Clean up all screens when the game closes
    public void dispose() {
        // Clean up each screen
        for (BaseScene scene : scenes.values()) {
            scene.dispose();
        }
        // Clear our collection and reset the active screen
        scenes.clear();
        activeSceneId = null;
    }
}
