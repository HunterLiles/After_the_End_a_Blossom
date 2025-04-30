package io.github.HunterLiles.logic;

import com.badlogic.gdx.math.Vector2;

public class PhysicsConstants {
    public static final float PPM = 100f;

    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_GROUND = 0x0002;
    public static final short CATEGORY_PLATFORM = 0x0004;

    public static final float PLAYER_SPEED = 1f;
    public static final float JUMP_FORCE = 3f;
    public static final float MAX_VELOCITY = 1f;

    private static float gravityX = 0f;

    //Negative values pull downward
    private static float gravityY = -6f;

    public static Vector2 getGravity() {
        return new Vector2(gravityX, gravityY);
    }

    public static void setGravity(float x, float y) {
        gravityX = x;
        gravityY = y;
    }

    //Negative values pull downward
    public static void setGravityY(float y) {
        gravityY = y;
    }

    public static float getGravityY() {
        return gravityY;
    }
}
