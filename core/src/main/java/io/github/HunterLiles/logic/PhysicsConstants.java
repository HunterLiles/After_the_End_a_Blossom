package io.github.HunterLiles.logic;

import com.badlogic.gdx.math.Vector2;

// This class stores all the physics-related numbers we use in our game
public class PhysicsConstants {
    // PPM means "Pixels Per Meter" - it helps convert between game world units and screen pixels
    // A higher number means objects appear larger on screen
    public static final float PPM = 100f;

    // These are "collision categories" that help determine what objects can collide with each other
    public static final short CATEGORY_PLAYER = 0x0001;   // For the player character
    public static final short CATEGORY_GROUND = 0x0002;   // For the ground
    public static final short CATEGORY_PLATFORM = 0x0004; // For platforms the player can stand on

    // Movement-related constants
    public static final float PLAYER_SPEED = 1f;   // How fast the player moves
    public static final float JUMP_FORCE = 3f;     // How high the player jumps
    public static final float MAX_VELOCITY = 1f;   // Maximum speed the player can move

    // Gravity settings (like in the real world, but we can change it)
    // X-axis gravity (left/right) - usually 0
    private static float gravityX = 0f;

    // Y-axis gravity (up/down) - negative values pull downward
    private static float gravityY = -6f;

    // Get the current gravity as a 2D vector (x and y components)
    public static Vector2 getGravity() {
        return new Vector2(gravityX, gravityY);
    }

    // Change both x and y gravity at once
    public static void setGravity(float x, float y) {
        gravityX = x;
        gravityY = y;
    }

    // Change only the y gravity (up/down)
    // Negative values pull downward, positive values pull upward
    public static void setGravityY(float y) {
        gravityY = y;
    }

    // Get the current y gravity value
    public static float getGravityY() {
        return gravityY;
    }
}
