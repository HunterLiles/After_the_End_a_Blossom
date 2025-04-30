package io.github.HunterLiles.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.github.HunterLiles.logic.PlayerMovement;
import io.github.HunterLiles.screens.BaseScene;
import io.github.HunterLiles.screens.PortalTrigger;
import io.github.HunterLiles.screens.TransitionTrigger;

import java.util.ArrayList;
import java.util.List;

// Handles all scene transition functionality
public class TransitionSystem {
    // Transition components
    private List<TransitionTrigger> transitionTriggers;
    private BaseScene scene;

    // Constructor - creates a new transition system
    public TransitionSystem(BaseScene scene) {
        this.scene = scene;
        this.transitionTriggers = new ArrayList<>();
    }

    // Add a transition trigger
    public void addTransitionTrigger(TransitionTrigger trigger) {
        if (trigger != null) {
            transitionTriggers.add(trigger);
        }
    }

    // Create a portal trigger
    public PortalTrigger createPortalTrigger(float x, float y, float width, float height,
                                           String targetSceneId, float targetX, float targetY,
                                           String description, boolean requiresInteraction) {
        PortalTrigger portal = new PortalTrigger(
            x, y, width, height,
            targetSceneId,
            targetX, targetY,
            description,
            requiresInteraction
        );

        addTransitionTrigger(portal);
        return portal;
    }

    // Check if the player has entered any portals
    public void checkTransitionTriggers(PlayerMovement playerMovement) {
        if (playerMovement == null || scene == null) {
            return;
        }

        // Get the player's position
        Vector2 playerPos = playerMovement.getBody().getPosition();

        // Check each transition trigger
        for (TransitionTrigger trigger : transitionTriggers) {
            // If the player has triggered this transition
            if (trigger.isTriggered(playerMovement.getBody(), playerPos)) {
                // If it's a portal trigger
                if (trigger instanceof PortalTrigger) {
                    PortalTrigger portal = (PortalTrigger) trigger;

                    // If the portal doesn't require a key press, or if it does and the player pressed E
                    if (!portal.requiresInteraction() ||
                        (portal.requiresInteraction() && Gdx.input.isKeyJustPressed(Input.Keys.E))) {
                        // Transition to the target scene
                        scene.transitionTo(trigger.getTargetSceneId());
                        break;  // Stop checking other triggers
                    }
                }
                // If it's another type of trigger
                else {
                    // Transition to the target scene
                    scene.transitionTo(trigger.getTargetSceneId());
                    break;  // Stop checking other triggers
                }
            }
        }
    }

    // Clear all transition triggers
    public void clearTransitionTriggers() {
        transitionTriggers.clear();
    }
}
