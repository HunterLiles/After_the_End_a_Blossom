package io.github.HunterLiles.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import io.github.HunterLiles.logic.PhysicsConstants;
import io.github.HunterLiles.logic.PlayerMovement;

// Handles all physics-related functionality
public class PhysicsSystem implements Disposable {
    // Physics simulation constants
    private static final float PHYSICS_STEP_TIME = 1/60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    // Physics components
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private ContactListener contactListener;

    // Constructor - creates a new physics system
    public PhysicsSystem() {
        // Create the physics world with gravity
        world = new World(PhysicsConstants.getGravity(), true);

        // Create a renderer for showing physics shapes (for debugging)
        debugRenderer = new Box2DDebugRenderer();
    }

    // Update the physics simulation
    public void update(float delta) {
        if (world != null) {
            world.step(PHYSICS_STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    // Draw debug shapes for physics bodies
    public void renderDebug(OrthographicCamera camera) {
        if (debugRenderer != null && world != null && camera != null) {
            debugRenderer.render(world, camera.combined.scl(PhysicsConstants.PPM));
        }
    }

    // Set up collision detection for the player's feet
    public void createContactListener(final PlayerMovement playerMovement, final String footSensorId) {
        // Create a new contact listener
        contactListener = new ContactListener() {
            // Called when two objects start touching
            @Override
            public void beginContact(Contact contact) {
                // Get the two fixtures (physics shapes) that are touching
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Check if either fixture is the player's foot sensor
                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals(footSensorId) ||
                    fixtureB.getUserData() != null && fixtureB.getUserData().equals(footSensorId)) {
                    // If the foot sensor is touching something, the player can jump
                    playerMovement.setCanJump(true);
                }
            }

            // Called when two objects stop touching
            @Override
            public void endContact(Contact contact) {
                // Get the two fixtures that were touching
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Check if either fixture is the player's foot sensor
                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals(footSensorId) ||
                    fixtureB.getUserData() != null && fixtureB.getUserData().equals(footSensorId)) {
                    // If the foot sensor is no longer touching anything, the player can't jump
                    playerMovement.setCanJump(false);
                }
            }

            // These methods are required but we don't need them
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) { }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) { }
        };

        // Add the contact listener to the physics world
        world.setContactListener(contactListener);
    }

    // Get the physics world
    public World getWorld() {
        return world;
    }

    // Clean up resources
    @Override
    public void dispose() {
        if (debugRenderer != null) {
            debugRenderer.dispose();
            debugRenderer = null;
        }

        if (world != null) {
            world.dispose();
            world = null;
        }
    }
}
