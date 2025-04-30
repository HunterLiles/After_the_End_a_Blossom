package io.github.HunterLiles.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;

// This class represents our player character in the game
public class Player extends Actor {
    // Time counter for animations
    private float elapsedTime;

    // The animation that's currently playing
    private Animation<TextureRegion> currentAnimation;

    // A collection of all animations for different states (running, jumping, etc.)
    private HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();

    // The atlas contains all the player's images
    private TextureAtlas atlas;

    // Default size for drawing the player
    private static final float DEFAULT_WIDTH = 120f;
    private static final float DEFAULT_HEIGHT = 100f;

    // Constructor - creates a new player
    // location: the name of the atlas file with player images
    // initialState: the starting animation state (like "idle" or "run")
    public Player(String location, String initialState) {
        // Load the atlas file with all player images
        atlas = new TextureAtlas(Gdx.files.internal("images/" + location + ".atlas"));

        // Load the initial animation
        loadAnimation(initialState);
        currentAnimation = animations.get(initialState);

        // Start the animation timer at 0
        elapsedTime = 0f;
    }

    // Load an animation if it hasn't been loaded already
    // state: the name of the animation to load (like "run", "jump", etc.)
    private void loadAnimation(String state) {
        // Only load if we don't already have this animation
        if (!animations.containsKey(state)) {
            // Create a new animation that plays at 4 frames per second
            Animation<TextureRegion> animation = new Animation<>(1/4f, atlas.findRegions(state));

            // Add it to our collection
            animations.put(state, animation);
        }
    }

    // Change which animation is playing
    // state: the name of the animation to play
    public void setAnimation(String state) {
        // Make sure the animation is loaded
        loadAnimation(state);

        // Set it as the current animation
        currentAnimation = animations.get(state);
    }

    // Update the animation timer
    // delta: time since the last update (in seconds)
    public void update(float delta) {
        // Add the elapsed time to advance the animation
        elapsedTime += delta;
    }

    // Draw the player on the screen
    // batch: the SpriteBatch to draw with
    // x, y: the position to draw at
    public void render(Batch batch, float x, float y) {
        // Only try to draw if we have an animation
        if (currentAnimation != null) {
            // Draw the current frame of the animation
            // true means the animation will loop
            batch.draw(currentAnimation.getKeyFrame(elapsedTime, true), x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }
}
