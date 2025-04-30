package io.github.HunterLiles.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

// This interface defines what a scene transition trigger should do
// A trigger is something that causes the game to switch to a different screen
// Examples: portals, doors, level completion, etc.
public interface TransitionTrigger {
    // Check if the trigger should activate
    // playerBody: the player's physics body
    // playerPosition: the player's position in the world
    // Returns true if we should transition to another screen
    boolean isTriggered(Body playerBody, Vector2 playerPosition);

    // Get the name of the screen we should switch to
    // Returns the ID of the target screen
    String getTargetSceneId();

    // Get the position where the player should appear in the new screen
    // Returns the position in the target screen, or null to use the default position
    Vector2 getTargetPosition();

    // Get a description of this trigger (for display to the player)
    // Returns a text description
    String getDescription();
}
