package io.github.HunterLiles.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import io.github.HunterLiles.Main;
import io.github.HunterLiles.logic.Level;
import io.github.HunterLiles.logic.PhysicsConstants;
import io.github.HunterLiles.logic.Player;
import io.github.HunterLiles.logic.PlayerMovement;

// Stone Age level scene
public class StoneAgeScene extends BaseScene {
    // Scene constants
    private static final String SCENE_ID = "stone_age";
    private static final String FOOT_SENSOR_ID = "foot";

    // Game state
    private boolean gameStarted = false;
    private Vector2 playerSpawnPosition;

    // Game components
    private Player player;
    private PlayerMovement playerMovement;
    private Level level;

    // Constructor
    public StoneAgeScene(Main game) {
        super(game);
        playerSpawnPosition = new Vector2(5f, 2f);
    }

    // Set player spawn position (used when coming from another level)
    public void setPlayerSpawnPosition(Vector2 position) {
        if (position != null) {
            this.playerSpawnPosition = position;
        }
    }

    @Override
    public void show() {
        // Initialize common components and systems
        initializeCommon();

        // Set initial camera position
        cameraSystem.setInitialPosition(
            playerSpawnPosition.x * PhysicsConstants.PPM,
            Gdx.graphics.getHeight() / 2
        );

        // Create player
        player = new Player("PlayerAnimation", "idle");
        playerMovement = new PlayerMovement(player, camera, physicsSystem.getWorld());
        playerMovement.getBody().setTransform(playerSpawnPosition.x, playerSpawnPosition.y, 0);

        // Set up collision detection for jumping
        physicsSystem.createContactListener(playerMovement, FOOT_SENSOR_ID);

        // Create level
        level = new Level(
            Level.Era.PREHISTORIC,
            "Dawn of Humanity",
            0f,
            0.8f,
            "audio/prehistoric.mp3"
        );
        level.initializeBackgroundLayers(camera);
        level.createGroundBodies(physicsSystem.getWorld(), camera);

        // Create portals to other levels
        createTransitionTriggers();

        // Show level name
        uiManager.showLevelName(level.getName(), level.getEra().toString(), gameStarted);

        // Set input processor
        Gdx.input.setInputProcessor(this);
    }

    // Create portals to other time periods
    private void createTransitionTriggers() {
        // Portal to Ancient era
        transitionSystem.createPortalTrigger(
            500, 100, 100, 100,
            "ancient_age",
            5f, 2f,
            "Portal to Ancient Civilizations",
            false
        );
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin post-processing
        renderingSystem.beginPostProcessing();

        // Update game logic
        updateLogic(delta);

        // Render game elements
        renderGame();

        // End post-processing
        renderingSystem.endPostProcessing();

        // Render UI (not affected by post-processing)
        uiManager.render();

        // Check for scene transitions
        checkTransition();
    }

    // Update game logic
    private void updateLogic(float delta) {
        // Only update gameplay if game has started
        if (gameStarted) {
            // Update physics
            physicsSystem.update(delta);

            // Update player
            playerMovement.update(delta);
            player.update(delta);

            // Update camera
            cameraSystem.update(delta, playerMovement, gameStarted);

            // Update batch projection
            cameraSystem.updateBatchProjection(batch);

            // Check for transitions
            transitionSystem.checkTransitionTriggers(playerMovement);
        } else {
            // Update camera without following player
            cameraSystem.update(delta, null, false);
            cameraSystem.updateBatchProjection(batch);
        }

        // Update UI
        uiManager.update(delta);
    }

    // Render game elements
    private void renderGame() {
        // Render background
        renderingSystem.renderBackground(level);

        // Render player
        renderingSystem.renderPlayer(player, playerMovement);

        // Render physics debug shapes
        physicsSystem.renderDebug(camera);
    }

    @Override
    public boolean keyDown(int keycode) {
        // If game hasn't started, any key press will start it
        if (!gameStarted) {
            gameStarted = true;
            cameraSystem.resetFollowState();
            uiManager.hideLevelName();
            return true;
        }

        return false;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
