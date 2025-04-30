package io.github.HunterLiles.screens;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.HunterLiles.logic.PhysicsConstants;

// This class represents a portal that the player can enter to go to a different screen
// It's a rectangular area in the game world that triggers a transition when the player enters it
public class PortalTrigger implements TransitionTrigger {
    private Rectangle bounds;           // The area of the portal in the game world
    private String targetSceneId;       // The name of the screen to go to
    private Vector2 targetPosition;     // Where the player should appear in the new screen
    private String description;         // Text description of the portal
    private boolean requiresInteraction; // Whether the player needs to press a key to use the portal

    // Create a new portal
    // x, y: position of the top-left corner of the portal
    // width, height: size of the portal
    // targetSceneId: name of the screen to go to
    // targetX, targetY: where the player should appear in the new screen
    // description: text description of the portal
    // requiresInteraction: whether the player needs to press a key to use the portal
    public PortalTrigger(float x, float y, float width, float height,
                         String targetSceneId, float targetX, float targetY,
                         String description, boolean requiresInteraction) {
        // Create a rectangle representing the portal area
        this.bounds = new Rectangle(x, y, width, height);

        // Save the target screen name
        this.targetSceneId = targetSceneId;

        // Save the position where the player should appear in the new screen
        this.targetPosition = new Vector2(targetX, targetY);

        // Save the description
        this.description = description;

        // Save whether interaction is required
        this.requiresInteraction = requiresInteraction;
    }

    // Check if the portal should activate
    @Override
    public boolean isTriggered(Body playerBody, Vector2 playerPosition) {
        // Convert physics position to screen position
        // (physics uses smaller units than the screen)
        float screenX = playerPosition.x * PhysicsConstants.PPM;
        float screenY = playerPosition.y * PhysicsConstants.PPM;

        // Check if the player is inside the portal area
        boolean inBounds = bounds.contains(screenX, screenY);

        // If the portal requires a key press
        if (requiresInteraction) {
            // Just return whether the player is in bounds
            // The scene will check for the key press separately
            return inBounds;
        } else {
            // If no key press is needed, activate as soon as the player enters
            return inBounds;
        }
    }

    // Get the name of the screen to go to
    @Override
    public String getTargetSceneId() {
        return targetSceneId;
    }

    // Get where the player should appear in the new screen
    @Override
    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    // Get the text description of this portal
    @Override
    public String getDescription() {
        return description;
    }

    // Check if this portal requires a key press to activate
    public boolean requiresInteraction() {
        return requiresInteraction;
    }

    // Get the area of this portal in the game world
    public Rectangle getBounds() {
        return bounds;
    }
}
