package io.github.HunterLiles.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import io.github.HunterLiles.effects.PostProcessor;
import io.github.HunterLiles.logic.Level;
import io.github.HunterLiles.logic.PhysicsConstants;
import io.github.HunterLiles.logic.Player;
import io.github.HunterLiles.logic.PlayerMovement;

// Handles all rendering-related functionality
public class RenderingSystem implements Disposable {
    // Rendering constants
    private static final float PLAYER_RENDER_OFFSET_X = 60f;
    private static final float PLAYER_RENDER_OFFSET_Y = 50f;

    // Rendering components
    private SpriteBatch batch;
    private PostProcessor postProcessor;

    // Constructor - creates a new rendering system
    public RenderingSystem(SpriteBatch batch) {
        this.batch = batch;

        // Initialize post-processor
        postProcessor = new PostProcessor(com.badlogic.gdx.Gdx.graphics.getWidth(),
                                         com.badlogic.gdx.Gdx.graphics.getHeight());
    }

    // Begin post-processing
    public void beginPostProcessing() {
        if (postProcessor != null && postProcessor.isEnabled()) {
            postProcessor.begin();
        }
    }

    // End post-processing
    public void endPostProcessing() {
        if (postProcessor != null && postProcessor.isEnabled()) {
            postProcessor.end(batch);
        }
    }

    // Render the background
    public void renderBackground(Level level) {
        if (batch != null && level != null) {
            batch.begin();
            level.renderBackground(batch);
            batch.end();
        }
    }

    // Render the player
    public void renderPlayer(Player player, PlayerMovement playerMovement) {
        if (batch != null && player != null && playerMovement != null) {
            batch.begin();

            // Get the player's position in the physics world
            Vector2 playerPos = playerMovement.getBody().getPosition();

            // Convert physics position to screen position and apply offsets
            player.render(batch,
                         playerPos.x * PhysicsConstants.PPM - PLAYER_RENDER_OFFSET_X,
                         playerPos.y * PhysicsConstants.PPM - PLAYER_RENDER_OFFSET_Y);

            batch.end();
        }
    }

    // Handle window resizing
    public void resize(int width, int height) {
        if (postProcessor != null) {
            postProcessor.resize(width, height);
        }
    }

    // Clean up resources
    @Override
    public void dispose() {
        if (postProcessor != null) {
            postProcessor.dispose();
            postProcessor = null;
        }
    }
}
