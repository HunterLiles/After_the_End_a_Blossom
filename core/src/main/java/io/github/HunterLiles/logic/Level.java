package io.github.HunterLiles.logic;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Represents a game level with specific era theme and properties.
 * Each level has its own background layers, ground properties, and theme.
 */
public class Level {
    // Era themes
    public enum Era {
        PREHISTORIC,
        ANCIENT,
        MEDIEVAL,
        INDUSTRIAL,
        MODERN,
        FUTURE
    }

    private Era era;
    private String name;
    private ParallaxLayer[] backgroundLayers;
    private float groundHeight;
    private float groundFriction;
    private String musicPath;
    private OrthographicCamera camera;

    /**
     * Creates a new level with the specified properties.
     *
     * @param era The era theme of the level
     * @param name The name of the level
     * @param groundHeight The height of the ground
     * @param groundFriction The friction of the ground
     * @param musicPath The path to the background music file
     */
    public Level(Era era, String name, float groundHeight, float groundFriction, String musicPath) {
        this.era = era;
        this.name = name;
        this.groundHeight = groundHeight;
        this.groundFriction = groundFriction;
        this.musicPath = musicPath;
    }

    /**
     * Initializes the level's background layers based on the era theme.
     *
     * @param camera The camera to use for parallax effects
     */
    public void initializeBackgroundLayers(OrthographicCamera camera) {
        this.camera = camera;

        // Different background layer configurations based on era
        switch (era) {
            case PREHISTORIC:
                createPrehistoricBackground();
                break;
            case ANCIENT:
                createAncientBackground();
                break;
            case MEDIEVAL:
                createMedievalBackground();
                break;
            case INDUSTRIAL:
                createIndustrialBackground();
                break;
            case MODERN:
                createModernBackground();
                break;
            case FUTURE:
                createFutureBackground();
                break;
        }
    }

    /**
     * Creates the ground bodies for this level in the physics world.
     *
     * @param world The physics world
     * @param camera The camera for positioning
     */
    public void createGroundBodies(World world, OrthographicCamera camera) {
        // Create a platform at the bottom of the screen that extends infinitely in both directions
        com.badlogic.gdx.physics.box2d.BodyDef groundBodyDef = new com.badlogic.gdx.physics.box2d.BodyDef();
        groundBodyDef.position.set(0, groundHeight);

        com.badlogic.gdx.physics.box2d.Body groundBody = world.createBody(groundBodyDef);
        com.badlogic.gdx.physics.box2d.PolygonShape groundBox = new com.badlogic.gdx.physics.box2d.PolygonShape();

        // Make the ground extremely wide to simulate infinity
        float groundWidthMultiplier = 1000f;
        groundBox.setAsBox(camera.viewportWidth * groundWidthMultiplier / PhysicsConstants.PPM, 1f);

        com.badlogic.gdx.physics.box2d.FixtureDef fixtureDef = new com.badlogic.gdx.physics.box2d.FixtureDef();
        fixtureDef.shape = groundBox;
        fixtureDef.filter.categoryBits = PhysicsConstants.CATEGORY_GROUND;
        fixtureDef.friction = groundFriction;
        groundBody.createFixture(fixtureDef);

        groundBox.dispose();
    }

    /**
     * Renders the background layers of the level.
     *
     * @param batch The sprite batch to use for rendering
     */
    public void renderBackground(SpriteBatch batch) {
        if (backgroundLayers != null) {
            for (ParallaxLayer layer : backgroundLayers) {
                layer.render(batch);
            }
        }
    }

    /**
     * Creates background layers for the prehistoric era.
     * Uses existing background images with color tinting to create a prehistoric feel.
     */
    private void createPrehistoricBackground() {
        backgroundLayers = new ParallaxLayer[12];
        // Use existing background images with different parallax factors
        // and color tinting to create a prehistoric feel
        for (int i = 1; i <= 12; i++) {
            Texture texture = new Texture("images/background/" + i + ".png");
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            float factor = i / 12f; // Slower parallax for distant layers
            backgroundLayers[i-1] = new ParallaxLayer(texture, factor, true, false);
            backgroundLayers[i-1].setCamera(camera);
            backgroundLayers[i-1].setPosition(0, 0);
            backgroundLayers[i-1].setSize(camera.viewportWidth, camera.viewportHeight);
            // For prehistoric era, use a warmer, more earthy color tint
            backgroundLayers[i-1].setTint(1.0f, 0.9f, 0.7f, 1.0f);
        }
    }

    /**
     * Creates background layers for the ancient era.
     */
    private void createAncientBackground() {
        backgroundLayers = new ParallaxLayer[12];
        for (int i = 1; i <= 12; i++) {
            Texture texture = new Texture("images/background/" + i + ".png");
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            float factor = i / 12f;
            backgroundLayers[i-1] = new ParallaxLayer(texture, factor, true, false);
            backgroundLayers[i-1].setCamera(camera);
            backgroundLayers[i-1].setPosition(0, 0);
            backgroundLayers[i-1].setSize(camera.viewportWidth, camera.viewportHeight);
            // For ancient era, use a sandy, desert-like color tint
            backgroundLayers[i-1].setTint(1.0f, 0.95f, 0.8f, 1.0f);
        }
    }

    /**
     * Creates background layers for the medieval era.
     */
    private void createMedievalBackground() {
        backgroundLayers = new ParallaxLayer[12];
        for (int i = 1; i <= 12; i++) {
            Texture texture = new Texture("images/background/" + i + ".png");
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            float factor = i / 12f;
            backgroundLayers[i-1] = new ParallaxLayer(texture, factor, true, false);
            backgroundLayers[i-1].setCamera(camera);
            backgroundLayers[i-1].setPosition(0, 0);
            backgroundLayers[i-1].setSize(camera.viewportWidth, camera.viewportHeight);
            // For medieval era, use a cooler, more stone-like color tint
            backgroundLayers[i-1].setTint(0.8f, 0.8f, 0.9f, 1.0f);
        }
    }

    /**
     * Creates background layers for the industrial era.
     */
    private void createIndustrialBackground() {
        backgroundLayers = new ParallaxLayer[12];
        for (int i = 1; i <= 12; i++) {
            Texture texture = new Texture("images/background/" + i + ".png");
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            float factor = i / 12f;
            backgroundLayers[i-1] = new ParallaxLayer(texture, factor, true, false);
            backgroundLayers[i-1].setCamera(camera);
            backgroundLayers[i-1].setPosition(0, 0);
            backgroundLayers[i-1].setSize(camera.viewportWidth, camera.viewportHeight);
            // For industrial era, use a darker, more smoky color tint
            backgroundLayers[i-1].setTint(0.7f, 0.7f, 0.7f, 1.0f);
        }
    }

    /**
     * Creates background layers for the modern era.
     */
    private void createModernBackground() {
        backgroundLayers = new ParallaxLayer[12];
        for (int i = 1; i <= 12; i++) {
            Texture texture = new Texture("images/background/" + i + ".png");
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            float factor = i / 12f;
            backgroundLayers[i-1] = new ParallaxLayer(texture, factor, true, false);
            backgroundLayers[i-1].setCamera(camera);
            backgroundLayers[i-1].setPosition(0, 0);
            backgroundLayers[i-1].setSize(camera.viewportWidth, camera.viewportHeight);
            // For modern era, use a neutral color tint
            backgroundLayers[i-1].setTint(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    /**
     * Creates background layers for the future era.
     */
    private void createFutureBackground() {
        backgroundLayers = new ParallaxLayer[12];
        for (int i = 1; i <= 12; i++) {
            Texture texture = new Texture("images/background/" + i + ".png");
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            float factor = i / 12f;
            backgroundLayers[i-1] = new ParallaxLayer(texture, factor, true, false);
            backgroundLayers[i-1].setCamera(camera);
            backgroundLayers[i-1].setPosition(0, 0);
            backgroundLayers[i-1].setSize(camera.viewportWidth, camera.viewportHeight);
            // For future era, use a cooler, more tech-like color tint
            backgroundLayers[i-1].setTint(0.8f, 0.9f, 1.0f, 1.0f);
        }
    }

    // Getters and setters
    public Era getEra() {
        return era;
    }

    public String getName() {
        return name;
    }

    public float getGroundHeight() {
        return groundHeight;
    }

    public float getGroundFriction() {
        return groundFriction;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public ParallaxLayer[] getBackgroundLayers() {
        return backgroundLayers;
    }
}
