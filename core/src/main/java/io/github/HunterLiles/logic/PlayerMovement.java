package io.github.HunterLiles.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static io.github.HunterLiles.logic.PhysicsConstants.*;

// This class handles the player's movement, physics, and controls
public class PlayerMovement {
    // Size of the player's physics body
    private static final float PLAYER_WIDTH = 30f;
    private static final float PLAYER_HEIGHT = 50f;

    // Size and position of the foot sensor (used to detect when player is on ground)
    private static final float FOOT_SENSOR_WIDTH = 25f;
    private static final float FOOT_SENSOR_HEIGHT = 2f;
    private static final float FOOT_SENSOR_Y_OFFSET = -50f;

    // Names of the different animation states
    private static final String ANIM_JUMP = "jump";             // Jumping upward
    private static final String ANIM_FALL = "fall";             // Falling downward
    private static final String ANIM_RUN = "run";               // Running right
    private static final String ANIM_FLIPPED_RUN = "flipped-run"; // Running left
    private static final String ANIM_FLIPPED_JUMP = "flipped-jump"; // Jumping while facing left
    private static final String ANIM_FLIPPED_FALL = "flipped-fall"; // Falling while facing left
    private static final String ANIM_IDLE = "idle";             // Standing still

    // ID for the foot sensor (used in collision detection)
    private static final String FOOT_SENSOR_ID = "foot";

    // Movement settings
    private static final float VELOCITY_DAMPING = 0.8f;      // How quickly player slows down
    private static final float MOVEMENT_THRESHOLD = 0.1f;    // Minimum speed to be considered "moving"

    // Where the player starts in the level
    private static final float START_X = 5f; // Not too close to the edge
    private static final float START_Y = 2f; // A bit above the ground

    // References to other game objects
    private Player player;              // The player's visual representation
    private OrthographicCamera camera;  // The game camera
    private World world;                // The physics world
    private Body body;                  // The player's physics body
    private boolean canJump;            // Whether the player can jump right now

    // Constructor - create player movement with default starting position
    public PlayerMovement(Player player, OrthographicCamera camera, World world) {
        this(player, camera, world, START_X, START_Y);
    }

    // Constructor - create player movement with custom starting position
    public PlayerMovement(Player player, OrthographicCamera camera, World world, float startX, float startY) {
        this.player = player;
        this.camera = camera;
        this.world = world;
        this.canJump = false;
        createBody(startX, startY);
    }

    // Create the player's physics body at the default position
    private void createBody() {
        createBody(START_X, START_Y);
    }

    // Create the player's physics body at a specific position
    private void createBody(float startX, float startY) {
        // Define the body properties
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;  // Dynamic means it moves and is affected by physics
        bodyDef.position.set(startX, startY);         // Starting position
        bodyDef.fixedRotation = true;                 // Prevent the player from rotating

        // Create the body in the physics world
        body = world.createBody(bodyDef);

        // Create a box shape for the player
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PLAYER_WIDTH / PPM, PLAYER_HEIGHT / PPM);

        // Define the physical properties of the player
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;        // How heavy the player is
        fixtureDef.friction = 0.2f;     // How much the player slides
        fixtureDef.restitution = 0.1f;  // How bouncy the player is

        // Add the shape to the body
        body.createFixture(fixtureDef);

        // Create a foot sensor at the bottom of the player
        // This is used to detect when the player is standing on something
        shape.setAsBox(FOOT_SENSOR_WIDTH / PPM, FOOT_SENSOR_HEIGHT / PPM,
                      new Vector2(0, FOOT_SENSOR_Y_OFFSET / PPM), 0f);
        fixtureDef.isSensor = true;  // Sensors detect collisions but don't cause physical reactions
        body.createFixture(fixtureDef).setUserData(FOOT_SENSOR_ID);

        // Clean up the shape
        shape.dispose();
    }

    // Update the player's movement and animation
    public void update(float delta) {
        handleInput();     // Process keyboard input
        updateAnimation(); // Update the player's animation based on movement
    }

    // Handle keyboard input for player movement
    private void handleInput() {
        // Make sure the physics body exists
        if (body == null) {
            Gdx.app.error("PlayerMovement", "Body is null in handleInput");
            return;
        }

        // Get the current velocity and position
        Vector2 velocity = body.getLinearVelocity();
        Vector2 position = body.getPosition();

        // Move right when D or right arrow is pressed
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.setLinearVelocity(MAX_VELOCITY, velocity.y);
        }
        // Move left when A or left arrow is pressed
        else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.setLinearVelocity(-MAX_VELOCITY, velocity.y);
        }
        // Slow down when no movement keys are pressed
        else {
            body.setLinearVelocity(velocity.x * VELOCITY_DAMPING, velocity.y);
        }

        try {
            // Jump when space or W is pressed and player is on the ground
            if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.W)) && canJump) {
                // Reset vertical velocity and apply an upward force
                body.setLinearVelocity(velocity.x, 0);
                body.applyLinearImpulse(0, JUMP_FORCE, position.x, position.y, true);
                canJump = false;  // Can't jump again until landing
            }
        } catch (Exception e) {
            Gdx.app.error("PlayerMovement", "Error during jump: " + e.getMessage());
        }

        // Make sure the player doesn't move too fast
        velocity = body.getLinearVelocity();
        if (Math.abs(velocity.x) > MAX_VELOCITY) {
            velocity.x = Math.signum(velocity.x) * MAX_VELOCITY;
            body.setLinearVelocity(velocity.x, velocity.y);
        }
    }

    // Update the player's animation based on movement
    private void updateAnimation() {
        Vector2 velocity = body.getLinearVelocity();
        boolean movingLeft = velocity.x < 0;  // Check if moving left

        // If in the air (can't jump)
        if (!canJump) {
            if (velocity.y > 0) {
                // Moving upward - use jump animation
                player.setAnimation(movingLeft ? ANIM_FLIPPED_JUMP : ANIM_JUMP);
            } else {
                // Moving downward - use fall animation
                player.setAnimation(movingLeft ? ANIM_FLIPPED_FALL : ANIM_FALL);
            }
        }
        // If on the ground and moving
        else if (Math.abs(velocity.x) > MOVEMENT_THRESHOLD) {
            // Use run animation (flipped if moving left)
            player.setAnimation(movingLeft ? ANIM_FLIPPED_RUN : ANIM_RUN);
        }
        // If on the ground and not moving
        else {
            // Use idle animation
            player.setAnimation(ANIM_IDLE);
        }
    }

    // Set whether the player can jump (called by collision system)
    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    // Get the player's physics body
    public Body getBody() {
        return body;
    }
}
