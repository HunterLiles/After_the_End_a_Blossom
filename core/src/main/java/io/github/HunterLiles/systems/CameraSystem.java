package io.github.HunterLiles.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.HunterLiles.logic.PhysicsConstants;
import io.github.HunterLiles.logic.PlayerMovement;

// Handles all camera-related functionality
public class CameraSystem {
    // Camera constants
    private static final float CAMERA_LERP = 0.1f;
    private static final float WORLD_WIDTH_MULTIPLIER = 3f;
    private static final float WORLD_HEIGHT_MULTIPLIER = 2f;

    // Camera components
    private OrthographicCamera camera;
    private Vector2 initialCameraPosition;

    // Camera state
    private boolean cameraFollowsPlayer = false;
    private float cameraFollowDelay = 0.5f;
    private float cameraFollowTimer = 0f;

    // Constructor - creates a new camera system
    public CameraSystem(OrthographicCamera camera) {
        this.camera = camera;
    }

    // Set the initial camera position
    public void setInitialPosition(float x, float y) {
        camera.position.x = x;
        camera.position.y = y;
        initialCameraPosition = new Vector2(x, y);

        // Reset camera follow state
        cameraFollowsPlayer = false;
        cameraFollowTimer = 0f;
    }

    // Update the camera position to follow the player
    public void update(float delta, PlayerMovement playerMovement, boolean gameStarted) {
        if (camera == null || playerMovement == null) {
            return;
        }

        // Get the player's position
        Vector2 playerPos = playerMovement.getBody().getPosition();

        // Update camera follow timer
        if (!cameraFollowsPlayer && gameStarted) {
            cameraFollowTimer += delta;
            if (cameraFollowTimer >= cameraFollowDelay) {
                cameraFollowsPlayer = true;
            }
        }

        // If the game has started and the camera should follow the player
        if (gameStarted && cameraFollowsPlayer) {
            // Smoothly move the camera toward the player (only horizontally)
            camera.position.x += (playerPos.x * PhysicsConstants.PPM - camera.position.x) * CAMERA_LERP;
        }
        // Otherwise, keep the camera at its initial position
        else {
            if (initialCameraPosition != null) {
                camera.position.x = initialCameraPosition.x;
                camera.position.y = initialCameraPosition.y;
            }
        }

        // Keep the camera within the world bounds
        constrainCamera();

        // Update the camera's projection matrix
        camera.update();
    }

    // Keep the camera within the world bounds
    private void constrainCamera() {
        // Calculate the edges of the viewport
        float viewportHalfWidth = camera.viewportWidth / 2f;
        float viewportHalfHeight = camera.viewportHeight / 2f;

        // Calculate the size of the world
        float worldWidth = Gdx.graphics.getWidth() * WORLD_WIDTH_MULTIPLIER;
        float worldHeight = Gdx.graphics.getHeight() * WORLD_HEIGHT_MULTIPLIER;

        // Keep the camera within the world bounds
        camera.position.x = Math.max(viewportHalfWidth, Math.min(worldWidth - viewportHalfWidth, camera.position.x));
        camera.position.y = Math.max(viewportHalfHeight, Math.min(worldHeight - viewportHalfHeight, camera.position.y));
    }

    // Reset camera follow state
    public void resetFollowState() {
        cameraFollowsPlayer = false;
        cameraFollowTimer = 0f;
    }

    // Update the batch's projection matrix
    public void updateBatchProjection(SpriteBatch batch) {
        if (batch != null && camera != null) {
            batch.setProjectionMatrix(camera.combined);
        }
    }

    // Get the camera
    public OrthographicCamera getCamera() {
        return camera;
    }
}
