package io.github.HunterLiles.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static io.github.HunterLiles.logic.PhysicsConstants.*;

public class PlayerMovement {
    //Player dimensions and sensor constants
    private static final float PLAYER_WIDTH = 30f;
    private static final float PLAYER_HEIGHT = 50f;
    private static final float FOOT_SENSOR_WIDTH = 25f;
    private static final float FOOT_SENSOR_HEIGHT = 2f;
    private static final float FOOT_SENSOR_Y_OFFSET = -50f;

    //Animation state constants
    private static final String ANIM_JUMP = "jump";
    private static final String ANIM_FALL = "fall";
    private static final String ANIM_RUN = "run";
    private static final String ANIM_FLIPPED_RUN = "flipped-run";
    private static final String ANIM_FLIPPED_JUMP = "flipped-jump";
    private static final String ANIM_FLIPPED_FALL = "flipped-fall";
    private static final String ANIM_IDLE = "idle";

    //Foot sensor identifier
    private static final String FOOT_SENSOR_ID = "foot";

    //Movement constants
    private static final float VELOCITY_DAMPING = 0.8f;
    private static final float MOVEMENT_THRESHOLD = 0.1f;

    //Player starting position
    private static final float START_X = 5f; // Moved away from the edge to prevent camera snapping
    private static final float START_Y = 2f;

    private Player player;
    private OrthographicCamera camera;
    private World world;
    private Body body;
    private boolean canJump;

    public PlayerMovement(Player player, OrthographicCamera camera, World world) {
        this(player, camera, world, START_X, START_Y);
    }

    public PlayerMovement(Player player, OrthographicCamera camera, World world, float startX, float startY) {
        this.player = player;
        this.camera = camera;
        this.world = world;
        this.canJump = false;
        createBody(startX, startY);
    }

    private void createBody() {
        createBody(START_X, START_Y);
    }

    private void createBody(float startX, float startY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startX, startY);
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PLAYER_WIDTH / PPM, PLAYER_HEIGHT / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef);

        //Create a foot sensor for jump detection
        shape.setAsBox(FOOT_SENSOR_WIDTH / PPM, FOOT_SENSOR_HEIGHT / PPM,
                      new Vector2(0, FOOT_SENSOR_Y_OFFSET / PPM), 0f);
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(FOOT_SENSOR_ID);

        shape.dispose();
    }

    public void update(float delta) {
        handleInput();
        updateAnimation();
    }

    private void handleInput() {
        // Ensure body is valid
        if (body == null) {
            Gdx.app.error("PlayerMovement", "Body is null in handleInput");
            return;
        }

        Vector2 velocity = body.getLinearVelocity();
        Vector2 position = body.getPosition();

        // Debug log to verify input handling
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            Gdx.app.debug("PlayerMovement", "Right key pressed");
            body.setLinearVelocity(MAX_VELOCITY, velocity.y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            Gdx.app.debug("PlayerMovement", "Left key pressed");
            body.setLinearVelocity(-MAX_VELOCITY, velocity.y);
        } else {
            // Apply stronger damping when no keys are pressed
            body.setLinearVelocity(velocity.x * VELOCITY_DAMPING, velocity.y);
        }

        try {
            if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.W)) && canJump) {
                Gdx.app.debug("PlayerMovement", "Jump key pressed, canJump: " + canJump);
                // Apply jump impulse
                body.setLinearVelocity(velocity.x, 0);
                body.applyLinearImpulse(0, JUMP_FORCE, position.x, position.y, true);
                canJump = false;
            }
        } catch (Exception e) {
            Gdx.app.error("PlayerMovement", "Error during jump: " + e.getMessage());
        }

        // Ensure velocity doesn't exceed maximum
        velocity = body.getLinearVelocity();
        if (Math.abs(velocity.x) > MAX_VELOCITY) {
            velocity.x = Math.signum(velocity.x) * MAX_VELOCITY;
            body.setLinearVelocity(velocity.x, velocity.y);
        }
    }

    private void updateAnimation() {
        Vector2 velocity = body.getLinearVelocity();
        boolean movingLeft = velocity.x < 0;

        if (!canJump) {
            if (velocity.y > 0) {
                // Jumping
                player.setAnimation(movingLeft ? ANIM_FLIPPED_JUMP : ANIM_JUMP);
            } else {
                // Falling
                player.setAnimation(movingLeft ? ANIM_FLIPPED_FALL : ANIM_FALL);
            }
        } else if (Math.abs(velocity.x) > MOVEMENT_THRESHOLD) {
            player.setAnimation(movingLeft ? ANIM_FLIPPED_RUN : ANIM_RUN);
        } else {
            player.setAnimation(ANIM_IDLE);
        }
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public Body getBody() {
        return body;
    }
}
