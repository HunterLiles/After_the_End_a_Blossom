package io.github.HunterLiles.screens;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.HunterLiles.Main;
import io.github.HunterLiles.systems.CameraSystem;
import io.github.HunterLiles.systems.PhysicsSystem;
import io.github.HunterLiles.systems.RenderingSystem;
import io.github.HunterLiles.systems.TransitionSystem;
import io.github.HunterLiles.systems.UIManager;

// Base class for all game screens with common functionality
public abstract class BaseScene extends InputAdapter implements Screen {
    // Game reference
    protected Main game;

    // Core components
    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected Stage stage;

    // System managers
    protected PhysicsSystem physicsSystem;
    protected UIManager uiManager;
    protected RenderingSystem renderingSystem;
    protected CameraSystem cameraSystem;
    protected TransitionSystem transitionSystem;

    // Scene transition variables
    protected String nextSceneId;
    protected boolean transitionPending;

    // Constructor - creates a new screen
    public BaseScene(Main game) {
        this.game = game;
        this.transitionPending = false;
    }

    // Initialize common components and systems
    protected void initializeCommon() {
        // Create core components
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        stage = new Stage(new FitViewport(camera.viewportWidth, camera.viewportHeight));

        // Initialize systems
        physicsSystem = new PhysicsSystem();
        cameraSystem = new CameraSystem(camera);
        renderingSystem = new RenderingSystem(batch);
        uiManager = new UIManager(stage);
        transitionSystem = new TransitionSystem(this);
    }

    // Start changing to a different screen
    // sceneId: the name of the screen to change to
    public void transitionTo(String sceneId) {
        this.nextSceneId = sceneId;
        this.transitionPending = true;
    }

    // Check if we need to change screens and do it if necessary
    // This should be called in the render method
    protected void checkTransition() {
        if (transitionPending && nextSceneId != null) {
            // Use the SceneManager to change to the new screen
            SceneManager.getInstance().changeScene(nextSceneId);
            transitionPending = false;
            nextSceneId = null;
        }
    }

    // Handle window resizing
    @Override
    public void resize(int width, int height) {
        // Update stage viewport
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }

        // Update camera
        if (camera != null) {
            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update();
        }

        // Update systems
        if (renderingSystem != null) {
            renderingSystem.resize(width, height);
        }
    }

    // Called when the game is paused (like when minimized)
    @Override
    public void pause() {
        // We don't need to do anything here for now
    }

    // Called when the game resumes after being paused
    @Override
    public void resume() {
        // We don't need to do anything here for now
    }

    // Called when this screen is no longer visible
    @Override
    public void hide() {
        // We don't need to do anything here for now
    }

    // Clean up resources when the screen is no longer needed
    @Override
    public void dispose() {
        // Dispose core components
        if (batch != null) {
            batch.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }

        // Dispose systems
        if (physicsSystem != null) {
            physicsSystem.dispose();
        }
        if (renderingSystem != null) {
            renderingSystem.dispose();
        }
        if (uiManager != null) {
            uiManager.dispose();
        }
    }
}
